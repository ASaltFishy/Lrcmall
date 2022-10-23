package com.lrcmallbackend.serviceiml;

import com.lrcmallbackend.dao.BookDao;
import com.lrcmallbackend.dao.OrderDao;
import com.lrcmallbackend.dao.OrderItemDao;
import com.lrcmallbackend.dao.UserDao;
import com.lrcmallbackend.dto.ReturnBook;
import com.lrcmallbackend.dto.ReturnBookSale;
import com.lrcmallbackend.dto.ReturnCustomer;
import com.lrcmallbackend.dto.ReturnUserBook;
import com.lrcmallbackend.entity.*;
import com.lrcmallbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderItemDao orderItemDao;

    @Override
    public boolean addToCart(int bookId,int number,int userId){
        int cartId = orderDao.addOrder(userId,0);
        return orderItemDao.addItem(cartId,bookId,number);
    }

    @Override
    @Transactional
    public String addToOrder(int bookId,int number,int userId){
        int orderId = orderDao.addOrder(userId,1);
        orderDao.setOrderPay(orderId,bookId,number);
//        int result = 10/0;
        boolean ret = false;
        try{
            ret = orderItemDao.addItem(orderId,bookId,number);

        }catch (Exception e){
            e.printStackTrace();
        }
        Book book = bookDao.getBookDetail(bookId);
//        int result = 10/0;
        return book.getName()+"*"+number;
    }

    @Override
    public int addItemList(List<Map> orderlist,int orderId){
        return bookDao.addItemList(orderlist,orderId);
    }

    @Override
    public List<ReturnBook> getCart(int userId){
        Order order = orderDao.getMyCart(userId);
        if(order==null)return null;
        List<OrderItem> items = order.getItems();
        int itemNumber = items.size();
        List<ReturnBook> ret = new ArrayList<>();
        for(int i=0;i<itemNumber;i++){
            OrderItem item = items.get(i);
            Book toBeDup = item.getBook();
            ReturnBook returnItem = new ReturnBook(toBeDup.getBookId(), toBeDup.getName(),toBeDup.getAuthor(),toBeDup.getImage(),toBeDup.getPrice(),toBeDup.getIsbn(),toBeDup.getSurplus(),item.getNumber());
            ret.add(returnItem);
        }
        return ret;
    }

    @Override
    public List<Order> getOrder(int userId){
        return orderDao.getOrder(userId);
    }

    @Override
    public int cartToOrder(int userId){
        return orderDao.cartToOrder(userId);
    }

    @Override
    public List<Order> getAllOrder(){
        return orderDao.getAllOrder();
    }

    @Override
    public List<ReturnBookSale> getAllSales(Date start, Date end){
        List<Order> orders = orderDao.getOrdersByTime(start,end);
        List<Book> allBook = bookDao.getAllBooks();
        List<ReturnBookSale> ret = new ArrayList<>();
        int orderSize = orders.size();
        int bookSize = bookDao.getMaxBookId()+1;
        int[] bookSale = new int[bookSize];
        for(int i=0;i<orderSize;i++){
            Order temp = orders.get(i);
            List<OrderItem> items = temp.getItems();
            // refresh book sale number
            for(int j=0;j< items.size();j++)
            {
                OrderItem tempItem = items.get(j);
                int number = tempItem.getNumber();
                int index = tempItem.getBook().getBookId();
                bookSale[index]+=number;
            }
        }
        for(int i=0;i< allBook.size();i++){
            Book recentBook = allBook.get(i);
            int index = recentBook.getBookId();
            ReturnBookSale retItem = new ReturnBookSale(recentBook.getImage(),recentBook.getName(), recentBook.getAuthor(), bookSale[index]);
            ret.add(retItem);
        }
        return ret;
    }

    @Override
    public List<ReturnCustomer> getAllConsumptions(Date start, Date end){
        List<Order> orders = orderDao.getOrdersByTime(start,end);
        List<User> allUsers = userDao.getUsers();
        List<ReturnCustomer> ret = new ArrayList<>();
        int userSize = userDao.getMaxUserId()+1;
        int[] userConsumption = new int[userSize];
        for(int i=0;i<orders.size();i++){
            Order temp =  orders.get(i);
            userConsumption[temp.getOwner().getUserId()] += temp.getCost();
        }
        for(int i=0;i<allUsers.size();i++){
            User recentUser = allUsers.get(i);
            ReturnCustomer retItem = new ReturnCustomer();
            retItem.setMoney(userConsumption[recentUser.getUserId()]);
            retItem.setName(recentUser.getName());
            ret.add(retItem);
        }
        return ret;
    }

    @Override
    public List<ReturnUserBook> getOneSale(Date start, Date end, int userId){
        User owner = userDao.getOneUser(userId);
        List<Order> orders = orderDao.getOrdersByTimeAndUser(start,end,owner);
        List<ReturnUserBook> ret = new ArrayList<>();
        List<Book> allBook = bookDao.getAllBooks();
        int bookSize = bookDao.getMaxBookId()+1;
        int[] bookSale = new int[bookSize];
        int orderSize = orders.size();
        for (int i=0;i<orderSize;i++){
            Order order = orders.get(i);
            List<OrderItem> items = order.getItems();
            for(int j=0;j<items.size();j++){
                OrderItem tempItem = items.get(j);
                int number = tempItem.getNumber();
                int index = tempItem.getBook().getBookId();
                bookSale[index]+=number;
            }
        }
        for(int i=0;i< allBook.size();i++){
            Book recentBook = allBook.get(i);
            int index = recentBook.getBookId();
            if(bookSale[index]==0)continue;
            ReturnUserBook retItem = new ReturnUserBook(recentBook.getName(), bookSale[index],recentBook.getPrice());
            ret.add(retItem);
        }
        return ret;
    }

    @Override
    public boolean changeItemNumber(int userId,int bookId,int newNumber){
        Order cart = orderDao.getMyCart(userId);
        return orderItemDao.changeItemNumber(cart,bookId,newNumber);
    }

}
