package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Result;
import com.ben.sgzspringbootapi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@Service
public class SgzUtil {

        @Autowired
        private UserService userService;

        public Date CurrentDate(){
            Date date = new Date();
            /*
            Calendar Cal=Calendar.getInstance();
            Cal.setTime(date);
            Cal.add(HOUR_OF_DAY,14);
            date = Cal.getTime();
            System.out.println(Cal);
            System.out.println(date);
            */
            return date;
        }

        public ArrayList<User> getGroupList(Integer userID)
        {
            User user = userService.selectbyUserId(userID);

            ArrayList<User> userList = new ArrayList<User>();
            getAllMembers(userList, user);
            return userList;
        }

        private void getAllMembers(ArrayList userList, User user) {
            userList.add(user);
            if (user.getTreeLeft() != 0) {
                getAllMembers(userList, userService.selectbyUserId(user.getTreeLeft()));
            }
            if (user.getTreeRight() != 0) {
                getAllMembers(userList, userService.selectbyUserId(user.getTreeRight()));
            }

        }

}
