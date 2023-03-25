package com.kenzie.appserver.service;
import com.kenzie.appserver.controller.model.CreateVideoGameRequest;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.VideoGameRepository;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.model.Consoles;
import com.kenzie.appserver.service.model.VideoGame;
import com.kenzie.capstone.service.client.VideoGameServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VideoGameServiceTest {
    private VideoGameRepository videoGameRepository;
    private VideoGameService videoGameService;
    private VideoGameServiceClient videoGameServiceClient;

    @BeforeEach
    void setup() {
        videoGameRepository = mock(VideoGameRepository.class);
        videoGameServiceClient = mock(VideoGameServiceClient.class);
        //videoGame = new VideoGame(videoGameRepository,
        videoGameService = new VideoGameService(videoGameRepository,videoGameServiceClient);

    }

    @Test
    void findAllGamesTwoGames_Test() {
        // GIVEN
        com.kenzie.capstone.service.model.VideoGameResponse videoGame1 = new com.kenzie.capstone.service.model.VideoGameResponse();
        videoGame1.setName("name1");

        com.kenzie.capstone.service.model.VideoGameResponse videoGame2 = new com.kenzie.capstone.service.model.VideoGameResponse();
        videoGame2.setName("name2");

        List<com.kenzie.capstone.service.model.VideoGameResponse> recordList = new ArrayList<>();
        recordList.add(videoGame1);
        recordList.add(videoGame2);
        when(videoGameServiceClient.getAllVideoGames()).thenReturn(recordList);

        // WHEN
        List<VideoGameResponse> videoGames = videoGameService.listAllVideoGames();

        // THEN
        assertNotNull(videoGames, "The video game list is returned");
        assertEquals(2, videoGames.size(), "There are two video games");

        for (VideoGameResponse videoGame : videoGames) {
            if (videoGame.getName().equals(videoGame1.getName())) {
                assertEquals(videoGame1.getName(), videoGame.getName(), "The video game name matches");
                assertEquals(videoGame1.getDescription(), videoGame.getDescription(), "The video game description matches");
            } else if (videoGame.getName().equals(videoGame2.getName())) {
                assertEquals(videoGame2.getName(), videoGame.getName(), "The video game name matches");
                assertEquals(videoGame2.getDescription(), videoGame.getDescription(), "The video game description matches");
            } else {
                assertTrue(false, "Video game returned that was not in the records!");
            }
        }
    }
    @Test
    void findByVideoGameName_Test() {
        // GIVEN
        String gameName = "testGame";
        VideoGame game = new VideoGame(gameName,"testDescription","testImage",Consoles.PC,Consoles.DS);

        com.kenzie.capstone.service.model.VideoGameResponse response = new com.kenzie.capstone.service.model.VideoGameResponse();
        response.setName(gameName);
        response.setDescription("testDescription");
        response.setConsoles(game.getConsoles());
        response.setImage(game.getImage());
        when(videoGameServiceClient.getVideoGame(gameName)).thenReturn(response);
        // WHEN
        VideoGameRecord videoGameResponse = videoGameService.findByName(gameName);

        // THEN
        assertNotNull(videoGameResponse, "The video game is returned");
        assertEquals(response.getName(), videoGameResponse.getName(), "The video game name matches");
        assertEquals(response.getDescription(), videoGameResponse.getDescription(), "The video game description matches");
    }

    @Test
    void addNewVideoGame_Test() {
        // GIVEN
        String gameName = "testGame";
        String gameDescription = "testDescription";
        String image = "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg";

        ///Consoles[] gameConsoles = new Consoles[]{Consoles.PC, Consoles.PS2};
        VideoGame videoGame1 = new VideoGame(gameName,gameDescription,image,Consoles.PS2, Consoles.PC);
        CreateVideoGameRequest videoGameRequest = new CreateVideoGameRequest();
        videoGameRequest.setVideoGameName(gameName);
        videoGameRequest.setDescription(gameDescription);
        videoGameRequest.setConsoles(videoGame1.getConsoles());
        videoGameRequest.setDownwardVote(videoGame1.getDownwardVote());
        videoGameRequest.setUpwardVote(videoGame1.getUpwardVote());
        videoGameRequest.setTotalVote(videoGame1.getTotalVote());
        videoGameRequest.setImage(videoGame1.getImage());
        // WHEN
        VideoGameResponse videoGameResponse = videoGameService.addNewVideoGame(videoGameRequest);

        // THEN
        assertNotNull(videoGameResponse, "The video game is returned");
        assertEquals(gameName, videoGameResponse.getName(), "The video game name matches");
        assertEquals(gameDescription, videoGameResponse.getDescription(), "The video game description matches");
    }


    @Test
    void listAllVideoGames_Test() {
        VideoGame videoGame = new VideoGame("test","testing","image",Consoles.PS4,Consoles.NS);
        // GIVEN
        com.kenzie.capstone.service.model.VideoGameResponse response0 = new com.kenzie.capstone.service.model.VideoGameResponse();
        response0.setName("testGame1");
        response0.setDescription("testDescription1");
        response0.setConsoles(videoGame.getConsoles());
        response0.setImage("image");
        response0.setTotalVote(0);
        response0.setDownwardVote(0);
        response0.setUpwardVote(0);

        com.kenzie.capstone.service.model.VideoGameResponse response = new com.kenzie.capstone.service.model.VideoGameResponse();
        response.setName("testGame2");
        response.setDescription("testDescription2");
        response.setConsoles(videoGame.getConsoles());
        response.setImage("image");
        response.setTotalVote(0);
        response.setDownwardVote(0);
        response.setUpwardVote(0);


        List<com.kenzie.capstone.service.model.VideoGameResponse> recordList = new ArrayList<>();
        recordList.add(response0);
        recordList.add(response);
        when(videoGameServiceClient.getAllVideoGames()).thenReturn(recordList);

        // WHEN
        List<VideoGameResponse> videoGameResponses = videoGameService.listAllVideoGames();

        // THEN
        assertNotNull(videoGameResponses, "The video game list is returned");
        assertEquals(2, videoGameResponses.size(), "There are two video games");

        for (VideoGameResponse videoGameResponse : videoGameResponses) {
            if (videoGameResponse.getName().equals(response0.getName())) {
                assertEquals(response0.getName(), videoGameResponse.getName(), "The video game name matches");
                assertEquals(response0.getDescription(), videoGameResponse.getDescription(), "The video game description matches");
            } else if (videoGameResponse.getName().equals(response.getName())) {
                assertEquals(response.getName(), videoGameResponse.getName(), "The video game name matches");
                assertEquals(response.getDescription(), videoGameResponse.getDescription(), "The video game description matches");
            } else {
                Assertions.fail("Video game returned that was not in the records!");
            }
        }
    }

    @Test
    void findByNameInvalidVideoGame_Test() {
        // GIVEN
        String videoGameName = "invalid";
        // WHEN
        when(videoGameServiceClient.getVideoGame(videoGameName)).thenReturn(null);
        // THEN
        Assertions.assertNull(videoGameService.findByName(videoGameName));
    }

    @Test
    void findByNameGameNotFound_Test() {
        // GIVEN
        String gameName = "gameName";

        // WHEN
        when(videoGameServiceClient.getVideoGame(gameName)).thenReturn(null);


        // THEN
        Assertions.assertNull(videoGameService.findByName(gameName));
    }

    @Test
    void findByNameInvalidGame_Test() {
        // GIVEN
        String gameName = UUID.randomUUID().toString();

        // WHEN
        when(videoGameServiceClient.getVideoGame(gameName)).thenReturn(null);

        // THEN
        Assertions.assertNull(videoGameService.findByName(gameName));
    }

    @Test
    void addNewVideoGameSecond_Test() {
        // GIVEN
        String videoGameName = "videoGameName";
        String description = "description";
        String image = "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg";

       // Consoles[] consoles = {Consoles.PS5, Consoles.WII};

        VideoGame videoGame1 = new VideoGame(videoGameName,description,image,Consoles.PS5,Consoles.WII);
        CreateVideoGameRequest videoGameRequest = new CreateVideoGameRequest();
        videoGameRequest.setTotalVote(videoGame1.getTotalVote());
        videoGameRequest.setUpwardVote(videoGame1.getUpwardVote());
        videoGameRequest.setDownwardVote(videoGame1.getDownwardVote());
        videoGameRequest.setDescription(videoGame1.getDescription());
        videoGameRequest.setConsoles(videoGame1.getConsoles());
        videoGameRequest.setVideoGameName(videoGame1.getName());
        videoGameRequest.setImage(videoGame1.getImage());

        VideoGameResponse expectedVideoGameResponse = new VideoGameResponse();
        expectedVideoGameResponse.setName(videoGameName);
        expectedVideoGameResponse.setDescription(description);
        expectedVideoGameResponse.setConsoles(videoGame1.getConsoles());
        expectedVideoGameResponse.setUpwardVote(0);
        expectedVideoGameResponse.setDownwardVote(0);
        expectedVideoGameResponse.setTotalVote(0);
        expectedVideoGameResponse.setImage(videoGame1.getImage());

        VideoGameRecord expectedVideoGameRecord = new VideoGameRecord();
        expectedVideoGameRecord.setName(videoGameName);
        expectedVideoGameRecord.setDescription(description);
        //expectedVideoGameRecord.setConsoles(consoles);
        expectedVideoGameRecord.setConsoles(videoGame1.getConsoles());
        expectedVideoGameRecord.setImage(videoGame1.getImage());
        expectedVideoGameRecord.setUpwardVote(0);
        expectedVideoGameRecord.setDownwardVote(0);
        expectedVideoGameRecord.setTotalVote(0);

        ArgumentCaptor<VideoGameRecord> videoGameRecordCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);

        // WHEN
        VideoGameResponse returnedVideoGameResponse = videoGameService.addNewVideoGame(videoGameRequest);

        // THEN
        assertNotNull(returnedVideoGameResponse);

        verify(videoGameRepository).save(videoGameRecordCaptor.capture());

        VideoGameRecord record = videoGameRecordCaptor.getValue();

        assertNotNull(record, "The video game record is returned");
        assertNotNull(record.getName(), "The video game name is returned");
        assertEquals(record.getName(), videoGameName, "The video game name matches");
        assertEquals(record.getDescription(), description, "The video game description matches");
        Assertions.assertEquals(record.getConsoles(), videoGame1.getConsoles(), "The video game consoles match");
        assertEquals(record.getUpwardVote(), expectedVideoGameRecord.getUpwardVote(), "The video game upward vote matches");
        assertEquals(record.getDownwardVote(), expectedVideoGameRecord.getDownwardVote(), "The video game downward vote matches");
        assertEquals(record.getTotalVote(), expectedVideoGameRecord.getTotalVote(), "The video game voting percentage matches");
        assertEquals(record.getImage(),expectedVideoGameRecord.getImage());

        assertEquals(returnedVideoGameResponse.getName(), expectedVideoGameResponse.getName(), "The returned video game name matches");
        assertEquals(returnedVideoGameResponse.getDescription(), expectedVideoGameResponse.getDescription(), "The returned video game description matches");
        //Assertions.assertArrayEquals(returnedVideoGameResponse.getConsoles(), expectedVideoGameResponse.getConsoles(), "The returned video game consoles match");
        assertEquals(returnedVideoGameResponse.getUpwardVote(), expectedVideoGameResponse.getUpwardVote(), "The returned video game upward vote matches");
        assertEquals(returnedVideoGameResponse.getDownwardVote(), expectedVideoGameResponse.getDownwardVote(), "The returned video game downward vote matches");
        assertEquals(returnedVideoGameResponse.getTotalVote(), expectedVideoGameResponse.getTotalVote(), "The returned video game total vote matches");
        assertEquals(returnedVideoGameResponse.getImage(),expectedVideoGameResponse.getImage());
    }

    @Test
    void updateVideoGameDescription_Test() {
        // GIVEN
        String gameName = "gameName";
        String description = "oldDescription";
        String newDescription = "newDescription";
        VideoGameRecord oldVideoGameRecord = new VideoGameRecord();
        oldVideoGameRecord.setName(gameName);
        oldVideoGameRecord.setDescription(description);
        videoGameRepository.save(oldVideoGameRecord);

        when(videoGameRepository.findById(gameName)).thenReturn(Optional.of(oldVideoGameRecord));
        ArgumentCaptor<VideoGameRecord> videoGameRecordCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);

        // Capture the video game record passed to videoGameRepository.save()
        doAnswer(invocation -> {
            VideoGameRecord record = invocation.getArgumentAt(0, VideoGameRecord.class);
            assertNotNull(record, "The video game record is not null");
            assertEquals(record.getName(), gameName, "The game name matches");
            assertEquals(record.getDescription(), newDescription, "The game description has been updated");
            assertEquals(record.getConsoles(), oldVideoGameRecord.getConsoles(), "The game consoles have not changed");
            return null;
        }).when(videoGameRepository).save(videoGameRecordCaptor.capture());

        // WHEN
        VideoGameResponse updatedVideoGameResponse = videoGameService.updateVideoGameDescription(gameName, newDescription);

        // THEN
        VideoGameRecord record = videoGameRecordCaptor.getValue();
        assertNotNull(record, "The video game record is not null");
        assertEquals(record.getName(), gameName, "The game name matches");
        assertEquals(record.getDescription(), newDescription, "The game description has been updated");
        assertEquals(record.getConsoles(), oldVideoGameRecord.getConsoles(), "The game consoles have not changed");

        assertNotNull(updatedVideoGameResponse, "The video game response is returned");
        assertEquals(updatedVideoGameResponse.getName(), gameName, "The game name matches");
        assertEquals(updatedVideoGameResponse.getDescription(), newDescription, "The game description has been updated");
        assertEquals(updatedVideoGameResponse.getConsoles(), oldVideoGameRecord.getConsoles(), "The game consoles have not changed");
    }

    @Test
    void updateVideoGameDescriptionVideoGameDoesNotExist_Test() {
        // GIVEN
        String gameName = "nonExistentGame";
        String newDescription = "newDescription";

        when(videoGameRepository.findById(gameName)).thenReturn(Optional.empty());

        // WHEN
        Assertions.assertThrows(ResponseStatusException.class, () -> videoGameService.updateVideoGameDescription(gameName, newDescription));

        // THEN
        try {
            verify(videoGameRepository, never()).save(any(VideoGameRecord.class));
        } catch(MockitoAssertionError error) {
            throw new MockitoAssertionError("There should not be a call to .save() if the video game is not found in the database. - " + error);
        }
    }

    @Test
    void deleteVideoGameNotFound_Test() {
        // GIVEN
        String gameName = "gameName";
        when(videoGameRepository.findById(gameName)).thenReturn(Optional.empty());

        // WHEN
        boolean result = videoGameService.deleteVideoGame(gameName);

        // THEN
        assertFalse(result);
        verify(videoGameRepository, never()).delete(any());
    }

    @Test
    void addNewVideoGameWithValidRequest_returnsVideoGameResponse_Test() {
        // GIVEN
        String name = "gameName";
        String description = "gameDescription";
        String image = "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg";
        VideoGame videoGame1 = new VideoGame(name,description,image,Consoles.PS5,Consoles.WIIU);
        CreateVideoGameRequest videoGameRequest = new CreateVideoGameRequest();
        videoGameRequest.setVideoGameName(videoGame1.getName());
        videoGameRequest.setConsoles(videoGame1.getConsoles());
        videoGameRequest.setDescription(videoGame1.getDescription());
        videoGameRequest.setTotalVote(videoGame1.getTotalVote());
        videoGameRequest.setUpwardVote(videoGame1.getUpwardVote());
        videoGameRequest.setDownwardVote(videoGame1.getDownwardVote());
        videoGameRequest.setImage(videoGame1.getImage());

        // WHEN
        VideoGameResponse response = videoGameService.addNewVideoGame(videoGameRequest);

        // THEN
        assertEquals(name, response.getName(), "Name should match");
        assertEquals(image,response.getImage());
        assertEquals(description, response.getDescription(), "Description should match");
        assertEquals(0, response.getUpwardVote(), "Upward vote should be 0");
        assertEquals(0, response.getDownwardVote(), "Downward vote should be 0");
        assertEquals(0, response.getTotalVote(), "Total vote should be 0");
    }

