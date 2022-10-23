package com.lrcmallbackend.dao;

import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.entity.Order;
import com.lrcmallbackend.entity.User;

import java.util.Date;
import java.util.List;

public interface OrderDao {
    Order checkCart(User owner);
    int addOrder(int userId,int type);
    int cartToOrder(int userId);
    List<Book> getCart(int userId);
    List<Order> getOrder(int userId);
    Order getMyCart(int userId);
    List<Order> getAllOrder();
    List<Order> getOrdersByTime(Date start,Date end);
    List<Order> getOrdersByTimeAndUser(Date start,Date end,User owner);
    void setOrderPay(int orderId,int bookId,int number);
}
