package com.ben.sgzspringbootapi.service;

import com.ben.sgzspringbootapi.entity.Level;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface LevelService {

    public Level selectLevel(Integer levelID);
    public void addNewLevel(Level level);
    public void updateLevel(Level level);
    public void deleteLevel(Integer levelID);
    public List<Level> LevelList();
}
