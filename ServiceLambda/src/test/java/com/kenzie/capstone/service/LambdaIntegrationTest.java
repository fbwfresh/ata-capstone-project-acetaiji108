package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.dependency.DaoModule;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.*;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void getVideoGameTest() throws InvalidGameException {
        // GIVEN
//        VideoGameRequest request = new VideoGameRequest();
//        request.setName("Persona 5");
//        request.setDescription("A classic platformer game developed by Nintendo.");
//        //request.setConsoles((Set<String>) List.of("Nintendo Entertainment System", "Game Boy Advance"));
//        VideoGame game = new VideoGame(request.getName(), request.getDescription(), Consoles.NS, Consoles.GBA);
//
//
//        VideoGameRecord record = new VideoGameRecord();
//        record.setName(game.getName());
//        record.setDescription(game.getDescription());
//        record.setConsoles(game.getConsoles());

        //when(videoGameDao.findByName(request.getName())).thenReturn(record);

        // WHEN
        VideoGameResponse response = videoGameService.getVideoGame("Persona 5");


        // THEN
        //verify(videoGameDao, times(1)).findByName(request.getName());


//        assertEquals(request.getDescription(), response.getDescription(), "The video game description matches");
//        assertEquals(record.getConsoles(), response.getConsoles(), "The video game consoles match");
    }

    @Test
    void testGetAllVideoGames() {
        // GIVEN
        List<VideoGameResponse> response = videoGameService.getAllVideoGames();

        // WHEN
        for (VideoGameResponse game : response) {
            System.out.println("Name: " + game.getName());
            System.out.println("Description: " + game.getDescription());
            System.out.println("Consoles: " + game.getConsoles());
            System.out.println("------------------------");
        }

        // THEN
        assertNotNull(response, "The response is valid");
        assertEquals(51, response.size(), "There are 51 games in the database");
    }
}

