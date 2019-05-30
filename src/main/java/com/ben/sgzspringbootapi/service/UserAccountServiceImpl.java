package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.PageResult;
import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.entity.UserAccount;
import com.ben.sgzspringbootapi.mapper.UserAccountMapper;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService{

    @Autowired
    UserAccountMapper userAccountMapper;

    public PageResult userAccountList(int page, int pageSize){
        PageInfo<UserAccount> pageInfo = PageHelper.startPage(page, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                userAccountMapper.userAccountList();
            }
        });
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public List<UserAccount> userAccountListSearch(int userId, String mobile, String nickName, int level,int page,int pageSize) {
       return userAccountMapper.userAccountListSearch(userId, mobile, nickName, level);
    }

   /* public List<UserAccount> userAccountListSearch(User user){
        return userAccountMapper.userAccountListSearch(user);
    }*/
}
