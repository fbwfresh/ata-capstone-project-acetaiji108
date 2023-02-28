package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ExampleRepository;
import com.kenzie.appserver.repositories.model.ExampleRecord;
import com.kenzie.appserver.service.model.Example;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExampleServiceTest {
    private ExampleRepository exampleRepository;
    private ExampleService exampleService;
    private LambdaServiceClient lambdaServiceClient;

    @BeforeEach
    void setup() {
        exampleRepository = mock(ExampleRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        exampleService = new ExampleService(exampleRepository, lambdaServiceClient);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        String id = randomUUID().toString();

        ExampleRecord record = new ExampleRecord();
        record.setId(id);
        record.setName("concertname");

        // WHEN
        when(exampleRepository.findById(id)).thenReturn(Optional.of(record));
        Example example = exampleService.findById(id);

        // THEN
        Assertions.assertNotNull(example, "The object is returned");
        Assertions.assertEquals(record.getId(), example.getId(), "The id matches");
        Assertions.assertEquals(record.getName(), example.getName(), "The name matches");
    }

    @Test
    void findByConcertId_invalid() {
        // GIVEN
        String id = randomUUID().toString();

        when(exampleRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Example example = exampleService.findById(id);

        // THEN
        Assertions.assertNull(example, "The example is null when not found");
    }

}
