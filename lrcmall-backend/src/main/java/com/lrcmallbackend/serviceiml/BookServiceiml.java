package com.lrcmallbackend.serviceiml;

import com.lrcmallbackend.dao.BookDao;
import com.lrcmallbackend.daoimpl.BookDaoimpl;
import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceiml implements BookService {
    @Autowired
    BookDao bookDao;

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
//    @Override
//    public Book getBookDetail(){
//        return bookRepository.getById();
//    }
}
