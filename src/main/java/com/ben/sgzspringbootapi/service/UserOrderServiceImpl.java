package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.UserOrder;
import com.ben.sgzspringbootapi.mapper.UserOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    UserOrderMapper userOrderMapper;

    public UserOrder selectOrder(String orderId){
        return userOrderMapper.selectOrder(orderId);
    }

    public void addNewUserOrder(UserOrder userOrder){
        userOrderMapper.addNewUserOrder(userOrder);
    }

    public void deleteOrder(String orderId){
        userOrderMapper.deleteOrder(orderId);
    }

    public List<UserOrder> orderListByDate(Date date){
        return userOrderMapper.orderListByDate(date);
    }

    public List<UserOrder> orderListByUserID(Integer userID) {return userOrderMapper.orderListByUserID(userID); }
}