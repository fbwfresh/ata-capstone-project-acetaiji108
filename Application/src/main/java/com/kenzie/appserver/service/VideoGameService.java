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
    public VideoGameResponse addUpvote(String name){
       VideoGameRecord record = findByName(name);
       record.setUpwardVote(record.getUpwardVote() + 1);
       record.setTotalVote(record.getTotalVote() + 1);
       VideoGameRequest createVideoGameRequest = new VideoGameRequest();
       createVideoGameRequest.setName(record.getName());
       createVideoGameRequest.setDescription(record.getDescription());
       createVideoGameRequest.setImage(record.getImage());
       createVideoGameRequest.setTotalVote(record.getTotalVote());
       createVideoGameRequest.setConsoles(record.getConsoles());
       createVideoGameRequest.setDownwardVote(record.getDownwardVote());
       createVideoGameRequest.setUpwardVote(record.getUpwardVote());
       videoGameServiceClient.addVideoGame(createVideoGameRequest);
       return toVideoGameResponse(record);
    }
    public VideoGameResponse addDownVote(String name){
        VideoGameRecord record = findByName(name);
        record.setDownwardVote(record.getDownwardVote() + 1);
        record.setTotalVote(record.getTotalVote() + 1);
        VideoGameRequest createVideoGameRequest = new VideoGameRequest();
        createVideoGameRequest.setName(record.getName());
        createVideoGameRequest.setDescription(record.getDescription());
        createVideoGameRequest.setImage(record.getImage());
        createVideoGameRequest.setTotalVote(record.getTotalVote());
        createVideoGameRequest.setConsoles(record.getConsoles());
        createVideoGameRequest.setDownwardVote(record.getDownwardVote());
        createVideoGameRequest.setUpwardVote(record.getUpwardVote());
        videoGameServiceClient.addVideoGame(createVideoGameRequest);
        return toVideoGameResponse(record);
    }
    public VideoGameRecord findByName(String name) {
        // Example getting data from the local repository

//        VideoGameRecord dataFromDynamo = videoGameRepository
//                .findById(name).orElse(null);
//        return dataFromDynamo;
try {
    com.kenzie.capstone.service.model.VideoGameResponse responseFromLambdaClient = videoGameServiceClient.getVideoGame(name);
    VideoGameRecord record = new VideoGameRecord();
    record.setName(responseFromLambdaClient.getName());
    record.setDescription(responseFromLambdaClient.getDescription());
    record.setConsoles(responseFromLambdaClient.getConsoles());
    record.setUpwardVote(responseFromLambdaClient.getUpwardVote());
    record.setDownwardVote(responseFromLambdaClient.getDownwardVote());
    record.setTotalVote(responseFromLambdaClient.getTotalVote());
    record.setImage(responseFromLambdaClient.getImage());
    return record;

}catch (NullPointerException e){
    return null;
}
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
        videoGameRecord.setName(game.getName());
        videoGameRecord.setDescription(game.getDescription());
        videoGameRecord.setConsoles(game.getConsoles());
        videoGameRecord.setDownwardVote(game.getDownwardVote());
        videoGameRecord.setUpwardVote(game.getUpwardVote());
        videoGameRecord.setTotalVote(game.getTotalVote());
        videoGameRecord.setImage(game.getImage());
       // System.out.println("before save");
        videoGameRepository.save(videoGameRecord);

        VideoGameRequest videoGameRequest = new VideoGameRequest();
        videoGameRequest.setConsoles(game.getConsoles());
        videoGameRequest.setDescription(game.getDescription());
        videoGameRequest.setName(game.getName());
        videoGameRequest.setDownwardVote(game.getDownwardVote());
        videoGameRequest.setUpwardVote(game.getUpwardVote());
        videoGameRequest.setTotalVote(game.getTotalVote());
        videoGameRequest.setImage(game.getImage());
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

           videoGameResponse.setImage(response.getImage());
             System.out.println(response.getImage());

           return videoGameResponse;
       }).collect(Collectors.toList());
          return videoGameResponseList;
    }


    public VideoGameResponse updateVideoGame(String name,UpdateRequest videoGameUpdateRequest) {
      com.kenzie.capstone.service.model.VideoGameResponse response = videoGameServiceClient.getVideoGame(name);
        if (response != null) {
            VideoGameRequest videoGameRequest = new VideoGameRequest();
            videoGameRequest.setName(name);
            videoGameRequest.setConsoles(videoGameUpdateRequest.getConsoles());
            videoGameRequest.setDescription(videoGameUpdateRequest.getDescription());
            videoGameRequest.setUpwardVote(videoGameUpdateRequest.getUpwardVote());
            videoGameRequest.setTotalVote(videoGameUpdateRequest.getTotalVote());
            videoGameRequest.setDownwardVote(videoGameUpdateRequest.getDownwardVote());
            videoGameRequest.setImage(videoGameUpdateRequest.getImage());
            videoGameServiceClient.updateVideoGame(name,videoGameRequest);
            //videoGameServiceClient.addVideoGame(videoGameRequest);

            VideoGameRecord videoGameRecord = new VideoGameRecord();
            videoGameRecord.setName(name);
            videoGameRecord.setDescription(videoGameUpdateRequest.getDescription());
            videoGameRecord.setConsoles(videoGameUpdateRequest.getConsoles());
            videoGameRecord.setDownwardVote(videoGameUpdateRequest.getDownwardVote());
            videoGameRecord.setUpwardVote(videoGameUpdateRequest.getUpwardVote());
            videoGameRecord.setTotalVote(videoGameUpdateRequest.getTotalVote());
            videoGameRecord.setImage(videoGameUpdateRequest.getImage());
            videoGameRepository.save(videoGameRecord);


            VideoGameResponse controllerResponse = new VideoGameResponse();
            controllerResponse.setImage(videoGameRecord.getImage());
            controllerResponse.setConsoles(videoGameRecord.getConsoles());
            controllerResponse.setName(name);
            controllerResponse.setTotalVote(videoGameRecord.getTotalVote());
            controllerResponse.setUpwardVote(videoGameRecord.getUpwardVote());
            controllerResponse.setDownwardVote(videoGameRecord.getDownwardVote());
            controllerResponse.setDescription(videoGameRecord.getDescription());
            return controllerResponse;
        }else
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Couldn't find the requested game.");
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

    public boolean deleteVideoGame(String name) {
        if (videoGameRepository.findById(name).isPresent()) {
            videoGameRepository.delete((videoGameRepository.findById(name).get()));

        }
        boolean deleted = videoGameServiceClient.deleteVideoGame(name);

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
    public List<VideoGameResponse> allGamesHighestToLowest() {
        List<VideoGameResponse> videoGameResponsesList = listAllVideoGames();
        List<VideoGameResponse> highToLow = videoGameResponsesList.stream()
                .sorted(Comparator.comparing(VideoGameResponse::getUpwardVote).reversed())
                .collect(Collectors.toList());
        return highToLow;
    }
    public List<VideoGameResponse> allGamesLowestToHighest() {
        List<VideoGameResponse> videoGameResponsesList = listAllVideoGames();
        List<VideoGameResponse> lowToHigh = videoGameResponsesList.stream()
                .sorted(Comparator.comparing(VideoGameResponse::getUpwardVote))
                .collect(Collectors.toList());
        return lowToHigh;
    }

    public VideoGameResponse gamingSuggestion(){
        Random random = new Random();
       List<VideoGameResponse> highestToLowest =  allGamesHighestToLowest();
       return highestToLowest.get(random.nextInt(15));
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
        videoGameResponse.setTotalVote(record.getTotalVote());
        videoGameResponse.setUpwardVote(record.getUpwardVote());
        videoGameResponse.setImage(record.getImage());
        return videoGameResponse;
    }


}
