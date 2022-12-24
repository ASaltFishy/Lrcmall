package com.lrcmallbackend.controller;

import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.entity.BookImage;
import com.lrcmallbackend.entity.BookType;
import com.lrcmallbackend.repository.BookImageRepository;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.repository.BookTypeRepository;
import com.lrcmallbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

//    @Autowired
//    BookTypeRepository bookTypeRepository;
//    @RequestMapping("/init")
//    public void init(){
////        ArrayList<String> typename = new ArrayList<String>(Arrays.asList("世界名著", "编程", "儿童文学","魔幻小说","外国文学","历史","科幻小说","学习","传记文学","青春文学","中小学教辅","武侠小说"));
////        typename.forEach(name->{
////            BookType a = new BookType(name);
////            bookTypeRepository.save(a);
////        });
//        BookType novel = bookTypeRepository.findByName("小说");
//        BookType fiction = bookTypeRepository.findByName("科幻小说");
//        BookType mohuan = bookTypeRepository.findByName("魔幻小说");
//        BookType wuxia = bookTypeRepository.findByName("武侠小说");
//        BookType waiguo = bookTypeRepository.findByName("外国文学");
//        BookType zhuanji = bookTypeRepository.findByName("传记文学");
//        BookType qingchun = bookTypeRepository.findByName("青春文学");
//        BookType shijiemingzhu = bookTypeRepository.findByName("世界名著");
//        BookType biancheng = bookTypeRepository.findByName("编程");
//        BookType jiaofu = bookTypeRepository.findByName("中小学教辅");
//        BookType xuexi = bookTypeRepository.findByName("学习");
//        BookType ertong = bookTypeRepository.findByName("儿童文学");
//        BookType lishi = bookTypeRepository.findByName("历史");
//
//        xuexi.relateWith(lishi);
//        lishi.relateWith(xuexi);
//
//        bookTypeRepository.save(novel);
//        bookTypeRepository.save(fiction);
//        bookTypeRepository.save(mohuan);
//        bookTypeRepository.save(wuxia);
//        bookTypeRepository.save(waiguo);
//        bookTypeRepository.save(zhuanji);
//        bookTypeRepository.save(qingchun);
//        bookTypeRepository.save(shijiemingzhu);
//        bookTypeRepository.save(biancheng);
//        bookTypeRepository.save(jiaofu);
//        bookTypeRepository.save(xuexi);
//        bookTypeRepository.save(ertong);
//        bookTypeRepository.save(lishi);
//    }

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

    @RequestMapping("/searchByTag")
    public List<Book> searchByTag(@RequestParam("tag")String tag){
        System.out.println("searchBookByTag----"+tag);
        return bookService.searchBookByTag(tag);
    }

}
