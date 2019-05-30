package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.UserOrderDetail;
import com.ben.sgzspringbootapi.mapper.UserOrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderDetailServiceImpl implements UserOrderDetailService{

    @Autowired
    private UserOrderDetailMapper userOrderDetailMapper;

    public List<UserOrderDetail> orderDetailListByUserID(Integer userID){
        return userOrderDetailMapper.orderDetailListByUserID(userID);
    }
}
