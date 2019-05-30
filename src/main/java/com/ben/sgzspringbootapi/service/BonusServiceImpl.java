package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.PageResult;
import com.ben.sgzspringbootapi.mapper.BonusMapper;
import com.ben.sgzspringbootapi.entity.Bonus;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BonusServiceImpl implements BonusService {

    @Autowired
    BonusMapper bonusMapper;

    public Bonus selectBonus(Integer bonusID) {
        return bonusMapper.selectBonus(bonusID);
    }

    public void addNewBonus(Bonus bonus) {
        bonusMapper.addNewBonus(bonus);
    }

    public void deleteBonus(Integer bonusId) {
        bonusMapper.deleteBonus(bonusId);
    }

    public List<Bonus> bonusListByDate(Date date) {
        return bonusMapper.bonusListByDate(date);
    }

    @Override
    public List<Bonus> selectBonusByUserID(int userID) {
      return   bonusMapper.selectBonusByUserID(userID);
    }

    @Override
    public List<Bonus> selectBonusByUserIDSearch(int userID, String startTime, String endTime) {
      return bonusMapper.selectBonusByUserIDSearch(userID, startTime, endTime);
    }


    public List<Bonus> selectBonusByUserIDfund(Integer userID) {
       return bonusMapper.selectBonusByUserIDfund(userID);
    }
}
