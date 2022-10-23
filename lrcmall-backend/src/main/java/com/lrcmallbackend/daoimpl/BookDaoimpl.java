package com.lrcmallbackend.daoimpl;

import com.lrcmallbackend.dao.BookDao;
import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BookDaoimpl implements BookDao {
    @Autowired
    BookRepository bookRepository;


    @Override
    public List<Book> getBooks(){
        return bookRepository.findBooksByShowStatus(1);
    }

    @Override
    public Book getBookDetail(int bookId){
        return bookRepository.getById(bookId);
    }


    @Override
    public int addItemList(List<Map> orderlist, int orderId){
       int pay = 0;
        for(int i=0;i<orderlist.size();i++){
            int bookid = Integer.parseInt(orderlist.get(i).get("bookid").toString());
            int number = Integer.parseInt(orderlist.get(i).get("number").toString());
            //decrease the book surplus
            Book temp = bookRepository.getById(bookid);
            temp.setSurplus(temp.getSurplus()-number);
            pay += temp.getPrice()*number;
            bookRepository.saveAndFlush(temp);
        }
        return pay;
    }

    @Override
    public boolean deleteBook(String bookName){
        List<Book> toBeRemoveList = bookRepository.findBookByName(bookName);
        if(toBeRemoveList.size() != 1)return false;
        Book toBeRemove = toBeRemoveList.get(0);
        toBeRemove.setShowStatus(0);
        bookRepository.save(toBeRemove);
        bookRepository.flush();
        return true;
    }

    @Override
    public boolean addBook(String img,String name,String author,String type,String description,String isbn,int number,int price){
        Book newBook = new Book();
        newBook.setName(name);
        newBook.setBookType(type);
        newBook.setAuthor(author);
        newBook.setSurplus(number);
        newBook.setImage(img);
        newBook.setDiscription(description);
        newBook.setPrice(price);
        newBook.setIsbn(isbn);
        newBook.setShowStatus(1);
        bookRepository.save(newBook);
        return true;
    }

    @Override
    public boolean modifyBook(Book newBook){
        Book temp = bookRepository.getById(newBook.getBookId());
        if(temp==null)return false;
        temp.setName(newBook.getName());
        temp.setIsbn(newBook.getIsbn());
        temp.setDiscription(newBook.getDiscription());
        temp.setImage(newBook.getImage());
        temp.setBookType(newBook.getBookType());
        temp.setAuthor(newBook.getAuthor());
        temp.setPrice(newBook.getPrice());
        temp.setSurplus(newBook.getSurplus());
        bookRepository.save(temp);
        return true;
    }

    @Override
    public int getMaxBookId()
    {
        return bookRepository.getMaxBookId();
    }

    @Override
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

}
