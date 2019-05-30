package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.UserOrderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderDetailMapper {
    List<UserOrderDetail> orderDetailListByUserID(Integer userID);
}
