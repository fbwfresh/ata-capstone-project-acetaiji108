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
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private ExampleDao exampleDao;
    private LambdaService lambdaService;
    private VideoGameService videoGameService;
    private VideoGameDao videoGameDao;

    @BeforeAll
    void setup() {
        DaoModule module = new DaoModule();
        this.videoGameDao = new NonCachingVideoGameDao(module.provideDynamoDBMapper());
        this.videoGameService = new VideoGameService(videoGameDao);
//        this.exampleDao = mock(ExampleDao.class);
//        this.lambdaService = new LambdaService(exampleDao);
    }
//    @AfterAll
//    void afterEach(){
//        this.videoGameDao.deleteVideoGame()
//    }
    @Test
    void addVideoGameTest() {
        // GIVEN
        String gameName = "Contra2";
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
//TODO make a delete method in the videoGameService for the lambda
       assertTrue(videoGameService.deleteVideoGame(response.getName()));
    }
//    @Test
//    void setDataTest() {
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);
//
//        // GIVEN
//        String data = "somedata";
//
//        // WHEN
//        ExampleData response = this.lambdaService.setExampleData(data);
//
//        // THEN
//        verify(exampleDao, times(1)).setExampleData(idCaptor.capture(), dataCaptor.capture());
//
//        assertNotNull(idCaptor.getValue(), "An ID is generated");
//        assertEquals(data, dataCaptor.getValue(), "The data is saved");
//
//        assertNotNull(response, "A response is returned");
//        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
//        assertEquals(data, response.getData(), "The response data should match");
//    }
//
//    @Test
//    void getDataTest() {
//        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
//
//        // GIVEN
//        String id = "fakeid";
//        String data = "somedata";
//        ExampleRecord record = new ExampleRecord();
//        record.setId(id);
//        record.setData(data);
//
//
//        when(exampleDao.getExampleData(id)).thenReturn(Arrays.asList(record));
//
//        // WHEN
//        ExampleData response = this.lambdaService.getExampleData(id);
//
//        // THEN
//        verify(exampleDao, times(1)).getExampleData(idCaptor.capture());
//
//        assertEquals(id, idCaptor.getValue(), "The correct id is used");
//
//        assertNotNull(response, "A response is returned");
//        assertEquals(id, response.getId(), "The response id should match");
//        assertEquals(data, response.getData(), "The response data should match");
//    }

    // Write additional tests here

}