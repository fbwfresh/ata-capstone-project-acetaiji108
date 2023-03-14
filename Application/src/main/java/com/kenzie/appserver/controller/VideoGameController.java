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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class VideoGameController {
    private final VideoGameService videoGameService;

    VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
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
        System.out.println("starting controller");
        VideoGameResponse videoGameResponse = videoGameService.addNewVideoGame(request);
        System.out.println("executed service method");
//TODO edit this to include a converter class that I should make to replace a space character with a % or & or _ or whatever then another method
        //TODO to decode the % or & or _ into a space. Also should edit this return to create a new URI with that special character
        //TODO also need to update the get method to be able to retrieve it using that special character
           return ResponseEntity.ok(videoGameResponse);


 //       return ResponseEntity.created(URI.create("/games/" + videoGameResponse.getName())).body(videoGameResponse);

    }
//    @PutMapping("/teacher/{teacherName}")
//    public ResponseEntity<ReviewResponse> updateReview(@PathVariable String teacherName, @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
//        try {
//            Review review = new Review();
//            review.setTeacherName(reviewUpdateRequest.getTeacherName());
//            review.setDatePosted(reviewUpdateRequest.getDatePosted());
//            review.setCourseTitle(reviewUpdateRequest.getCourseTitle());
//            review.setComment(reviewUpdateRequest.getComment());
//            review.setUsername(reviewUpdateRequest.getUsername());
//            review.setPresentation(reviewUpdateRequest.getPresentation());
//            review.setOutgoing(reviewUpdateRequest.getOutgoing());
//            review.setSubjectKnowledge(reviewUpdateRequest.getSubjectKnowledge());
//            review.setListening(reviewUpdateRequest.getListening());
//            review.setCommunication(reviewUpdateRequest.getCommunication());
//            review.setAvailability(reviewUpdateRequest.getAvailability());
//            Review updatedReview = reviewService.updateReview(review);
//            ReviewResponse reviewResponse = convertToResponse(updatedReview);
//            return ResponseEntity.accepted().body(reviewResponse);
//        } catch (ReviewNotFoundException e){
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PutMapping("/{name}")
    public ResponseEntity<VideoGameResponse> updateVideoGame(@RequestBody UpdateRequest videoGameUpdateRequest) {
        VideoGameResponse videoGameResponse = videoGameService.updateVideoGame(videoGameUpdateRequest);
        if (videoGameResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(videoGameResponse);
    }

//    @PutMapping("/{name}")
//    public ResponseEntity<VideoGameResponse> updateDescription(@RequestBody UpdateRequest videoGameUpdateRequest) {
//        VideoGameResponse videoGameResponse = videoGameService.updateVideoGameDescription(videoGameUpdateRequest);
//        if (videoGameResponse == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(videoGameResponse);
//    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteGame(@PathVariable String name) {
        String response = videoGameService.deleteVideoGame(name);
        if (response.equals("Game Not Found. Try Checking The Spelling, And Try Again")) {
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
        videoGameResponse.setTotalVote(record.getVotingPercentage());
        videoGameResponse.setUpwardVote(record.getUpwardVote());
        return videoGameResponse;
    }

}
