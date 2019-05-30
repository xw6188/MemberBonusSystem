package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Bonus;
import com.ben.sgzspringbootapi.entity.PageResult;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BonusService {
    public Bonus selectBonus(Integer bonusID);
    public void addNewBonus(Bonus bonus);
    public void deleteBonus(Integer bonusID);
    public List<Bonus> bonusListByDate(Date date);
    public List<Bonus> selectBonusByUserID(int userID);
    public List<Bonus> selectBonusByUserIDSearch(int userID,String startTime,String endTime);
    public List<Bonus> selectBonusByUserIDfund(Integer userID);
}
