package com.lrcmallbackend.dao;

import com.lrcmallbackend.entity.User;

import java.util.List;

public interface UserDao {
    User checkAccount(String username,String password);
    boolean addAccount(String username,String password,String email);
    List<User> getUsers();
    boolean changeType(int userId);
    int getMaxUserId();
    User getOneUser(int userId);
}
