package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.VideoGameRepository;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.model.Consoles;
import com.kenzie.appserver.service.model.VideoGame;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class VideoGameService {
    //TODO add the lambda functionality to the service methods whenever we get that part figured out and also add a leaderboard
    // functionality
    private VideoGameRepository videoGameRepository;
    private LambdaServiceClient lambdaServiceClient;

    public VideoGameService(VideoGameRepository videoGameRepository, LambdaServiceClient lambdaServiceClient) {
        this.videoGameRepository = videoGameRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public VideoGameResponse findByName(String name) {
        // Example getting data from the local repository
        VideoGameRecord dataFromDynamo = videoGameRepository
                .findById(name).orElse(null);
              //  .map(game -> new VideoGame(game.getName(), game.getDescription(),))
        return toVideoGameResponse(dataFromDynamo);
        //TODO Example getting data from the lambda
        //ExampleData dataFromLambda = lambdaServiceClient.getExampleData(name);

    }

    public VideoGameResponse addNewVideoGame(String name, String description, Consoles... consoles) {
        // Example sending data to the local repository
        VideoGame videoGame = new VideoGame(name,description,consoles);
        VideoGameRecord videoGameRecord = new VideoGameRecord();
        videoGameRecord.setName(videoGame.getName());
        videoGameRecord.setDescription(videoGame.getDescription());
        videoGameRecord.setConsoles(videoGame.getConsoles());
        videoGameRepository.save(videoGameRecord);
        //TODO the code below is how to save the videogame utilizing lambda functionality
        //Example sending data to the lambda
        // VideoGameRecord dataFromLambda = lambdaServiceClient.setVideoGameData(name,description,consoles);
        //videoGameRecord.setId(dataFromLambda.getId());
        // videoGameRecord.setName(dataFromLambda.getData());
       // videoGameRepository.save(exampleRecord);
        //Example example = new Example(dataFromLambda.getId(), name);
        return toVideoGameResponse(videoGameRecord);
    }

    public List<VideoGameResponse> listAllVideoGames(){
        List<VideoGameResponse> videoGameResponses = new ArrayList<>();
        Iterator<VideoGameRecord> videoGameRecordList =  videoGameRepository.findAll().iterator();
        while(videoGameRecordList.hasNext()){
            videoGameResponses.add(toVideoGameResponse(videoGameRecordList.next()));
        }
        return videoGameResponses;
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
    public VideoGameResponse updateVideoGameDescription(String name,String description) {
        Optional<VideoGameRecord> gameExists = videoGameRepository.findById(name);
        if (!gameExists.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Game Not Found");
        }
        gameExists.get().setDescription(description);
        videoGameRepository.save(gameExists.get());
        return toVideoGameResponse(gameExists.get());
    }

    public String deleteVideoGame(String name){
      if (videoGameRepository.findById(name).isPresent()) {
          videoGameRepository.delete((videoGameRepository.findById(name).get()));
          return "Deleted Game";
      }
      return "Game Not Found. Try Checking The Spelling, And Try Again";
    }
    private VideoGameResponse toVideoGameResponse(VideoGameRecord record){
        if (record == null){
            return null;
        }
        VideoGameResponse videoGameResponse = new VideoGameResponse();
        videoGameResponse.setConsoles(record.getConsoles());
        videoGameResponse.setName(record.getName());
        videoGameResponse.setDescription(record.getDescription());
        return videoGameResponse;
    }
}
