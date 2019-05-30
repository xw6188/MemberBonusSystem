package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.PageResult;
import com.ben.sgzspringbootapi.entity.Summary;
import com.ben.sgzspringbootapi.mapper.SummaryMapper;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    SummaryMapper summaryMapper;

    public void addNewSummary(Summary summary){
        summaryMapper.addNewSummary(summary);
    }
    public PageResult selectSummarybyID(int userID,int page,int pageSize,String startTime,String endTime){
        PageInfo<Summary> pageInfo = PageHelper.startPage(page, pageSize).doSelectPageInfo(() -> {
            summaryMapper.selectSummarybyID(userID,startTime,endTime);
        });
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }
    public Summary selectTodaySummarybyID(Integer userID) {
        return summaryMapper.selectTodaySummarybyID(userID);
    }
    public void updateSummary(Summary summary){
        summaryMapper.updateSummary(summary);
    }
    public List<Summary> selectTodaySummaryList(int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        return summaryMapper.selectTodaySummaryList();
    }
    public List<Summary> selectYesterdaySummaryList(int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        return summaryMapper.selectYesterdaySummaryList();
    }
    public List<Summary> summaryList(){
        return summaryMapper.summaryList();
    }
    public void onlyKeepCompanySummary() {
        summaryMapper.onlyKeepCompanySummary();
    }

    @Override
    public PageResult summaryListAll(int page, int pageSize) {
        PageInfo<Summary> pageInfo = PageHelper.startPage(page, pageSize).doSelectPageInfo(() -> summaryMapper.selectSummaryAll());
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

}
