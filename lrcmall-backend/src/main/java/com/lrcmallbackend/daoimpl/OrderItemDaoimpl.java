package com.lrcmallbackend.daoimpl;

import com.lrcmallbackend.dao.OrderItemDao;
import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.entity.Order;
import com.lrcmallbackend.entity.OrderItem;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.repository.OrderItemRepository;
import com.lrcmallbackend.repository.OrderRepository;
import com.lrcmallbackend.util.RedisUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class OrderItemDaoimpl implements OrderItemDao {
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    RedisUtil redisUtil;

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Transactional
    public boolean addItem(int orderId, int bookId, int number){
        Order order = orderRepository.findById(orderId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book == null)System.out.println("invalid bookid");
        if(order == null)System.out.println("order doesn't exist");
        if(order == null || book == null )return false;
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setBook(book);
        item.setNumber(number);
        orderItemRepository.save(item);
        bookRepository.saveAndFlush(book);
        redisUtil.hdel("book:"+bookId,"author");
        redisUtil.hdel("book:"+bookId,"book_type");
        redisUtil.hdel("book:"+bookId,"description");
        redisUtil.hdel("book:"+bookId,"image");
        redisUtil.hdel("book:"+bookId,"isbn");
        redisUtil.hdel("book:"+bookId,"name");
        redisUtil.hdel("book:"+bookId,"price");
        redisUtil.hdel("book:"+bookId,"show_status");
        redisUtil.hdel("book:"+bookId,"surplus");
        orderRepository.saveAndFlush(order);
//        int result = 10/0;
        return true;
    }

    @Override
    public boolean changeItemNumber(Order order,int bookId,int newNumber){
        Book book = bookRepository.getById(bookId);
        List<OrderItem> temp = orderItemRepository.findOrderItemsByOrderAndBook(order,book);
        OrderItem item = temp.get(0);
        item.setNumber(newNumber);
        orderItemRepository.save(item);
        return true;
    }
}
