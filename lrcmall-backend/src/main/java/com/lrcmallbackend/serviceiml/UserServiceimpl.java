package com.lrcmallbackend.serviceiml;

import com.lrcmallbackend.dao.UserDao;
import com.lrcmallbackend.entity.User;
import com.lrcmallbackend.repository.UserRepository;
import com.lrcmallbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User checkAccount(String name, String password){
        return userDao.checkAccount(name,password);
    }

    @Override
    public boolean addAccount(String username,String password,String email){
        return userDao.addAccount(username,password,email);
    }

    @Override
    public List<User> getUsers(){
        return userDao.getUsers();
    }

    @Override
    public boolean changeType(int userId){
        return userDao.changeType(userId);
    }
}
