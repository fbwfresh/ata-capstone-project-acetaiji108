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
//    private VideoGameService videoGameService;
//    private VideoGameDao videoGameDao;

    @BeforeAll
    void setup() {
        DaoModule module = new DaoModule();
        this.exampleDao = mock(ExampleDao.class);
        this.lambdaService = new LambdaService(exampleDao);
    }
//    @AfterAll
//    void afterEach(){
//        this.videoGameDao.deleteVideoGame()
//    }


        @Test
    void setDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        ExampleData response = this.lambdaService.setExampleData(data);

        // THEN
        verify(exampleDao, times(1)).setExampleData(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        ExampleRecord record = new ExampleRecord();
        record.setId(id);
        record.setData(data);


        when(exampleDao.getExampleData(id)).thenReturn(Arrays.asList(record));

        // WHEN
        ExampleData response = this.lambdaService.getExampleData(id);

        // THEN
        verify(exampleDao, times(1)).getExampleData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    // Write additional tests here

}