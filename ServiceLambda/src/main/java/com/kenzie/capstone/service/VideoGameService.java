package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.VideoGameConverter;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.VideoGameRecord;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class VideoGameService {
    private VideoGameDao videoGameDao;
    static final Logger log = LogManager.getLogger();
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
        log.info("before dao " + name);
        VideoGameRecord record = videoGameDao.findByName(name);
        log.info(record);
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
        log.info("starting the update method");
//        if (request == null || request.getName() == null) {
//            throw new InvalidGameException("Request must contain a valid game ID and information to update");
//        }
        log.info("after the InvalidGameException");

        VideoGameRecord existingRecord = videoGameDao.findByName(request.getName());
        log.info(existingRecord + " after the findbyName method");
        if (existingRecord == null) {
            throw new InvalidGameException("Game with name " + request.getName() + " not found");
        }
        log.info("After InvalidGameException");

        // Update fields in the existing record based on the fields in the request
        if (request.getName() != null) {
            existingRecord.setName(request.getName());
        }
        log.info(request.getName());
        if (request.getDescription() != null) {
            existingRecord.setDescription(request.getDescription());
        }
        log.info(request.getDescription());
        if (request.getConsoles() != null) {
            existingRecord.setConsoles(request.getConsoles());
        }
        log.info(request.getConsoles());
        if (request.getImage() != null) {
            existingRecord.setImage(request.getImage());
        }
        log.info(request.getImage());
        existingRecord.setName(request.getName());
        existingRecord.setDescription(request.getDescription());
        existingRecord.setConsoles(request.getConsoles());
        existingRecord.setImage(request.getImage());
        existingRecord.setUpwardVote(request.getUpwardVote());
        existingRecord.setDownwardVote(request.getDownwardVote());
        existingRecord.setTotalVote(request.getTotalVote());
        log.info("updated Record " + existingRecord);
        // Save the updated record to the database
       VideoGameRecord record = videoGameDao.updateVideoGame(existingRecord);
       log.info("called the dao method " + record);

        return VideoGameConverter.fromRecordToResponse(record);
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
