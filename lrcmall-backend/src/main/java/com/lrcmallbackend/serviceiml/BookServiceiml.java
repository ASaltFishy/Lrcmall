package com.lrcmallbackend.serviceiml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcmallbackend.dao.BookDao;
import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.service.BookService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceiml implements BookService {
    @Autowired
    BookDao bookDao;
    @Autowired
    SolrClient solrClient;
    @Autowired
    SolrTemplate solrTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<Book> getBooks(){
        return bookDao.getBooks();
    }

    @Override
    public Book getBookDetail(int bookId){
        return bookDao.getBookDetail(bookId);
    }

    @Override
    public boolean deleteBook(String bookName){
        return bookDao.deleteBook(bookName);
    }

    @Override
    public boolean addBook(Book newBook){
        return bookDao.addBook(newBook.getImage(), newBook.getName(), newBook.getAuthor(), newBook.getBookType(), newBook.getDiscription(), newBook.getIsbn(), newBook.getSurplus(), newBook.getPrice());
    }

    @Override
    public boolean modifyBook(Book newBook){
        return bookDao.modifyBook(newBook);
    }

    @Override
    public List<Book> searchBook(String keyword){
        SolrQuery query = new SolrQuery();
        List<Book> ret = new ArrayList<>();
        query.setQuery("description:"+keyword);
        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList documentList = response.getResults();
            if (!documentList.isEmpty()) {
                for (SolrDocument document:documentList) {
                    Book item = new Book();
                    item.setBookId(Integer.parseInt(document.getFieldValue("id").toString()));
                    item.setName(document.getFieldValue("name").toString());
                    item.setAuthor(document.getFieldValue("author").toString());
                    item.setDiscription(document.getFieldValue("description").toString());
                    ret.add(item);
                }
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public void setBookSolr(){
        List<Book> list = bookDao.getBooks();
            for(Book item : list){
                solrTemplate.saveBean("",item);
            }
            solrTemplate.commit("");
            System.out.println("solr 数据写入成功！");

    }

    @Override
    public List<Book> searchBookByTag(String tag){
        return bookDao.searchBookByTag(tag);
    }

}
