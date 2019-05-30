package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.entity.UserOrder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserOrderMapper {
    UserOrder selectOrder(String orderId);
    void addNewUserOrder(UserOrder userOrder);
    void deleteOrder(String orderId);
    List<UserOrder> orderListByDate(Date date);
    List<UserOrder> orderListByUserID(Integer userID);

}
