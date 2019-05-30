package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.Summary;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SummaryMapper {
    void addNewSummary(Summary summary);
    List<Summary> selectSummarybyID(int userID,String startTime,String endTime);
    Summary selectTodaySummarybyID(Integer userID);
    List<Summary> selectTodaySummaryList();
    List<Summary> selectYesterdaySummaryList();
    void updateSummary(Summary summary);
    List<Summary> summaryList();
    void onlyKeepCompanySummary();
    List<Summary> selectSummaryAll();
}
