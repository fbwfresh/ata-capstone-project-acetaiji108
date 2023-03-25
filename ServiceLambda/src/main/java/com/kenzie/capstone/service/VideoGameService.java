package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.VideoGameConverter;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.VideoGameRecord;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class VideoGameService {
    private VideoGameDao videoGameDao;
    @Inject
    public VideoGameService(VideoGameDao videoGameDao){
        this.videoGameDao = videoGameDao;
    }
    public List<VideoGameResponse> getAllVideoGames(){
        List<VideoGameResponse> videoGameResponsesList = new ArrayList<>();
       List<VideoGameRecord> videoGameRecords = videoGameDao.getAllGames();
       for(VideoGameRecord record : videoGameRecords){
       videoGameResponsesList.add(VideoGameConverter.fromRecordToResponse(record));
       }
       return videoGameResponsesList;
    }

    public VideoGameResponse getVideoGame(String name){
        VideoGameRecord record = videoGameDao.findByName(name);
        if(record == null){
            throw new InvalidGameException("Request must contain a valid video game name");
        }
        return VideoGameConverter.fromRecordToResponse(record);
    }

    public VideoGameResponse addVideoGame(VideoGameRequest request){
        if (request == null) {
            throw new InvalidGameException("Request must contain a valid information");
        }
        VideoGameRecord record = VideoGameConverter.fromRequestToRecord(request);
        videoGameDao.addVideoGame(record);
        return VideoGameConverter.fromRecordToResponse(record);
    }

    public VideoGameResponse updateVideoGame(VideoGameRequest request) {
        if (request == null || request.getName() == null) {
            throw new InvalidGameException("Request must contain a valid game ID and information to update");
        }

        VideoGameRecord existingRecord = videoGameDao.findByName(request.getName());
        if (existingRecord == null) {
            throw new InvalidGameException("Game with name " + request.getName() + " not found");
        }

        // Update fields in the existing record based on the fields in the request
        if (request.getName() != null) {
            existingRecord.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingRecord.setDescription(request.getDescription());
        }
        if (request.getConsoles() != null) {
            existingRecord.setConsoles(request.getConsoles());
        }
        if (request.getImage() != null) {
            existingRecord.setImage(request.getImage());
        }

        existingRecord.setUpwardVote(request.getUpwardVote());
        existingRecord.setDownwardVote(request.getDownwardVote());
        existingRecord.setTotalVote(request.getTotalVote());


        // Save the updated record to the database
        videoGameDao.updateVideoGame(existingRecord);

        return VideoGameConverter.fromRecordToResponse(existingRecord);
    }



//    public VideoGameResponse addUpvote(VideoGameRequest request){
//     //  VideoGameRecord record = videoGameDao.findByName(name);
//        if (request == null) {
//            throw new InvalidGameException("Request must contain a valid information");
//        }
//        VideoGameRecord record = VideoGameConverter.fromRequestToRecord(request);
//        videoGameDao.addVideoGame(record);
//        return VideoGameConverter.fromRecordToResponse(record);
//    }

    public boolean deleteVideoGame(String videoGameName){
        VideoGameRecord record = videoGameDao.findByName(videoGameName);
            if(record == null){
                throw new InvalidGameException("Request must contain a valid video game name");
         }
            boolean deleted = videoGameDao.deleteVideoGame(record);

                if(!deleted){
                   return false;
              }
        return deleted;
    }

}
