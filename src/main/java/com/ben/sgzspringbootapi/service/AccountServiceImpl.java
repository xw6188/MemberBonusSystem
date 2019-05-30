package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Account;
import com.ben.sgzspringbootapi.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;
    public void addNewAccount(Account account){
        accountMapper.addNewAccount(account);
    }
    public void deleteAccount(Integer accountID){
        accountMapper.deleteAccount(accountID);
    }
    public Account selectAccountByUserID(Integer userID){
        return accountMapper.selectAccountByUserID(userID);
    }
    public List<Account> accountList(){
        return accountMapper.accountList();
    }
    public void updateAccount(Account account){
        accountMapper.updateAccount(account);
    }
}
