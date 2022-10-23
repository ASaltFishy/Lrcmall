package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.Order;
import com.lrcmallbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order,Integer> {
//    @Query(value = "from Order,OrderItem where Order=OrderItem.order and type=0")
//    Order checkCart(@Param("user") User user);

    List<Order> findOrderByType(int type);

    List<Order> findOrderByOwnerAndType(User user,int type);

    List<Order> findOrderByTimeBetweenAndType(Date start,Date end,int type);

    List<Order> findOrderByTimeBetweenAndTypeAndOwner(Date start,Date end,int type,User owner);

//    @Query(nativeQuery = true, value = "select b.*,oi.number,oi.orderid from orders as o,orderitems as oi,books as b where o.ownerid=:userId and o.orderid=oi.orderid and oi.bookid = b.bookid and o.type=:type")
//    List<Map> getCartOrOrder(@RequestParam("userId") int userId,@RequestParam("type") int type);

}