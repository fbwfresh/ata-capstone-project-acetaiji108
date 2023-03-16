package com.kenzie.appserver.service;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.VideoGameRepository;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.model.Consoles;
import com.kenzie.appserver.service.model.VideoGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VideoGameServiceTest {
    private VideoGameRepository videoGameRepository;
    private VideoGame videoGame;
    //private ReferralServiceClient referralServiceClient;

    @BeforeEach
    void setup() {
        videoGameRepository = mock(VideoGameRepository.class);
        //referralServiceClient = mock(ReferralServiceClient.class);
        //videoGame = new VideoGame(videoGameRepository,
    }

    @Test
    void findAllGamesTwoGames_Test() {
        // GIVEN
        VideoGameRecord videoGame1 = new VideoGameRecord();
        videoGame1.setName("name1");

        VideoGameRecord videoGame2 = new VideoGameRecord();
        videoGame2.setName("name2");

        List<VideoGameRecord> recordList = new ArrayList<>();
        recordList.add(videoGame1);
        recordList.add(videoGame2);
        when(videoGameRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<VideoGameResponse> videoGames = new VideoGameService(videoGameRepository).listAllVideoGames();

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
                Assertions.assertTrue(false, "Video game returned that was not in the records!");
            }
        }
    }
    @Test
    void findByVideoGameName_Test() {
        // GIVEN
        String gameName = "testGame";

        VideoGameRecord record = new VideoGameRecord();
        record.setName(gameName);
        record.setDescription("testDescription");
        record.setConsoles(new Consoles[]{Consoles.PC, Consoles.DS});

        when(videoGameRepository.findById(gameName)).thenReturn(Optional.of(record));
        // WHEN
        VideoGameRecord videoGameResponse = new VideoGameService(videoGameRepository).findByName(gameName);

        // THEN
        assertNotNull(videoGameResponse, "The video game is returned");
        assertEquals(record.getName(), videoGameResponse.getName(), "The video game name matches");
        assertEquals(record.getDescription(), videoGameResponse.getDescription(), "The video game description matches");
    }

    @Test
    void addNewVideoGame_Test() {
        // GIVEN
        String gameName = "testGame";
        String gameDescription = "testDescription";
        ///Consoles[] gameConsoles = new Consoles[]{Consoles.PC, Consoles.PS2};
        VideoGame videoGame1 = new VideoGame(gameName,gameDescription,Consoles.PS2, Consoles.PC);
        // WHEN
        VideoGameResponse videoGameResponse = new VideoGameService(videoGameRepository).addNewVideoGame(gameName, gameDescription,videoGame1.getConsoles());

        // THEN
        assertNotNull(videoGameResponse, "The video game is returned");
        assertEquals(gameName, videoGameResponse.getName(), "The video game name matches");
        assertEquals(gameDescription, videoGameResponse.getDescription(), "The video game description matches");
    }


    @Test
    void listAllVideoGames_Test() {
        // GIVEN
        VideoGameRecord record1 = new VideoGameRecord();
        record1.setName("testGame1");
        record1.setDescription("testDescription1");
        record1.setConsoles(new Consoles[]{Consoles.PC, Consoles.GC});

        VideoGameRecord record2 = new VideoGameRecord();
        record2.setName("testGame2");
        record2.setDescription("testDescription2");
        record2.setConsoles(new Consoles[]{Consoles.PS4, Consoles.XONE});

        List<VideoGameRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(videoGameRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<VideoGameResponse> videoGameResponses = new VideoGameService(videoGameRepository).listAllVideoGames();

        // THEN
        assertNotNull(videoGameResponses, "The video game list is returned");
        assertEquals(2, videoGameResponses.size(), "There are two video games");

        for (VideoGameResponse videoGameResponse : videoGameResponses) {
            if (videoGameResponse.getName().equals(record1.getName())) {
                assertEquals(record1.getName(), videoGameResponse.getName(), "The video game name matches");
                assertEquals(record1.getDescription(), videoGameResponse.getDescription(), "The video game description matches");
            } else if (videoGameResponse.getName().equals(record2.getName())) {
                assertEquals(record2.getName(), videoGameResponse.getName(), "The video game name matches");
                assertEquals(record2.getDescription(), videoGameResponse.getDescription(), "The video game description matches");
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
        when(videoGameRepository.findById(videoGameName)).thenReturn(Optional.empty());
        VideoGameRecord videoGame = new VideoGameService(videoGameRepository).findByName(videoGameName);
        // THEN
        Assertions.assertNull(videoGame, "The video game is null when not found");
    }

    @Test
    void findByNameGameNotFound_Test() {
        // GIVEN
        String gameName = "gameName";

        // WHEN
        when(videoGameRepository.findById(gameName)).thenReturn(Optional.empty());
        VideoGameRecord game = new VideoGameService(videoGameRepository).findByName(gameName);

        // THEN
        Assertions.assertNull(game, "The game is null when not found");
    }

    @Test
    void findByNameInvalidGame_Test() {
        // GIVEN
        String gameName = UUID.randomUUID().toString();

        // WHEN
        when(videoGameRepository.findById(gameName)).thenReturn(Optional.empty());
        VideoGameRecord game = new VideoGameService(videoGameRepository).findByName(gameName);

        // THEN
        Assertions.assertNull(game, "The game is null when not found");
    }

    @Test
    void addNewVideoGameSecond_Test() {
        // GIVEN
        String videoGameName = "videoGameName";
        String description = "description";
       // Consoles[] consoles = {Consoles.PS5, Consoles.WII};

        VideoGame videoGame1 = new VideoGame(videoGameName,description,Consoles.PS5,Consoles.WII);

        VideoGameResponse expectedVideoGameResponse = new VideoGameResponse();
        expectedVideoGameResponse.setName(videoGameName);
        expectedVideoGameResponse.setDescription(description);
        expectedVideoGameResponse.setConsoles(videoGame1.getConsoles());
        expectedVideoGameResponse.setUpwardVote(0);
        expectedVideoGameResponse.setDownwardVote(0);
        expectedVideoGameResponse.setTotalVote(0);

        VideoGameRecord expectedVideoGameRecord = new VideoGameRecord();
        expectedVideoGameRecord.setName(videoGameName);
        expectedVideoGameRecord.setDescription(description);
        //expectedVideoGameRecord.setConsoles(consoles);
        expectedVideoGameRecord.setConsoles(videoGame1.getConsoles());

        expectedVideoGameRecord.setUpwardVote(0);
        expectedVideoGameRecord.setDownwardVote(0);
        expectedVideoGameRecord.setVotingPercentage(0);

        ArgumentCaptor<VideoGameRecord> videoGameRecordCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);

        // WHEN
        VideoGameResponse returnedVideoGameResponse = new VideoGameService(videoGameRepository).addNewVideoGame(videoGameName, description,videoGame1.getConsoles());

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
        assertEquals(record.getVotingPercentage(), expectedVideoGameRecord.getVotingPercentage(), "The video game voting percentage matches");

        assertEquals(returnedVideoGameResponse.getName(), expectedVideoGameResponse.getName(), "The returned video game name matches");
        assertEquals(returnedVideoGameResponse.getDescription(), expectedVideoGameResponse.getDescription(), "The returned video game description matches");
        //Assertions.assertArrayEquals(returnedVideoGameResponse.getConsoles(), expectedVideoGameResponse.getConsoles(), "The returned video game consoles match");
        assertEquals(returnedVideoGameResponse.getUpwardVote(), expectedVideoGameResponse.getUpwardVote(), "The returned video game upward vote matches");
        assertEquals(returnedVideoGameResponse.getDownwardVote(), expectedVideoGameResponse.getDownwardVote(), "The returned video game downward vote matches");
        assertEquals(returnedVideoGameResponse.getTotalVote(), expectedVideoGameResponse.getTotalVote(), "The returned video game total vote matches");
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
        VideoGameResponse updatedVideoGameResponse = new VideoGameService(videoGameRepository).updateVideoGameDescription(gameName, newDescription);

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
        Assertions.assertThrows(ResponseStatusException.class, () -> new VideoGameService(videoGameRepository).updateVideoGameDescription(gameName, newDescription));

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
        String result = new VideoGameService(videoGameRepository).deleteVideoGame(gameName);

        // THEN
        assertEquals("Game Not Found. Try Checking The Spelling, And Try Again", result, "The game could not be found");
        verify(videoGameRepository, never()).delete(any());
    }

    @Test
    void addNewVideoGameWithValidRequest_returnsVideoGameResponse_Test() {
        // GIVEN
        String name = "gameName";
        String description = "gameDescription";
       // Consoles[] consoles = {Consoles.WIIU, Consoles.PS5};
        VideoGame videoGame1 = new VideoGame(name,description,Consoles.PS5,Consoles.WIIU);

        // WHEN
        VideoGameResponse response = new VideoGameService(videoGameRepository).addNewVideoGame(name, description, videoGame1.getConsoles());

        // THEN
        assertEquals(name, response.getName(), "Name should match");
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
        VideoGameRecord record1 = new VideoGameRecord();
        record1.setName("Game 1");
        record1.setUpwardVote(50);
        record1.setDownwardVote(20);
        record1.setVotingPercentage(70);
        videoGameRepository.save(record1);

        VideoGameRecord record2 = new VideoGameRecord();
        record2.setName("Game 2");
        record2.setUpwardVote(40);
        record2.setDownwardVote(10);
        record2.setVotingPercentage(80);
        videoGameRepository.save(record2);

        VideoGameRecord record3 = new VideoGameRecord();
        record3.setName("Game 3");
        record3.setUpwardVote(100);
        record3.setDownwardVote(50);
        record3.setVotingPercentage(67);
        videoGameRepository.save(record3);

        VideoGameRecord record4 = new VideoGameRecord();
        record4.setName("Game 4");
        record4.setUpwardVote(80);
        record4.setDownwardVote(30);
        record4.setVotingPercentage(73);
        videoGameRepository.save(record4);

        VideoGameRecord record5 = new VideoGameRecord();
        record5.setName("Game 5");
        record5.setUpwardVote(70);
        record5.setDownwardVote(20);
        record5.setVotingPercentage(78);
        videoGameRepository.save(record5);

        VideoGameRecord record6 = new VideoGameRecord();
        record6.setName("Game 6");
        record6.setUpwardVote(50);
        record6.setDownwardVote(20);
        record6.setVotingPercentage(75);
        videoGameRepository.save(record6);

        List<VideoGameRecord> records = List.of(record1, record2, record3, record4, record5, record6);
        when(videoGameRepository.findAll()).thenReturn(records);

        // WHEN
        List<VideoGameResponse> top5 = new VideoGameService(videoGameRepository).top5RatingLeaderboard();

        // THEN
        // list the top 5 games above with the highest upvotes
        verify(videoGameRepository, times(1)).findAll();
        assertEquals(5, top5.size(), "There should be 5 games in the list");
        assertEquals("Game 3", top5.get(0).getName(), "Game 3 should be the first game in the list");
    }




}

