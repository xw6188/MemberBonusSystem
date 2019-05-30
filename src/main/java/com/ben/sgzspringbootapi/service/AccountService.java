package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Account;
import com.ben.sgzspringbootapi.entity.Result;
import com.ben.sgzspringbootapi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public interface AccountService {
    public void addNewAccount(Account account);
    public void deleteAccount(Integer accountID);
    public Account selectAccountByUserID(Integer userID);
    public List<Account> accountList();
    public void updateAccount(Account account);
}
