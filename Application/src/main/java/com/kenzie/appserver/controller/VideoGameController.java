package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CreateVideoGameRequest;
import com.kenzie.appserver.controller.model.UpdateRequest;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.VideoGameRepository;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.VideoGameService;
import com.kenzie.appserver.service.model.Consoles;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class VideoGameController {
    private final VideoGameService videoGameService;

    VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @PostMapping("/{name}/upvote")
    public ResponseEntity<VideoGameResponse> addUpvote(@PathVariable String name){
       VideoGameResponse response = videoGameService.addUpvote(name);
       if(response == null){
           return ResponseEntity.badRequest().build();
       }
       return ResponseEntity.ok(response);
    }
    @PostMapping("/{name}/downvote")
    public ResponseEntity<VideoGameResponse> addDownvote(@PathVariable String name){
        VideoGameResponse response = videoGameService.addDownVote(name);
        if(response == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<VideoGameResponse> getGameByName(@PathVariable String name) {

            VideoGameRecord videoGameRecord = videoGameService.findByName(name);
            if (videoGameRecord == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(toVideoGameResponse(videoGameRecord));
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoGameResponse>> getAllGames() {
        List<VideoGameResponse> videoGameResponses = videoGameService.listAllVideoGames();
        if (videoGameResponses == null || videoGameResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videoGameResponses);
    }

    @PostMapping
    public ResponseEntity<VideoGameResponse> addGame(@RequestBody CreateVideoGameRequest request) {
        try {
            VideoGameResponse videoGameResponse = videoGameService.addNewVideoGame(request);
          String  encodedUri = URLEncoder.encode(videoGameResponse.getName(),"UTF-8");
            return ResponseEntity.created(URI.create("/games/" + encodedUri)).body(videoGameResponse);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/{name}")
    public ResponseEntity<VideoGameResponse> updateVideoGame(@PathVariable String name, @RequestBody UpdateRequest videoGameUpdateRequest) {
        VideoGameResponse videoGameResponse = videoGameService.updateVideoGame(videoGameUpdateRequest);
        if (videoGameResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(videoGameResponse);
    }


    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteGame(@PathVariable String name) {
        boolean response = videoGameService.deleteVideoGame(name);
        if (!response) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<VideoGameResponse>> getTop5Leaderboard() {
        List<VideoGameResponse> videoGameList = videoGameService.top5RatingLeaderboard();
        if (videoGameList == null || videoGameList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videoGameList);
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

//    private String encodeUri(String name){
//        if(name.contains(" ")){
//            name.replaceAll(" ","-");
//        }
//        return name;
//    }
//    private String decodeUri(String name){
//        if(name.contains("-")){
//            name.replaceAll("-"," ");
//        }
//        return name;
//    }

}
