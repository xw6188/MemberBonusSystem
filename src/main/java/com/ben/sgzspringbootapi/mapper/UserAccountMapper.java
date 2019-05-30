package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.User;
import com.ben.sgzspringbootapi.entity.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountMapper {
    List<UserAccount> userAccountList();
//    List<UserAccount> userAccountListSearch(User user);
    List<UserAccount> userAccountListSearch(int userId,String mobile,String nickName,int level);

}
