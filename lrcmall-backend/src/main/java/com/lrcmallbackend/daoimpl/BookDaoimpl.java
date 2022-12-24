package com.lrcmallbackend.daoimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcmallbackend.dao.BookDao;
import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.entity.BookImage;
import com.lrcmallbackend.entity.BookType;
import com.lrcmallbackend.repository.BookImageRepository;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.repository.BookTypeRepository;
import com.lrcmallbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookDaoimpl implements BookDao {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookImageRepository bookImageRepository;
    @Autowired
    BookTypeRepository bookTypeRepository;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ObjectMapper objectMapper;


    private void addToRedis(Book b) throws JsonProcessingException {
        int id = b.getBookId();
        System.out.println("book: " + id + " is not in Redis, add it into Redis now!");
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(b), Map.class);
        redisUtil.hmset("book:" + id, map);
        //将主键保存为一个list，遍历时方便查询
        redisUtil.lSet("book:id", id);
    }

    @Override
    public List<Book> getBooks() {
        List<Book> list = bookRepository.findBooksByShowStatus(1);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Book temp = list.get(i);
            temp.setImage(bookImageRepository.findById(i + 1).get().getUrl());
            list.set(i, temp);
        }
        return list;
    }

    // get book detail using redis
//    @Override
//    public Book getBookDetail(int bookId) {
//        System.out.println("Searching book: " + bookId + " in Redis");
//        Book ret;
//        try {
//            Object b = redisUtil.hmget("book:" + bookId);
//            try {
//                System.out.println(objectMapper.writeValueAsString(b));
//                if (objectMapper.writeValueAsString(b).equals("{}")) {
//                    ret = bookRepository.getById(bookId);
//                    try {
//                        addToRedis(ret);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    System.out.println("Book: " + bookId + " already in Redis!");
//                    ret = objectMapper.readValue(objectMapper.writeValueAsString(b), Book.class);
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                ret = null;
//            }
//        } catch (RedisCommandTimeoutException e) {
//            System.out.println("Redis 连接断开，直接从数据库获取book: " + bookId);
//            ret = bookRepository.getById(bookId);
//        }
//        return ret;
//    }
    @Override
    public Book getBookDetail(int bookId) {
        Book ret = bookRepository.getById(bookId);
        Optional<BookImage> image = bookImageRepository.findById(bookId);
        String url = image.get().getUrl();
        ret.setImage(image.get().getUrl());
        return ret;
    }

    @Override
    public int addItemList(List<Map> orderlist, int orderId) {
        int pay = 0;
        for (int i = 0; i < orderlist.size(); i++) {
            int bookid = Integer.parseInt(orderlist.get(i).get("bookid").toString());
            int number = Integer.parseInt(orderlist.get(i).get("number").toString());
            //decrease the book surplus
            Book temp;
            temp = bookRepository.getById(bookid);

            int s = temp.getSurplus() - number;
            temp.setSurplus(s);
            pay += temp.getPrice() * number;
            bookRepository.saveAndFlush(temp);
        }
        return pay;
    }

    @Override
    public boolean deleteBook(String bookName) {
        List<Book> toBeRemoveList = bookRepository.findBookByName(bookName);
        if (toBeRemoveList.size() != 1) return false;
        Book toBeRemove = toBeRemoveList.get(0);
        toBeRemove.setShowStatus(0);
        bookRepository.save(toBeRemove);
        bookRepository.flush();
        //存在就设置，不存在就罢辽
//        try {
//            redisUtil.hset("book:" + toBeRemove.getBookId(), "show_status", 0);
//        } catch (RedisCommandTimeoutException e) {
//            System.out.println("Redis 连接断开，无需更新缓存中的数据 ");
//        }

        return true;
    }

    @Override
    public boolean addBook(String img, String name, String author, String type, String description, String isbn, int number, int price) {
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
        BookImage image = new BookImage(bookRepository.getMaxBookId(), img);
        bookImageRepository.save(image);
        return true;
    }

    @Override
    public boolean modifyBook(Book newBook) {
        Book temp = bookRepository.getById(newBook.getBookId());
        BookImage image = bookImageRepository.findById(newBook.getBookId()).get();
        temp.setName(newBook.getName());
        temp.setIsbn(newBook.getIsbn());
        temp.setDiscription(newBook.getDiscription());
        temp.setImage(newBook.getImage());
        image.setUrl(newBook.getImage());
        temp.setBookType(newBook.getBookType());
        temp.setAuthor(newBook.getAuthor());
        temp.setPrice(newBook.getPrice());
        temp.setSurplus(newBook.getSurplus());
        bookRepository.save(temp);
        bookImageRepository.save(image);
//        int bookid = newBook.getBookId();
//        try {
//            Object b = redisUtil.get("book:" + bookid);
//            if (b != null) {
//                System.out.println("Book: " + bookid + " already in Redis!");
//                try {
//                    addToRedis(temp);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (RedisCommandTimeoutException e) {
//            System.out.println("Redis 连接断开，无需更新缓存中的数据 ");
//        }
        return true;
    }

    @Override
    public int getMaxBookId() {
        return bookRepository.getMaxBookId();
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> list = bookRepository.findAll();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Book temp = list.get(i);
            temp.setImage(bookImageRepository.findById(i + 1).get().getUrl());
            list.set(i, temp);
        }
        return list;
    }

    @Override
    public List<Book> searchBookByTag(String tag) {
        List<BookType> tagLists = bookTypeRepository.searchTag(tag);
        List<Book> retList = new ArrayList<>();
        Set<Book> retSet = new HashSet<>();
        tagLists.forEach(tagname -> {
            retSet.addAll(bookRepository.findBookByBookTypeLike("%"+tagname.getName()+"%"));
        });
        retList.addAll(retSet);
        // add book cover
        retList.forEach(book->{
            Optional<BookImage> image = bookImageRepository.findById(book.getBookId());
            String url = image.get().getUrl();
            book.setImage(image.get().getUrl());
        });
        return retList;
    }
}
