package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.VideoGameRecord;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;

public class VideoGameConverter {
    public static VideoGameRecord fromRequestToRecord(VideoGameRequest request){
        VideoGameRecord record = new VideoGameRecord();
        record.setName(request.getName());
        record.setConsoles(request.getConsoles());
        record.setDescription(request.getDescription());
        record.setUpwardVote(request.getUpwardVote());
        record.setDownwardVote(request.getDownwardVote());
        record.setVotingPercentage(request.getVotingPercentage());
        return record;
    }

    public static VideoGameResponse fromRecordToResponse(VideoGameRecord record){
        VideoGameResponse response = new VideoGameResponse();
        response.setConsoles(record.getConsoles());
        response.setDescription(record.getDescription());
        response.setName(response.getName());
        response.setTotalVote(record.getVotingPercentage());
        response.setUpwardVote(record.getUpwardVote());
        response.setDownwardVote(record.getDownwardVote());
        return response;
    }
}
