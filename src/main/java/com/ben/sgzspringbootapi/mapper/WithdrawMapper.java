package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.Withdraw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawMapper {
    void requestWithdraw(Withdraw withdraw);
    List<Withdraw> withdrawListByUserID(Integer UserID);
    List<Withdraw> withdrawListAll();
    void updateWithdraw(Withdraw withdraw);
    Withdraw selectWithdrawByID(Integer WithdrawID);
}