//    @Test
//    void top5RatingLeaderboard() {
//        // GIVEN
//        VideoGameRecord record1 = new VideoGameRecord();
//        record1.setName("Game 1");
//        record1.setUpwardVote(50);
//        record1.setDownwardVote(20);
//        record1.setVotingPercentage(70);
//        videoGameRepository.save(record1);
//
//        VideoGameRecord record2 = new VideoGameRecord();
//        record2.setName("Game 2");
//        record2.setUpwardVote(40);
//        record2.setDownwardVote(10);
//        record2.setVotingPercentage(80);
//        videoGameRepository.save(record2);
//
//        VideoGameRecord record3 = new VideoGameRecord();
//        record3.setName("Game 3");
//        record3.setUpwardVote(100);
//        record3.setDownwardVote(50);
//        record3.setVotingPercentage(67);
//        videoGameRepository.save(record3);
//
//        VideoGameRecord record4 = new VideoGameRecord();
//        record4.setName("Game 4");
//        record4.setUpwardVote(80);
//        record4.setDownwardVote(30);
//        record4.setVotingPercentage(73);
//        videoGameRepository.save(record4);
//
//        VideoGameRecord record5 = new VideoGameRecord();
//        record5.setName("Game 5");
//        record5.setUpwardVote(70);
//        record5.setDownwardVote(20);
//        record5.setVotingPercentage(78);
//        videoGameRepository.save(record5);
//
//        VideoGameRecord record6 = new VideoGameRecord();
//        record6.setName("Game 6");
//        record6.setUpwardVote(50);
//        record6.setDownwardVote(20);
//        record6.setVotingPercentage(75);
//        videoGameRepository.save(record6);
//
//        List<VideoGameRecord> records = List.of(record1, record2, record3, record4, record5, record6);
//        when(videoGameRepository.findAll()).thenReturn(records);
//
//        // WHEN
//        List<VideoGameResponse> top5 = new VideoGameService(videoGameRepository).top5RatingLeaderboard();
//
//        // THEN
//        // list the top 5 games above with the highest voting percentage
//        verify(videoGameRepository, times(1)).findAll();
//        assertEquals(5, top5.size(), "There should be 5 games in the list");
//        assertEquals("Game 2", top5.get(0).getName(), "Game 2 should be the first game in the list");
//        assertEquals("Game 1", top5.get(1).getName(), "Game 1 should be the second game in the list");
//        assertEquals("Game 6", top5.get(2).getName(), "Game 6 should be the third game in the list");
//        assertEquals("Game 5", top5.get(3).getName(), "Game 5 should be the fourth game in the list");
//        assertEquals("Game 4", top5.get(4).getName(), "Game 4 should be the fifth game in the list");
//    }

    @Test
    void top5RatingLeaderboard() {
        // GIVEN
        com.kenzie.capstone.service.model.VideoGameResponse record1 = new com.kenzie.capstone.service.model.VideoGameResponse();
        record1.setName("Game 1");
        record1.setUpwardVote(50);
        record1.setDownwardVote(20);
        record1.setTotalVote(70);


        com.kenzie.capstone.service.model.VideoGameResponse record2 = new com.kenzie.capstone.service.model.VideoGameResponse();
        record2.setName("Game 2");
        record2.setUpwardVote(40);
        record2.setDownwardVote(10);
        record2.setTotalVote(80);


        com.kenzie.capstone.service.model.VideoGameResponse record3 = new com.kenzie.capstone.service.model.VideoGameResponse();
        record3.setName("Game 3");
        record3.setUpwardVote(100);
        record3.setDownwardVote(50);
        record3.setTotalVote(67);


        com.kenzie.capstone.service.model.VideoGameResponse record4 = new com.kenzie.capstone.service.model.VideoGameResponse();
        record4.setName("Game 4");
        record4.setUpwardVote(80);
        record4.setDownwardVote(30);
        record4.setTotalVote(73);


        com.kenzie.capstone.service.model.VideoGameResponse record5 = new com.kenzie.capstone.service.model.VideoGameResponse();
        record5.setName("Game 5");
        record5.setUpwardVote(70);
        record5.setDownwardVote(20);
        record5.setTotalVote(78);



        com.kenzie.capstone.service.model.VideoGameResponse record6 = new com.kenzie.capstone.service.model.VideoGameResponse();
        record6.setName("Game 6");
        record6.setUpwardVote(50);
        record6.setDownwardVote(20);
        record6.setTotalVote(75);


        List<com.kenzie.capstone.service.model.VideoGameResponse> responses = List.of(record1, record2, record3, record4, record5, record6);
        when(videoGameServiceClient.getAllVideoGames()).thenReturn(responses);

        // WHEN
        List<VideoGameResponse> top5 = videoGameService.top5RatingLeaderboard();

        // THEN
        // list the top 5 games above with the highest upvotes
        verify(videoGameServiceClient, times(1)).getAllVideoGames();
        assertEquals(5, top5.size(), "There should be 5 games in the list");
        assertEquals("Game 3", top5.get(0).getName(), "Game 3 should be the first game in the list");
    }




}

