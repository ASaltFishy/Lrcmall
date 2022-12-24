package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.Book;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findBookByName(String bookName);

    List<Book> findBooksByShowStatus(int show_status);

    @Query("select max(b.bookId) from Book b")
    int getMaxBookId();

//    @Query(nativeQuery = true,value = "SELECT * FROM users WHERE find_in_set(-tag,book_type )")
    Set<Book> findBookByBookTypeLike(String tag);
}