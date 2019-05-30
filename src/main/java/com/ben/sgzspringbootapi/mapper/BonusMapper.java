package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.Bonus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BonusMapper {
    Bonus selectBonus(Integer bonusID);
    void addNewBonus(Bonus bonus);
    void deleteBonus(Integer bonusID);
    List<Bonus> bonusListByDate(Date date);
    List<Bonus> selectBonusByUserID(int userID);
    List<Bonus> selectBonusByUserIDSearch(@Param("userID") int userID,@Param("startTime") String startTime,@Param("endTime") String endTime);
    List<Bonus> selectBonusByUserIDfund(Integer userID);
}
