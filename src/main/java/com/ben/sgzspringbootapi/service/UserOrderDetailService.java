package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.UserOrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserOrderDetailService {
    public List<UserOrderDetail> orderDetailListByUserID(Integer userID);
}
