package com.ben.sgzspringbootapi.mapper;

import com.ben.sgzspringbootapi.entity.Level;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelMapper {

    Level selectLevel(Integer levelID);
    void addNewLevel(Level level);
    void updateLevel(Level level);
    void deleteLevel(Integer levelID);
    List<Level> LevelList();
}
