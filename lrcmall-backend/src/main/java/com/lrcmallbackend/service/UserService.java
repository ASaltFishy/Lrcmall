package com.lrcmallbackend.service;

import com.lrcmallbackend.entity.User;

import java.util.List;

public interface UserService {
    User checkAccount(String name,String password);
    boolean addAccount(String username,String password,String email);
    List<User> getUsers();
    boolean changeType(int userId);
}
