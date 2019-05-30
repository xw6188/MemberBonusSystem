package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public User selectbyUserId(int id){
        return userMapper.selectbyUserId(id);
    }

    public User selectbyPosition(int position){
        return userMapper.selectbyPosition(position);
    }

    public void updateUserName(int id, String name){
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        userMapper.updateUserName(user);
    }

    public void deletebyUserId(int id){
        userMapper.deletebyUserId(id);
    }

    public void addNewUser(User user){
        userMapper.addNewUser(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public List<User> userListBothLeg(){
        return userMapper.userListBothLeg();
    }

    public List<User> userListNonLeaf() { return userMapper.userListNonLeaf(); }

    public List<User> userList() { return userMapper.userList(); }

    public void resetLastteamamt(){
        userMapper.resetLastteamamt();
    }
}

