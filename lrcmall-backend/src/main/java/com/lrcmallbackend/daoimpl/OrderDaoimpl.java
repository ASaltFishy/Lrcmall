package com.lrcmallbackend.daoimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcmallbackend.dao.OrderDao;
import com.lrcmallbackend.entity.*;
import com.lrcmallbackend.repository.BookRepository;
import com.lrcmallbackend.repository.OrderRepository;
import com.lrcmallbackend.repository.UserRepository;
import com.lrcmallbackend.util.RedisUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoimpl implements OrderDao {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ObjectMapper objectMapper;

    private void addToRedis(Book b) throws JsonProcessingException {
        int id = b.getBookId();
        System.out.println("book: "+id+" is not in Redis, add it into Redis now!");
        Map map = objectMapper.readValue(objectMapper.writeValueAsString(b),Map.class);
        redisUtil.hmset("book:"+id,map);
        //将主键保存为一个list，遍历时方便查询
        redisUtil.lSet("book:id",id);
    }

    @Override
    public Order checkCart(User owner){
        List<Order> temp = orderRepository.findOrderByOwnerAndType(owner,0);
        if(temp.isEmpty())return null;
        else return temp.get(0);
    }

    public int checkNewOrder(User owner){
        List<Order> temp = orderRepository.findOrderByOwnerAndType(owner,1);
        int size = temp.size();
        int maxId = 0;
        for(int i=0;i<size;i++){
            Order tempOrder = temp.get(i);
            int tempId = tempOrder.getOrderId();
            if(maxId<tempId)
                maxId = tempId;
        }
        return maxId;
    }

    @Override
    public void setOrderPay(int orderId,int bookId,int number)
    {
        Order toBeSet = orderRepository.getById(orderId);
        Book temp = bookRepository.getById(bookId);
        int cost = temp.getPrice()*number;
        toBeSet.setCost(cost);
    }

    @Override
    @Transactional
    public int addOrder(int userId,int type){
        User owner = userRepository.getById(userId);
        if(type==0){
            //check the already existed cart
            Order cart = checkCart(owner);
            if(cart==null)
                System.out.println("not existed and add now");
            else return cart.getOrderId();
        }
        Order temp = new Order();
        temp.setType(type);
        temp.setOwner(owner);
        temp.setTime(new Date());
        orderRepository.saveAndFlush(temp);
//        int result = 10/0;
        if(type==0){
            //just new cart need this orderId
            Order newCart = checkCart(owner);
            System.out.println("newcart id "+newCart.getOrderId());
            return newCart.getOrderId();
        }
        else {
            //return new order's id
            int id = checkNewOrder(owner);
            System.out.println("get neworder id " + id);
            return id;
        }

    }

    @Override
    public Order getMyCart(int userId){
        User user = userRepository.getById(userId);
        List<Order> orders = user.getOrderCarts();
        for(int i=0;i<orders.size();i++) {
            if (orders.get(i).getType() == 0) {
                return orders.get(i);
            }
        }
        return null;
    }

    @Override
    public int cartToOrder(int userId){
        int pay = 0;
        User user = userRepository.getById(userId);
        List<Order> orders = user.getOrderCarts();
        Order cart = null;
        for(int i=0;i<orders.size();i++){
            cart = orders.get(i);
            if(cart.getType()==0){
                List<OrderItem> items = cart.getItems();
                for(int j=0;j<items.size();j++){
                    int bookid = items.get(j).getBook().getBookId();
                    int number = items.get(j).getNumber();
                    //decrease the book surplus
                    Book temp = bookRepository.getById(bookid);
                    int s = temp.getSurplus()-number;
                    temp.setSurplus(s);
                    pay += temp.getPrice()*number;
                    bookRepository.saveAndFlush(temp);
                }
                Date date = new Date();
                orders.get(i).setType(1);
                orders.get(i).setTime(date);
                orders.get(i).setCost(pay);
                user.setOrderCarts(orders);
                userRepository.saveAndFlush(user);
                break;
            }
        }
        return pay;
    }

    @Override
    public List<Book> getCart(int userId){
        User user = userRepository.getById(userId);
        List<Order> orders = user.getOrderCarts();
        List<OrderItem> cart = new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getType()==0){
                cart = orders.get(i).getItems();
                break;
            }
        }
        List<Book> bookList = new ArrayList<>();
        int size = cart.size();
        for(int i=0;i<size;i++){
            bookList.add(cart.get(i).getBook());
        }
        return bookList;
    }

//    @Override
//    public int getCartByUser(int userId){
//        User owner = userRepository.getById(userId);
//        List<Order> temp = orderRepository.findOrderByOwnerAndType(owner,0);
//        return temp.get(0).getOrderId();
//    }

    @Override
    public List<Order> getOrder(int userId){
        List<Order> orders = userRepository.getById(userId).getOrderCarts();
        List<Order> allOrders = new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getType()==1){
                allOrders.add(orders.get(i));
            }
        }
        return allOrders;
    }

    @Override
    public List<Order> getAllOrder(){
        return orderRepository.findOrderByType(1);
    }

    @Override
    public List<Order> getOrdersByTime(Date start,Date end){
        return  orderRepository.findOrderByTimeBetweenAndType(start, end,1);
    }

    @Override
    public List<Order> getOrdersByTimeAndUser(Date start,Date end,User owner){
        return orderRepository.findOrderByTimeBetweenAndTypeAndOwner(start, end, 1,owner);
    }
}
