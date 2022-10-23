package com.lrcmallbackend.controller;

import com.lrcmallbackend.dto.ReturnBookSale;
import com.lrcmallbackend.dto.ReturnCustomer;
import com.lrcmallbackend.dto.ReturnUserBook;
import com.lrcmallbackend.entity.Order;
import com.lrcmallbackend.dto.ReturnBook;
import com.lrcmallbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/addToCart")
    public boolean addCartItem(@RequestBody Map<String,Integer> user){
        System.out.println("addToCart--"+user.get("number")+"book"+user.get("bookid"));
        return orderService.addToCart(user.get("bookid"),user.get("number"),user.get("userid"));
    }

//    using http
//    @RequestMapping("/addToOrder")
//    public boolean addOrderItem(@RequestBody Map<String,Integer> user){
//        System.out.println("addToOrder--"+user.get("number")+"book"+user.get("bookid"));
//        return orderService.addToOrder(user.get("bookid"),user.get("number"),user.get("userid"));
//    }

    //use kafka message
    @RequestMapping("/addToOrder")
    public void addOrderItem(@RequestBody Map<String,Integer> user){
        System.out.println("addToOrder--"+user.get("number")+"book"+user.get("bookid"));
        String data = user.get("bookid")+","+user.get("number")+","+user.get("userid");
        kafkaTemplate.send("orderRequest",  "key", data);
    }

    @RequestMapping("/cartToOrder")
    public int cartToOrder(@RequestBody Map<String,Integer> user){
        System.out.println("CartToOrder!");
        return orderService.cartToOrder(user.get("userid"));
    }

    @RequestMapping("/getCart")
    public List<ReturnBook> getCart(@RequestParam("userid")int userId){
        System.out.println("getCart--"+userId);
        return orderService.getCart(userId);
    }

    @RequestMapping("/getOrder")
    public List<Order> getOrder(@RequestParam("userid")int userId){
        System.out.println("getOrder--"+userId);
        return orderService.getOrder(userId);
    }

    @RequestMapping("/getAllOrders")
    public List<Order> getOrder(){
        System.out.println("getAllOrders");
        return orderService.getAllOrder();
    }

    @RequestMapping("/getAllSales")
    public List<ReturnBookSale> getAllSale(@RequestParam("start") String start_s,@RequestParam("end") String end_s){
        Date start = java.sql.Date.valueOf(start_s);
        Date end = java.sql.Date.valueOf(end_s);
        System.out.println("getAllSales from"+start_s+"--"+end_s);
        return orderService.getAllSales(start,end);
    }

    @RequestMapping("/getAllConsumption")
    public List<ReturnCustomer> getAllConsumption(@RequestParam("start") String start_s, @RequestParam("end") String end_s){
        Date start = java.sql.Date.valueOf(start_s);
        Date end = java.sql.Date.valueOf(end_s);
        System.out.println("getAllConsumption from"+start_s+"--"+end_s);
        return orderService.getAllConsumptions(start,end);
    }

    @RequestMapping("/getOneSale")
    public List<ReturnUserBook> getOneSale(@RequestParam("start") String start_s, @RequestParam("end") String end_s,@RequestParam("userid") int userId){
        Date start = java.sql.Date.valueOf(start_s);
        Date end = java.sql.Date.valueOf(end_s);
        System.out.println("getOneSale from"+start_s+"--"+end_s+"--"+userId);
        return orderService.getOneSale(start,end,userId);
    }

    @RequestMapping("/changeCartNumber")
    public boolean changeCartNumber(@RequestBody Map<String,Integer> bookInfo){
        System.out.println("changeBookNumber"+"--"+bookInfo.get("userId")+"--"+bookInfo.get("number"));
        return orderService.changeItemNumber(bookInfo.get("userId"),bookInfo.get("bookId"),bookInfo.get("number"));
    }
}
