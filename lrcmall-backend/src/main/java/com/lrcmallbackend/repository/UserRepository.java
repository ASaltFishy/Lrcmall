package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "from User where name=:name and password=:password")
    User checkAccount(@Param("name") String username, @Param("password") String password);

    List<User> findUserByType(int type);

    @Query("select max(u.userId) from User u")
    int getMaxUserId();
}