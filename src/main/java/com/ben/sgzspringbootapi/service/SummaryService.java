package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.PageResult;
import com.ben.sgzspringbootapi.entity.Summary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface SummaryService {
    public void addNewSummary(Summary summary);
    public PageResult selectSummarybyID(int userID,int page,int pageSize,String startTime,String endTime);
    public Summary selectTodaySummarybyID(Integer userID);
    public void updateSummary(Summary summary);
    public List<Summary> selectTodaySummaryList(int page, int pageSize);
    public List<Summary> selectYesterdaySummaryList(int page, int pageSize);
    public List<Summary> summaryList();
    public void onlyKeepCompanySummary();
    public PageResult summaryListAll(int page,int pageSize);
}
