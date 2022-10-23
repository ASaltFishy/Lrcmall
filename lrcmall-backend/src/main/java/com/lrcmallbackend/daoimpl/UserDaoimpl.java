package com.lrcmallbackend.daoimpl;

import com.lrcmallbackend.dao.UserDao;
import com.lrcmallbackend.entity.User;
import com.lrcmallbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.websocket.OnError;
import java.util.List;

@Repository
public class UserDaoimpl implements UserDao {
    @Autowired
    UserRepository userRepository;

    @Override
    public User checkAccount(String username, String password){
        return userRepository.checkAccount(username,password);
    }

    @Override
    public boolean addAccount(String username,String password,String email){
        User newUser = new User();
        newUser.setName(username);
        newUser.setPassword(password);
        newUser.setType(1);
        newUser.setEmail(email);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public List<User> getUsers(){
        List<User>temp =  userRepository.findUserByType(1);
        temp.addAll(userRepository.findUserByType(2));
        return temp;
    }

    @Override
    public boolean changeType(int userId){
        User toBeChange = userRepository.getById(userId);
        if(toBeChange==null)return false;
        if(toBeChange.getType()==2)toBeChange.setType(1);
        else toBeChange.setType(2);
        userRepository.save(toBeChange);
        return true;
    }

    @Override
    public int getMaxUserId(){
        return userRepository.getMaxUserId();
    }

    @Override
    public User getOneUser(int userId){
        return userRepository.getById(userId);
    }
}
