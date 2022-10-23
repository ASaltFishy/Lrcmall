package com.lrcmallbackend.dao;

import com.lrcmallbackend.entity.Order;

import java.math.BigDecimal;

public interface OrderItemDao {
    boolean addItem(int orderId, int bookId, int number);
    boolean changeItemNumber(Order order, int bookId, int newNumber);
}
