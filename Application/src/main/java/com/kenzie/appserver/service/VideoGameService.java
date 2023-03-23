package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CreateVideoGameRequest;
import com.kenzie.appserver.controller.model.UpdateRequest;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.VideoGameRepository;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.model.Consoles;
import com.kenzie.appserver.service.model.VideoGame;
import com.kenzie.capstone.service.client.VideoGameServiceClient;
import com.kenzie.capstone.service.model.VideoGameRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class VideoGameService {
    //TODO add the lambda functionality to the service methods whenever we get that part figured out and also add a leaderboard
    // functionality
    private VideoGameRepository videoGameRepository;
    private VideoGameServiceClient videoGameServiceClient;

    public VideoGameService(VideoGameRepository videoGameRepository, VideoGameServiceClient videoGameServiceClient) {
        this.videoGameRepository = videoGameRepository;

        this.videoGameServiceClient = videoGameServiceClient;

    }
//    public VideoGameService(VideoGameRepository videoGameRepository) {
//        this.videoGameRepository = videoGameRepository;
//    }

    public VideoGameRecord findByName(String name) {
        // Example getting data from the local repository
      //  VideoGameRecord dataFromDynamo = videoGameRepository
        //        .findById(name).orElse(null);

        //  .map(game -> new VideoGame(game.getName(), game.getDescription(),))

        com.kenzie.capstone.service.model.VideoGameResponse responseFromLambdaClient = videoGameServiceClient.getVideoGame(name);
        VideoGameRecord record = new VideoGameRecord();
        record.setName(responseFromLambdaClient.getName());
        record.setDescription(responseFromLambdaClient.getDescription());
        record.setConsoles(responseFromLambdaClient.getConsoles());
        record.setUpwardVote(responseFromLambdaClient.getUpwardVote());
        record.setDownwardVote(responseFromLambdaClient.getDownwardVote());
        record.setVotingPercentage(responseFromLambdaClient.getTotalVote());
        return record;
    }


//    public VideoGameResponse addNewVideoGame(String name, String description, Set<String> consoles) {
//        // Example sending data to the local repository
//
//       // VideoGameRecord videoGame = new VideoGameRecord();
//
//        VideoGameRecord videoGameRecord = new VideoGameRecord();
//        videoGameRecord.setName(name);
//        videoGameRecord.setDescription(description);
//        videoGameRecord.setConsoles(consoles);
//        videoGameRecord.setDownwardVote(0);
//        videoGameRecord.setUpwardVote(0);
//        videoGameRecord.setVotingPercentage(0);
//        videoGameRepository.save(videoGameRecord);
//        //TODO the code below is how to save the videogame utilizing lambda functionality
//        //Example sending data to the lambda
//        // VideoGameRecord dataFromLambda = lambdaServiceClient.setVideoGameData(name,description,consoles);
//        //videoGameRecord.setId(dataFromLambda.getId());
//        // videoGameRecord.setName(dataFromLambda.getData());
//
//       // videoGameRepository.save(exampleRecord);
//
//        //Example example = new Example(dataFromLambda.getId(), name);
//        return toVideoGameResponse(videoGameRecord);
//    }
    public VideoGameResponse addNewVideoGame(CreateVideoGameRequest game) {
        // Example sending data to the local repository

        // VideoGameRecord videoGame = new VideoGameRecord();
       // System.out.println("adding new game");
        VideoGameRecord videoGameRecord = new VideoGameRecord();
        videoGameRecord.setName(game.getVideoGameName());
        videoGameRecord.setDescription(game.getDescription());
        videoGameRecord.setConsoles(game.getConsoles());
        videoGameRecord.setDownwardVote(game.getDownwardVote());
        videoGameRecord.setUpwardVote(game.getUpwardVote());
        videoGameRecord.setVotingPercentage(game.getVotingPercentage());
       // System.out.println("before save");
        videoGameRepository.save(videoGameRecord);

        VideoGameRequest videoGameRequest = new VideoGameRequest();
        videoGameRequest.setConsoles(game.getConsoles());
        videoGameRequest.setDescription(game.getDescription());
        videoGameRequest.setName(game.getVideoGameName());
        videoGameRequest.setDownwardVote(game.getDownwardVote());
        videoGameRequest.setUpwardVote(game.getUpwardVote());
        videoGameRequest.setVotingPercentage(game.getVotingPercentage());
      com.kenzie.capstone.service.model.VideoGameResponse responseFromLambdaClient = videoGameServiceClient.addVideoGame(videoGameRequest);

        return toVideoGameResponse(videoGameRecord);
    }


    public List<VideoGameResponse> listAllVideoGames(){
//        List<VideoGameResponse> videoGameResponses = new ArrayList<>();
//        Iterator<VideoGameRecord> videoGameRecordList =  videoGameRepository.findAll().iterator();
//        while(videoGameRecordList.hasNext()){
//
//            videoGameResponses.add(toVideoGameResponse(videoGameRecordList.next()));
//        }
         List<com.kenzie.capstone.service.model.VideoGameResponse> videoGameResponsesList = videoGameServiceClient.getAllVideoGames();
         List<VideoGameResponse> videoGameResponseList =  videoGameResponsesList.stream().map(response -> {
           VideoGameResponse videoGameResponse = new VideoGameResponse();
           videoGameResponse.setUpwardVote(response.getUpwardVote());
           videoGameResponse.setName(response.getName());
           videoGameResponse.setConsoles(response.getConsoles());
           videoGameResponse.setDescription(response.getDescription());
           videoGameResponse.setDownwardVote(response.getDownwardVote());
           videoGameResponse.setTotalVote(response.getTotalVote());
           return videoGameResponse;
       }).collect(Collectors.toList());
          return videoGameResponseList;
    }


    public VideoGameResponse updateVideoGameConsoles(String name,Consoles... consoles) {
        Optional<VideoGameRecord> gameExists = videoGameRepository.findById(name);
        if (!gameExists.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game Not Found");
        }
        VideoGame game = new VideoGame(name,gameExists.get().getDescription(),consoles);
        gameExists.get().setConsoles(game.getConsoles());
        videoGameRepository.save(gameExists.get());
        return toVideoGameResponse(gameExists.get());
    }
//    public VideoGameResponse updateVideoGameConsoles(String name,List<String> consoles) {
//        Optional<VideoGameRecord> gameExists = videoGameRepository.findById(name);
//        if (!gameExists.isPresent()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game Not Found");
//        }
//        VideoGame game = new VideoGame(name,gameExists.get().getDescription(),consoles);
//        gameExists.get().setConsoles(game.getConsoles());
//        videoGameRepository.save(gameExists.get());
//        return toVideoGameResponse(gameExists.get());
//    }
    public VideoGameResponse updateVideoGame(UpdateRequest videoGameUpdateRequest) {
        Optional<VideoGameRecord> gameRecord = videoGameRepository.findById(videoGameUpdateRequest.getVideoGameName());
        if (gameRecord.isPresent()) {
            VideoGameRecord videoGameRecord = gameRecord.get();
            videoGameRecord.setName(videoGameUpdateRequest.getVideoGameName());
            videoGameRecord.setDescription(videoGameUpdateRequest.getDescription());
            videoGameRecord.setConsoles(videoGameUpdateRequest.getConsoles());
            videoGameRecord.setDownwardVote(videoGameUpdateRequest.getDownwardVote());
            videoGameRecord.setUpwardVote(videoGameUpdateRequest.getUpwardVote());
            videoGameRecord.setVotingPercentage(videoGameUpdateRequest.getVotingPercentage());
            videoGameRepository.save(videoGameRecord);

            VideoGameRequest videoGameRequest = new VideoGameRequest();
            videoGameRequest.setName(videoGameUpdateRequest.getVideoGameName());
            videoGameRequest.setConsoles(videoGameUpdateRequest.getConsoles());
            videoGameRequest.setDescription(videoGameUpdateRequest.getDescription());
            videoGameRequest.setUpwardVote(videoGameUpdateRequest.getUpwardVote());
            videoGameRequest.setVotingPercentage(videoGameUpdateRequest.getVotingPercentage());
            videoGameRequest.setDownwardVote(videoGameUpdateRequest.getDownwardVote());
            videoGameServiceClient.addVideoGame(videoGameRequest);
            return toVideoGameResponse(videoGameRecord);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find the requested game.");
    }
//    public VideoGameResponse updateVideoGameUpvote(UpdateRequest videoGameUpdateRequest) {
//        Optional<VideoGameRecord> gameRecord = videoGameRepository.findById(videoGameUpdateRequest.getVideoGameName());
//        if (gameRecord.isPresent()) {
//            VideoGameRecord videoGameRecord = gameRecord.get();
//            videoGameRecord.setName(videoGameUpdateRequest.getVideoGameName());
//            videoGameRecord.setDescription(videoGameUpdateRequest.getDescription());
//            videoGameRecord.setConsoles(videoGameUpdateRequest.getConsoles());
//            videoGameRecord.setDownwardVote(videoGameUpdateRequest.getDownwardVote());
//            videoGameRecord.setUpwardVote(videoGameUpdateRequest.getUpwardVote());
//            videoGameRecord.setVotingPercentage(videoGameUpdateRequest.getVotingPercentage());
//            videoGameRepository.save(videoGameRecord);
//            return toVideoGameResponse(videoGameRecord);
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find the requested game.");
//    }
//        Optional<VideoGameRecord> gameExists = videoGameRepository.findById(name);
//        if (!gameExists.isPresent()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game Not Found");
//        }
//        VideoGame game = new VideoGame(name,gameExists.get().getDescription(),consoles);
//        gameExists.get().setConsoles(game.getConsoles());
//        videoGameRepository.save(gameExists.get());
//        return toVideoGameResponse(gameExists.get());



    public VideoGameResponse updateVideoGameDescription(String name,String description) {
        Optional<VideoGameRecord> gameExists = videoGameRepository.findById(name);
        if (!gameExists.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game Not Found");
        }
        gameExists.get().setDescription(description);
        videoGameRepository.save(gameExists.get());
        return toVideoGameResponse(gameExists.get());
    }
    public VideoGameResponse updateVideoGameDescription(UpdateRequest videoGameUpdateRequest) {
        Optional<VideoGameRecord> gameExists = videoGameRepository.findById(videoGameUpdateRequest.getVideoGameName());
        if (!gameExists.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game Not Found");
        }
        gameExists.get().setDescription(videoGameUpdateRequest.getDescription());
        videoGameRepository.save(gameExists.get());
        return toVideoGameResponse(gameExists.get());
    }

    public boolean deleteVideoGame(String name) {
        if (videoGameRepository.findById(name).isPresent()) {
            videoGameRepository.delete((videoGameRepository.findById(name).get()));
            //todo add delete in the client
        }
        boolean deleted = videoGameServiceClient.deleteVideoGame(name);
//        if (deleted) {
//            return "Deleted Game";
//        }
        return deleted;
    }

    public List<VideoGameResponse> top5RatingLeaderboard() {
        List<VideoGameResponse> videoGameResponsesList = listAllVideoGames();
        List<VideoGameResponse> top5 =  videoGameResponsesList.stream()
                .sorted(Comparator.comparing(VideoGameResponse::getUpwardVote).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return top5;
    }

    public VideoGameResponse gamingSuggestion(){
        Random random = new Random();
       List<VideoGameResponse> top5 =  top5RatingLeaderboard();
       return top5.get(random.nextInt(5));
    }

    private VideoGameResponse toVideoGameResponse(VideoGameRecord record){
        if (record == null){
            return null;
        }
        VideoGameResponse videoGameResponse = new VideoGameResponse();
        videoGameResponse.setConsoles(record.getConsoles());
        videoGameResponse.setName(record.getName());
        videoGameResponse.setDescription(record.getDescription());
        videoGameResponse.setDownwardVote(record.getDownwardVote());
        videoGameResponse.setTotalVote(record.getVotingPercentage());
        videoGameResponse.setUpwardVote(record.getUpwardVote());
        return videoGameResponse;
    }

}
