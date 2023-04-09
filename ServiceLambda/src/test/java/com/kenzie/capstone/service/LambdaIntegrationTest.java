package com.kenzie.capstone.service;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.dependency.DaoModule;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        String image = "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg";
        VideoGame game = new VideoGame(gameName, description,image, Consoles.DS, Consoles.GC, Consoles.GBA, Consoles.NS, Consoles.WII, Consoles.WIIU);
        VideoGameRequest request = new VideoGameRequest();
        request.setName(game.getName());
        request.setConsoles(game.getConsoles());
        request.setDescription(game.getDescription());
        request.setUpwardVote(game.getUpwardVote());
        request.setDownwardVote(game.getDownwardVote());
        request.setTotalVote(game.getTotalVote());
        request.setImage(game.getImage());

        // WHEN
        VideoGameResponse response = this.videoGameService.addVideoGame(request);

        // THEN
        VideoGameRecord record = new VideoGameRecord();
        record.setUpwardVote(response.getUpwardVote());
        record.setTotalVote(response.getTotalVote());
        record.setDownwardVote(response.getDownwardVote());
        record.setName(response.getName());
        record.setDescription(response.getDescription());
        record.setConsoles(response.getConsoles());
        record.setImage(response.getImage());
        assertNotNull(record, "The record is valid");
        assertEquals(gameName, record.getName(), "The record name should match");
        assertEquals(description, record.getDescription(), "The record description should match");
        assertNotNull(record.getConsoles(), "The record consoles exist");
        assertNotNull(response, "A response is returned");

        // Print out the added video game information
        System.out.println("Added Video Game:");
        System.out.println("Name: " + response.getName());
        System.out.println("Description: " + response.getDescription());
        System.out.println("Consoles: " + response.getConsoles());
        System.out.println("Image: " + response.getImage());
        System.out.println("Upward Vote: " + response.getUpwardVote());
        System.out.println("Downward Vote: " + response.getDownwardVote());
        System.out.println("Total Vote: " + response.getTotalVote());

        // Delete the game
        boolean result = this.videoGameService.deleteVideoGame(gameName);
        assertTrue(result, "The game was successfully deleted");

    }

    @Test
    void addVideoGameTest_WithInvalidData() {
        // GIVEN
        String gameName = "Halo";
        String description = "Halo is a first-person shooter game.";
        String image = "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg";
        VideoGameRequest request = new VideoGameRequest();
        request.setName(gameName);
        request.setDescription(description);
        request.setImage(image);
        request.setConsoles(new HashSet<>());

        // WHEN
        AmazonDynamoDBException exception = assertThrows(AmazonDynamoDBException.class, () -> videoGameService.addVideoGame(request));
        System.out.println("Exception message: " + exception.getMessage());

        // THEN
        assertTrue(exception.getMessage().contains("An string set  may not be empty"), "The error message should contain the substring");
        System.out.println("Game is invalid");
    }




    @Test
    void getVideoGameTest() throws InvalidGameException {
        // GIVEN
        VideoGameResponse response = videoGameService.getVideoGame("Monster Hunter Rise");


        // WHEN
        System.out.println("Name: " + response.getName());
        System.out.println("Description: " + response.getDescription());
        System.out.println("Consoles: " + response.getConsoles());

        // THEN
        assertNotNull(response, "The response is valid");
        assertEquals("Monster Hunter Rise", response.getName(), "The video game name matches");
        }

    @Test
    void getVideoGame_WithInvalidNameTest() {
        // GIVEN
        String invalidGameName = "Invalid Game Name";

        // WHEN
        System.out.println("Attempting to get an invalid video game...");
        InvalidGameException exception = assertThrows(InvalidGameException.class, () -> videoGameService.getVideoGame(invalidGameName));

        // THEN
        assertEquals("Request must contain a valid video game name", exception.getMessage(), "The error message should match");
        System.out.println("Invalid game");
    }

    @Test
    void GetAllVideoGamesTest() {
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
        assertEquals(50, response.size(), "There are 50 games in the database");
    }

    @Test
    void getAllVideoGamesTest_noGamesFound() {
        // GIVEN
        List<VideoGameRecord> emptyRecords = Collections.emptyList();
        VideoGameDao videoGameDao = new VideoGameDao() {
            @Override
            public VideoGameRecord addVideoGame(VideoGameRecord record) {
                return null;
            }

            @Override
            public boolean deleteVideoGame(VideoGameRecord record) {
                return false;
            }
            @Override
            public VideoGameRecord findByName(String name) {
                return null;
            }
            @Override
            public List<VideoGameRecord> getAllGames() {
                return emptyRecords;
            }
            @Override
            public VideoGameRecord updateVideoGame(VideoGameRecord record) {
                return null;
            }
        };
        VideoGameService videoGameService = new VideoGameService(videoGameDao);

        // WHEN
        List<VideoGameResponse> responseList = videoGameService.getAllVideoGames();

        // THEN
        assertNotNull(responseList, "The response list should not be null");
        assertTrue(responseList.isEmpty(), "The response list should be empty");

        System.out.println("No video games found");
    }

    @Test
    void deleteVideoGameTest() {
        // GIVEN
        String gameNameToDelete = "Contra";
        String description = "Contra is a run-and-gun action platformer, notorious for its high difficulty. " +
                "The player character comes armed with a gun that can shoot infinitely. " +
                "Different weapons with new abilities and that shoot different types of projectiles can be acquired" +
                " as the player progresses through the levels.";
        String image = "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg";
        VideoGame game = new VideoGame(gameNameToDelete, description, image, Consoles.DS, Consoles.GC, Consoles.GBA, Consoles.NS, Consoles.WII, Consoles.WIIU);
        VideoGameRequest request = new VideoGameRequest();
        request.setName(game.getName());
        request.setConsoles(game.getConsoles());
        request.setDescription(game.getDescription());
        request.setUpwardVote(game.getUpwardVote());
        request.setDownwardVote(game.getDownwardVote());
        request.setTotalVote(game.getTotalVote());
        request.setImage(game.getImage());
        VideoGameResponse response = this.videoGameService.addVideoGame(request);

        // WHEN
        boolean isDeleted = this.videoGameService.deleteVideoGame(gameNameToDelete);

        // THEN
        assertTrue(isDeleted, "The game should be deleted successfully");
        System.out.println("Game '" + gameNameToDelete + "' deleted successfully.");

        // Verify that the game is not in the database anymore
        try {
            this.videoGameService.getVideoGame(gameNameToDelete);
        } catch (InvalidGameException e) {
            System.out.println("Game '" + gameNameToDelete + "' is not in the database anymore.");
            return;
        }
        fail("The game should not exist in the database anymore");
    }

    @Test
    void deleteVideoGame_NonExistentGameTest() {
        // GIVEN
        String nonExistentGameName = "Non-existent Game";

        // WHEN
        InvalidGameException exception = assertThrows(InvalidGameException.class, () -> videoGameService.deleteVideoGame(nonExistentGameName));

        // THEN
        assertNotNull(exception, "An exception should be thrown");
        assertEquals("The video game does not exist on any console", exception.getMessage(), "The error message should match");
        System.out.println("Could not delete invalid game: " + nonExistentGameName);
    }


    @Test
    void updateVideoGameTest() throws InvalidGameException {
        // GIVEN
        String gameName = "Test Game";
        String description = "Test Description";
        String console = "Test Console";
        String image = "test-image";
        VideoGameRecord record = new VideoGameRecord();
        record.setName(gameName);
        record.setDescription(description);
        record.setConsoles(Collections.singleton(console));
        record.setImage(image);
        videoGameDao.addVideoGame(record);

        VideoGameRequest request = new VideoGameRequest();
        request.setName(gameName);
        request.setDescription("Updated Test Description");
        request.setConsoles(new HashSet<>(Arrays.asList("Test Console 1", "Test Console 2")));
        request.setImage("test-image-url");
        request.setUpwardVote(100);
        request.setDownwardVote(50);
        request.setTotalVote(150);

        // WHEN
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

        VideoGameRecord updatedRecord = videoGameDao.findByName(gameName);
        assertNotNull(updatedRecord);
        assertEquals(request.getName(), updatedRecord.getName());
        assertEquals(request.getDescription(), updatedRecord.getDescription());
        assertEquals(request.getConsoles(), updatedRecord.getConsoles());
        assertEquals(request.getImage(), updatedRecord.getImage());
        assertEquals(request.getUpwardVote(), updatedRecord.getUpwardVote());
        assertEquals(request.getDownwardVote(), updatedRecord.getDownwardVote());
        assertEquals(request.getTotalVote(), updatedRecord.getTotalVote());

        videoGameDao.deleteVideoGame(updatedRecord);
        assertNull(videoGameDao.findByName(gameName));
    }

    @Test
    void updateVideoGame_NonExistentGameTest() {
        // GIVEN
        String nonExistentGameName = "Non-existent Game";
        VideoGameRequest request = new VideoGameRequest();
        request.setName(nonExistentGameName);

        // WHEN
        InvalidGameException exception = assertThrows(InvalidGameException.class, () -> videoGameService.updateVideoGame(request));

        // THEN
        assertTrue(exception.getMessage().contains("Game with name " + nonExistentGameName + " not found"), "The error message should contain the substring");
        System.out.println("Could not update invalid game: " + nonExistentGameName);
    }
}

