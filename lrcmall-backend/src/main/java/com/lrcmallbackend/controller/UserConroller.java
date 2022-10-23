package com.lrcmallbackend.controller;

import com.lrcmallbackend.entity.User;
import com.lrcmallbackend.service.TimeService;
import com.lrcmallbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Scope("singleton")
public class UserConroller {
    @Autowired
    UserService userService;
    @Autowired
    TimeService timeService;

    @RequestMapping("/checkAccount")
    @ResponseBody
    public User checkAccount(@RequestBody User user){
        System.out.println("checkAccount!--"+user.getName());
        System.out.println(timeService+"————"+this);
        timeService.startTimer();
        return userService.checkAccount(user.getName(),user.getPassword());
    }

    @RequestMapping("/addAccount")
    @ResponseBody
    public boolean addAccount(@RequestBody User user){
        System.out.println("addAccount!--"+user.getName());
        return userService.addAccount(user.getName(),user.getPassword(),user.getEmail());
    }

    @RequestMapping("/getUsers")
    @ResponseBody
    public List<User> getUsers(){
        System.out.println("getAllUsers!");
        return userService.getUsers();
    }

    @RequestMapping("/changeUserType")
    @ResponseBody
    public boolean changeUserType(@RequestBody Map<String,Integer> userid){
        System.out.println("changeUser----"+userid.get("data"));
        return userService.changeType(userid.get("data"));
    }

    @RequestMapping("/Logout")
    public String Logout(@RequestParam("userid")int userId){
        System.out.println("user————"+userId+"Logout now!");
        System.out.println(timeService+"————"+this);
        return timeService.endTimer();
    }
}
