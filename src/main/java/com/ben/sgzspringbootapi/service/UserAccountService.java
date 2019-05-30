package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.PageResult;
import com.ben.sgzspringbootapi.entity.UserAccount;
import org.springframework.stereotype.Service;

import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserAccountService {
        public PageResult userAccountList(int page,int pageSize);
        //public List<UserAccount> userAccountListSearch(User user);
        public List<UserAccount> userAccountListSearch(int userId,String mobile,String nickName,int level,int page,int pageSize);
}
