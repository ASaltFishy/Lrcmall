package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.Book;
import com.lrcmallbackend.entity.Order;
import com.lrcmallbackend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findOrderItemsByOrderAndBook(Order order, Book book);
}
