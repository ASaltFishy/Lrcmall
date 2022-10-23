package com.lrcmallbackend.service;


import com.lrcmallbackend.dto.ReturnCustomer;
import com.lrcmallbackend.dto.ReturnUserBook;
import com.lrcmallbackend.entity.Order;
import com.lrcmallbackend.dto.ReturnBook;
import com.lrcmallbackend.dto.ReturnBookSale;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {
    int addItemList(List<Map> orderlist, int orderId);
    List<ReturnBook> getCart(int userId);
    List<Order> getOrder(int userId);
    int cartToOrder(int userId);
    List<Order> getAllOrder();
    List<ReturnBookSale> getAllSales(Date start,Date end);
    List<ReturnCustomer> getAllConsumptions(Date start,Date end);
    List<ReturnUserBook> getOneSale(Date start, Date end, int userId);
    boolean changeItemNumber(int userId,int bookId,int newNumber);
    boolean addToCart(int bookId,int number,int userId);
    String addToOrder(int bookId,int number,int userId);
}
