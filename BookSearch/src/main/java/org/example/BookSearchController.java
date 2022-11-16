package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookSearchController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping("/getBook")
    public List<BookEntity> searchAuthorByBook(@RequestParam("bookName") String bookName) {
        System.out.println("收到查询请求--"+bookName);
        List<Book> list = bookRepository.findBookByName(bookName);
        List<BookEntity> ret = new ArrayList<>();
        for(Book item:list){
            ret.add(new BookEntity(item.getBookId(),item.getName(),item.getAuthor()));
        }
        return ret;
    }
}
