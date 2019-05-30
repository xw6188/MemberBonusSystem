package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User selectbyUserId(int id);

    User selectbyPosition(int position);

    void updateUserName(User user);

    void updateUser(User user);

    void deletebyUserId(int id);

    void addNewUser(User user);

    List<User> userListBothLeg();

    List<User> userListNonLeaf();

    List<User> userList();

    void resetLastteamamt();

}
