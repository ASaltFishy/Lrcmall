package com.lrcmallbackend.dao;

import com.lrcmallbackend.entity.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BookDao {
    List<Book> getBooks();
    List<Book> getAllBooks();
    Book getBookDetail(int bookId);
    int addItemList(List<Map> orderlist, int orderId);
    boolean deleteBook(String bookName);
    boolean addBook(String img,String name,String author,String type,String description,String isbn,int number,int price);
    boolean modifyBook(Book newBook);
    int getMaxBookId();
    List<Book> searchBookByTag(String tag);
}
