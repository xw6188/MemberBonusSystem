package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Withdraw;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WithdrawService {
    void requestWithdraw(Withdraw withdraw);
    List<Withdraw> withdrawListByUserID(Integer userID);
    void updateWithdraw(Withdraw withdraw);
    Withdraw selectWithdrawByID(Integer WithdrawID);
    List<Withdraw> withdrawListAll(int page, int pageSize);
}
