package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findBookByName(String bookName);

    List<Book> findBooksByShowStatus(int show_status);

    @Query("select max(b.bookId) from Book b")
    int getMaxBookId();
}