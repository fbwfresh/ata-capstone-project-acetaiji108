package com.kenzie.capstone.service.dao;
import com.kenzie.capstone.service.model.VideoGameRecord;


import java.util.List;

public interface VideoGameDao {
    public VideoGameRecord addVideoGame(VideoGameRecord record);
    public boolean deleteVideoGame(VideoGameRecord record);
    public VideoGameRecord findByName(String name);
    public List<VideoGameRecord> getAllGames();

}
