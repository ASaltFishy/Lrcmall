package com.lrcmallbackend.service;

import com.lrcmallbackend.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooks();
    Book getBookDetail(int bookId);
    boolean deleteBook(String bookName);
    boolean addBook(Book newBook);
    boolean modifyBook(Book newBook);
}
