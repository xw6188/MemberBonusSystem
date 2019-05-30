package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {
    void addNewAccount(Account account);
    void deleteAccount(Integer accountID);
    Account selectAccountByUserID(Integer userID);
    List<Account> accountList();
    void updateAccount(Account account);
}