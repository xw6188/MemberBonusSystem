package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Level;
import com.ben.sgzspringbootapi.mapper.LevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelServiceImpl implements LevelService{

    @Autowired
    LevelMapper levelMapper;

    public Level selectLevel(Integer levelID){
        return levelMapper.selectLevel(levelID);
    }

    public void addNewLevel(Level level){
        levelMapper.addNewLevel(level);
    }

    public void updateLevel(Level level){
        levelMapper.updateLevel(level);
    }

    public void deleteLevel(Integer levelID){
        levelMapper.deleteLevel(levelID);
    }

    public List<Level> LevelList(){
        return levelMapper.LevelList();
    }
}
