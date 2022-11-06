package com.lrcmallbackend.daoimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcmallbackend.dao.BookDao;
import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.util.RedisUtil;
import io.lettuce.core.RedisCommandTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BookDaoimpl implements BookDao {
    @Autowired
    BookRepository bookRepository;
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
        return bookRepository.findBooksByShowStatus(1);
    }

    @Override
    public Book getBookDetail(int bookId) {
        System.out.println("Searching book: " + bookId + " in Redis");
        Book ret;
        try {
            Object b = redisUtil.hmget("book:" + bookId);
            try {
                System.out.println(objectMapper.writeValueAsString(b));
                if (objectMapper.writeValueAsString(b).equals("{}")) {
                    ret = bookRepository.getById(bookId);
                    try {
                        addToRedis(ret);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Book: " + bookId + " already in Redis!");
                    ret = objectMapper.readValue(objectMapper.writeValueAsString(b), Book.class);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                ret = null;
            }
        } catch (RedisCommandTimeoutException e) {
            System.out.println("Redis 连接断开，直接从数据库获取book: " + bookId);
            ret = bookRepository.getById(bookId);
        }
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
//            try {
//                Object b = redisUtil.get("book:" + bookid);
//                if (b == null) {
            temp = bookRepository.getById(bookid);
//                    try {
//                        addToRedis(temp);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    System.out.println("Book: " + bookid + " already in Redis!");
//                    try {
//                        temp = objectMapper.readValue(objectMapper.writeValueAsString(redisUtil.hmget("book:" + bookid)), Book.class);
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                        temp = null;
//                    }
//                }
//            } catch (RedisCommandTimeoutException e) {
//                System.out.println("Redis 连接断开，直接从数据库获取book: " + bookid);
//                temp = bookRepository.getById(bookid);
//            }

            int s = temp.getSurplus() - number;
            temp.setSurplus(s);
            pay += temp.getPrice() * number;
            bookRepository.saveAndFlush(temp);
//            redisUtil.hset("book:" + bookid, "surplus", s);
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
        try {
            redisUtil.hset("book:" + toBeRemove.getBookId(), "show_status", 0);
        } catch (RedisCommandTimeoutException e) {
            System.out.println("Redis 连接断开，无需更新缓存中的数据 ");
        }

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
        return true;
    }

    @Override
    public boolean modifyBook(Book newBook) {
        Book temp = bookRepository.getById(newBook.getBookId());
        temp.setName(newBook.getName());
        temp.setIsbn(newBook.getIsbn());
        temp.setDiscription(newBook.getDiscription());
        temp.setImage(newBook.getImage());
        temp.setBookType(newBook.getBookType());
        temp.setAuthor(newBook.getAuthor());
        temp.setPrice(newBook.getPrice());
        temp.setSurplus(newBook.getSurplus());
        bookRepository.save(temp);
        int bookid = newBook.getBookId();
        try {
            Object b = redisUtil.get("book:" + bookid);
            if (b != null) {
                System.out.println("Book: " + bookid + " already in Redis!");
                try {
                    addToRedis(temp);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (RedisCommandTimeoutException e) {
            System.out.println("Redis 连接断开，无需更新缓存中的数据 ");
        }
        return true;
    }

    @Override
    public int getMaxBookId() {
        return bookRepository.getMaxBookId();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

}
