package com.lrcmallbackend.controller;

import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping  ("/getBooks")
    public List<Book> getBooks(){
        System.out.println("getBooks!");
        return bookService.getBooks();
    }

    @RequestMapping("/getBookDetail")
    public Book getBookDetail(@RequestParam("bookid")int bookId){
        System.out.println("getBookDetail----"+bookId);
        return bookService.getBookDetail(bookId);
    }

    @RequestMapping("/deleteBooks")
    @ResponseBody
    public boolean deleteBook(@RequestBody Map<String,String> bookName){
        System.out.println("deleteBook----"+bookName.get("data"));
        return bookService.deleteBook(bookName.get("data"));
    }

    @RequestMapping("/addBook")
    @ResponseBody
    public boolean addBook(@RequestBody Book newBook){
        System.out.println("addBook----"+newBook.getName());
        return bookService.addBook(newBook);
    }
    @RequestMapping("/modifyBook")
    @ResponseBody
    public boolean modifyBook(@RequestBody Book newBook){
        System.out.println("addBook----"+newBook.getName());
        return bookService.modifyBook(newBook);
    }

    @RequestMapping("/searchBook")
    public List<Book> searchBook(@RequestParam("keyword")String keyword){
        System.out.println("searchBook----"+keyword);
        return bookService.searchBook(keyword);
    }

    @RequestMapping("/setBookSolr")
    void addBookSolr(){
        System.out.println("Get Request To Add Book");
        bookService.setBookSolr();
    }




    //暂时没用上
//    @RequestMapping("/addToOrder")
//    public BigDecimal addOrderItem(@RequestBody List<Map> orderlist, @RequestParam("userid") int userId){
//        System.out.println("addToOrder!");
//        int orderid = orderService.addOrder(userId,1);
//        return orderService.addItemList(orderlist,orderid);
//    }

}
