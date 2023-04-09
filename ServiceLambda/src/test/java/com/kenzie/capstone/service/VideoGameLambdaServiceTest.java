package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.VideoGameConverter;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.VideoGameRecord;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VideoGameLambdaServiceTest {

    private VideoGameDao videoGameDao;
    private VideoGameService videoGameService;

    @BeforeEach
    void setup() {
        videoGameDao = mock(VideoGameDao.class);
        videoGameService = new VideoGameService(videoGameDao);
    }

    @Test
    void addVideoGameTest() {
        ArgumentCaptor<VideoGameRecord> videoGameCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);

        // GIVEN
        String gameName = "Test Game";
        String gameDescription = "Test Description";
        VideoGameRequest request = new VideoGameRequest();
        request.setName(gameName);
        request.setDescription(gameDescription);

        // WHEN
        VideoGameResponse response = this.videoGameService.addVideoGame(request);

        // THEN
        verify(videoGameDao, times(1)).addVideoGame(videoGameCaptor.capture());
        VideoGameRecord gameRecord = videoGameCaptor.getValue();

        assertNotNull(gameRecord, "The game record is valid");
        assertEquals(gameName, gameRecord.getName(), "The record game name should match");
        assertEquals(gameDescription, gameRecord.getDescription(), "The record game description should match");

        assertNotNull(response, "A response is returned");
        assertEquals(gameName, response.getName(), "The response game name should match");
        assertEquals(gameDescription, response.getDescription(), "The response game description should match");
    }

    @Test
    void addVideoGameTest_nullRequest() {
        // GIVEN
        VideoGameRequest request = null;

        // WHEN + THEN
        assertThrows(InvalidGameException.class, () -> {
            this.videoGameService.addVideoGame(request);
        });
    }


    @Test
    void deleteVideoGameTest() {
        // GIVEN
        VideoGameService videoGameService = new VideoGameService(videoGameDao);

        String videoGameName = "Test Game";
        VideoGameRecord record = new VideoGameRecord();
        record.setName(new String(videoGameName));
        record.setConsoles(new HashSet<>(Collections.singletonList("Test Console")));
        when(videoGameDao.findByName(eq(videoGameName))).thenReturn(record);
        ArgumentCaptor<VideoGameRecord> argumentCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);

        // WHEN
        when(videoGameDao.deleteVideoGame(argumentCaptor.capture())).thenReturn(true);
        boolean result = videoGameService.deleteVideoGame(videoGameName);

        // THEN
        assertTrue(result);
        VideoGameRecord capturedRecord = argumentCaptor.getValue();
        assertEquals(videoGameName, capturedRecord.getName());
        verify(videoGameDao, times(1)).deleteVideoGame(eq(record));
        verify(videoGameDao, times(1)).findByName(eq(videoGameName));
    }

    @Test
    void deleteVideoGameTest_videoGameDoesNotExist() {
        // GIVEN
        String videoGameName = "Non-Existent Game";
        when(videoGameDao.findByName(eq(videoGameName))).thenReturn(null);

        // WHEN + THEN
        assertThrows(InvalidGameException.class, () -> {
            this.videoGameService.deleteVideoGame(videoGameName);
        });
    }

    @Test
    void updateVideoGameTest() {
        // GIVEN
        VideoGameService videoGameService = new VideoGameService(videoGameDao);

        VideoGameRequest request = new VideoGameRequest();
        request.setName("Test Game");
        request.setDescription("Updated Test Description");
        request.setConsoles(new HashSet<>(Arrays.asList("Test Console 1", "Test Console 2")));
        request.setImage("test-image-url");
        request.setUpwardVote(100);
        request.setDownwardVote(50);
        request.setTotalVote(150);

        VideoGameRecord existingRecord = new VideoGameRecord();
        existingRecord.setName("Test Game");
        existingRecord.setDescription("Test Description");
        existingRecord.setConsoles(new HashSet<>(Collections.singletonList("Test Console")));
        existingRecord.setImage("test-image");
        existingRecord.setUpwardVote(50);
        existingRecord.setDownwardVote(25);
        existingRecord.setTotalVote(75);

        when(videoGameDao.findByName(eq(request.getName()))).thenReturn(existingRecord);
        ArgumentCaptor<VideoGameRecord> argumentCaptor = ArgumentCaptor.forClass(VideoGameRecord.class);

        // WHEN
        when(videoGameDao.updateVideoGame(argumentCaptor.capture())).thenReturn(existingRecord);
        VideoGameResponse result = videoGameService.updateVideoGame(request);

        // THEN
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getConsoles(), result.getConsoles());
        assertEquals(request.getImage(), result.getImage());
        assertEquals(request.getUpwardVote(), result.getUpwardVote());
        assertEquals(request.getDownwardVote(), result.getDownwardVote());
        assertEquals(request.getTotalVote(), result.getTotalVote());

        VideoGameRecord capturedRecord = argumentCaptor.getValue();
        assertNotNull(capturedRecord);
        assertEquals(request.getName(), capturedRecord.getName());
        assertEquals(request.getDescription(), capturedRecord.getDescription());
        assertEquals(request.getConsoles(), capturedRecord.getConsoles());
        assertEquals(request.getImage(), capturedRecord.getImage());
        assertEquals(request.getUpwardVote(), capturedRecord.getUpwardVote());
        assertEquals(request.getDownwardVote(), capturedRecord.getDownwardVote());
        assertEquals(request.getTotalVote(), capturedRecord.getTotalVote());

        verify(videoGameDao, times(1)).findByName(eq(request.getName()));
        verify(videoGameDao, times(1)).updateVideoGame(eq(existingRecord));
    }

    @Test
    void updateVideoGameTest_videoGameDoesNotExist() {
        // GIVEN
        String videoGameName = "Non-Existent Game";
        VideoGameRequest request = new VideoGameRequest();
        request.setName(videoGameName);
        request.setDescription("Test Description");

        when(videoGameDao.findByName(eq(videoGameName))).thenReturn(null);

        // WHEN + THEN
        assertThrows(InvalidGameException.class, () -> {
            this.videoGameService.updateVideoGame(request);
        });
    }

    @Test
    void getVideoGameTest() {
        // GIVEN
        VideoGameRecord record = new VideoGameRecord();

        record.setName("Monster Hunter Rise");
        record.setDescription("Action RPG");
        record.setConsoles(Collections.singleton("Nintendo Switch"));
        when(videoGameDao.findByName("Monster Hunter Rise")).thenReturn(record);

        // WHEN
        VideoGameResponse response = videoGameService.getVideoGame("Monster Hunter Rise");

        // THEN
        assertNotNull(response, "The response is valid");
        assertEquals("Monster Hunter Rise", response.getName(), "The video game name matches");
        assertEquals("Action RPG", response.getDescription(), "The video game description matches");
        assertEquals(Collections.singleton("Nintendo Switch"), response.getConsoles(), "The video game consoles match");

        // Print out the response for easier debugging
        System.out.println("Name: " + response.getName());
        System.out.println("Description: " + response.getDescription());
        System.out.println("Consoles: " + response.getConsoles());
    }

    @Test
    void getVideoGameTest_videoGameDoesNotExist() {
        // GIVEN
        String videoGameName = "Non-Existent Game";
        when(videoGameDao.findByName(eq(videoGameName))).thenReturn(null);

        // WHEN + THEN
        assertThrows(InvalidGameException.class, () -> {
            this.videoGameService.getVideoGame(videoGameName);
        });
    }

    @Test
    void getAllVideoGamesTest() {
        // GIVEN
        List<VideoGameRecord> records = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            VideoGameRecord record = new VideoGameRecord();
            record.setName("Game " + i);
            record.setDescription("Description " + i);
            record.setConsoles(Collections.singleton("Console " + i));
            records.add(record);
        }

        when(videoGameDao.getAllGames()).thenReturn(records);

        // WHEN
        List<VideoGameResponse> response = videoGameService.getAllVideoGames();

        // THEN
        assertNotNull(response, "The response is valid");
        assertEquals(50, response.size(), "There are 50 games in the database");

        System.out.println("All video games:");
        for (int i = 1; i <= 50; i++) {
            VideoGameResponse game = response.get(i - 1);
            System.out.println("Name: " + game.getName());
            System.out.println("Description: " + game.getDescription());
            System.out.println("Consoles: " + game.getConsoles());
            System.out.println("------------------------");

            assertEquals("Game " + i, game.getName(), "The video game name matches");
            assertEquals("Description " + i, game.getDescription(), "The video game description matches");
            assertEquals(Collections.singleton("Console " + i), game.getConsoles(), "The video game consoles match");
        }
    }
    @Test
    void getAllVideoGamesTest_empty() {
        // GIVEN
        when(videoGameDao.getAllGames()).thenReturn(Collections.emptyList());

        // WHEN
        List<VideoGameResponse> responseList = this.videoGameService.getAllVideoGames();

        // THEN
        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }
}
