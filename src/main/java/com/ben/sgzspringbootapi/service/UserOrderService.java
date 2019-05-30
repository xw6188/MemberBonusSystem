package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.UserOrder;
import com.ben.sgzspringbootapi.mapper.UserOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface UserOrderService {
    public void addNewUserOrder(UserOrder userOrder);
    public void deleteOrder(String orderId);
    public UserOrder selectOrder(String orderId);
    public List<UserOrder> orderListByDate(Date date);
    public List<UserOrder> orderListByUserID(Integer userID);
}
