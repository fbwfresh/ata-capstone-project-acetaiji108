package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.dependency.DaoModule;
import com.kenzie.capstone.service.model.*;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaIntegrationTest {

    private VideoGameService videoGameService;
    private VideoGameDao videoGameDao;

    @BeforeAll
    void setup() {
        DaoModule module = new DaoModule();
        this.videoGameDao = new NonCachingVideoGameDao(module.provideDynamoDBMapper());
        this.videoGameService = new VideoGameService(videoGameDao);
    }
    @Test
    void addVideoGameTest() {
        // GIVEN
        String gameName = "Contra";
        String description = "Contra is a run-and-gun action platformer, notorious for its high difficulty. " +
                "The player character comes armed with a gun that can shoot infinitely. " +
                "Different weapons with new abilities and that shoot different types of projectiles can be acquired" +
                " as the player progresses through the levels.";
        VideoGame game = new VideoGame(gameName, description, Consoles.DS, Consoles.GC, Consoles.GBA, Consoles.NS, Consoles.WII, Consoles.WIIU);
        VideoGameRequest request = new VideoGameRequest();
        request.setName(game.getName());
        request.setConsoles(game.getConsoles());
        request.setDescription(game.getDescription());
        request.setUpwardVote(request.getUpwardVote());
        request.setDownwardVote(request.getDownwardVote());
        request.setVotingPercentage(request.getVotingPercentage());
        // WHEN
        VideoGameResponse response = this.videoGameService.addVideoGame(request);
        // THEN
        VideoGameRecord record = new VideoGameRecord();
        record.setUpwardVote(response.getUpwardVote());
        record.setVotingPercentage(response.getTotalVote());
        record.setDownwardVote(response.getDownwardVote());
        record.setName(response.getName());
        record.setDescription(response.getDescription());
        record.setConsoles(response.getConsoles());
        assertNotNull(record, "The record is valid");
        assertEquals(gameName, record.getName(), "The record name should match");
        assertEquals(description, record.getDescription(), "The record description should match");
        assertNotNull(record.getConsoles(), "The record consoles exist");
        assertNotNull(response, "A response is returned");
    }
    @Test
    void deleteGameTest() {
        String gameName = "Contra";
        String description = "Contra is a run-and-gun action platformer, notorious for its high difficulty. " +
                "The player character comes armed with a gun that can shoot infinitely. " +
                "Different weapons with new abilities and that shoot different types of projectiles can be acquired" +
                " as the player progresses through the levels.";
        VideoGame game = new VideoGame(gameName, description, Consoles.DS, Consoles.GC, Consoles.GBA, Consoles.NS, Consoles.WII, Consoles.WIIU);
        VideoGameRequest request = new VideoGameRequest();
        request.setName(game.getName());
        request.setConsoles(game.getConsoles());
        request.setDescription(game.getDescription());
        request.setUpwardVote(request.getUpwardVote());
        request.setDownwardVote(request.getDownwardVote());
        request.setVotingPercentage(request.getVotingPercentage());
        assertTrue(videoGameService.deleteVideoGame(request.getName()));
    }
}