package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CreateVideoGameRequest;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.VideoGameRepository;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.VideoGameService;
import com.kenzie.appserver.service.model.Consoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        VideoGameResponse videoGameResponse = videoGameService.findByName(name);
        if (videoGameResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(videoGameResponse);
    }

    @GetMapping
    public ResponseEntity<List<VideoGameResponse>> getAllGames() {
        List<VideoGameResponse> videoGameResponses = videoGameService.listAllVideoGames();
        if (videoGameResponses == null || videoGameResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videoGameResponses);
    }

    @PostMapping
    public ResponseEntity<VideoGameResponse> addGame(@RequestBody CreateVideoGameRequest request) {
        VideoGameResponse videoGameResponse = videoGameService.addNewVideoGame(request.getVideoGameName(), request.getDescription(),request.getConsoles());
        return ResponseEntity.status(HttpStatus.CREATED).body(videoGameResponse);
    }

    //TODO Create updateRequestClass and replace the pathVariable with the RequestBody
    @PutMapping("/{name}/consoles")
    public ResponseEntity<VideoGameResponse> updateConsoles(@PathVariable String name, @RequestParam Consoles[] consoles) {
        VideoGameResponse videoGameResponse = videoGameService.updateVideoGameConsoles(name, consoles);
        if (videoGameResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(videoGameResponse);
    }
    //TODO Create updateRequestClass and replace the pathVariable with the RequestBody
    @PutMapping("/{name}/description")
    public ResponseEntity<VideoGameResponse> updateDescription(@PathVariable String name, @RequestParam String description) {
        VideoGameResponse videoGameResponse = videoGameService.updateVideoGameDescription(name, description);
        if (videoGameResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(videoGameResponse);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteGame(@PathVariable String name) {
        String response = videoGameService.deleteVideoGame(name);
        if (response.equals("Game Not Found. Try Checking The Spelling, And Try Again")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<VideoGameResponse>> getTop5Leaderboard() {
        List<VideoGameResponse> videoGameList = videoGameService.top5RatingLeaderboard();
        if (videoGameList == null || videoGameList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videoGameList);
    }

}
