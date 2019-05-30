package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User selectbyUserId(int id);
    public User selectbyPosition(int id);
    public void updateUserName(int id, String name);
    public void updateUser(User user);
    public void deletebyUserId(int id);
    public void addNewUser(User user);
    public List<User> userListBothLeg();
    public List<User> userListNonLeaf();
    public List<User> userList();
    public void resetLastteamamt();
}
