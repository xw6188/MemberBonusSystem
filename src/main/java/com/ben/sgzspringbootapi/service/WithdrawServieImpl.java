package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Withdraw;
import com.ben.sgzspringbootapi.mapper.WithdrawMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WithdrawServieImpl implements WithdrawService {

    @Autowired
    WithdrawMapper withdrawMapper;

    public void requestWithdraw(Withdraw withdraw){
        withdrawMapper.requestWithdraw(withdraw);
    }

    public List<Withdraw> withdrawListByUserID(Integer userID){
        return withdrawMapper.withdrawListByUserID(userID);
    }

    public List<Withdraw> withdrawListAll(int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        return withdrawMapper.withdrawListAll();
    }

    public Withdraw selectWithdrawByID(Integer withdrawID){
        return withdrawMapper.selectWithdrawByID(withdrawID);
    }

    public void updateWithdraw(Withdraw withdraw) { withdrawMapper.updateWithdraw(withdraw);};
}
