package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.NonCachingVideoGameDao;
import com.kenzie.capstone.service.dao.VideoGameDao;
import com.kenzie.capstone.service.dependency.DaoModule;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    }
    @Test
    void deleteGameTest() {
        String gameName = "Contra";
        assertTrue(videoGameService.deleteVideoGame(gameName));
    }

    @Test
    void getVideoGameTest() throws InvalidGameException {
        // GIVEN
    String gameName = "Persona 5";

        // WHEN
        VideoGameResponse response = videoGameService.getVideoGame(gameName);



        // THEN
        //verify(videoGameDao, times(1)).findByName(request.getName());

        assertNotNull(response, "The returned response is valid");
        assertEquals("Persona 5", response.getName(), "The video game name matches");
//        assertEquals(request.getDescription(), response.getDescription(), "The video game description matches");
//        assertEquals(record.getConsoles(), response.getConsoles(), "The video game consoles match");
    }
}
