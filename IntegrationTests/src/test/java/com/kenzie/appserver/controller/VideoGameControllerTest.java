package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateVideoGameRequest;
import com.kenzie.appserver.controller.model.UpdateRequest;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.VideoGameService;
import com.kenzie.appserver.service.model.Consoles;
import com.kenzie.appserver.service.model.VideoGame;
import net.andreinc.mockneat.MockNeat;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@IntegrationTest
public class VideoGameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private VideoGameService videoGameService;

    private static final ObjectMapper mapper = new ObjectMapper();
    private String name = "NBA 2K23";
    private String description = "Basketball game";
    private VideoGame videoGame = new VideoGame(name,description,Consoles.PS5,Consoles.PC,Consoles.PS4);
    private VideoGameController controller = new VideoGameController(videoGameService);


    @BeforeAll
    static void setup(){
        mapper.registerModule(new Jdk8Module());
    }

    @AfterEach
    void cleanup() {
        //cleanup the tables so that we have clean data for each test
        try {
            //controller.deleteGame(name);
            videoGameService.deleteVideoGame(name);
            mvc.perform(delete("/games/{name}", name));
        }catch (Exception e){
            System.out.println("Exception thrown during cleanup");
        }
    }

    @Test
    public void getGameByName_Exists() throws Exception {
        //GIVEN
        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());
        videoGameService.addNewVideoGame(createVideoGameRequest);

        mvc.perform(get("/games/{name}",createVideoGameRequest.getVideoGameName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name")
                        .value(is(createVideoGameRequest.getVideoGameName())))
                .andExpect(jsonPath("Description")
                        .value(is(description)))
                .andExpect(jsonPath("Consoles")
                        .value(hasItems(Consoles.PS5.getName(),Consoles.PC.getName(),Consoles.PS4.getName())))
                .andExpect(status().isOk());
//        VideoGameController controller = new VideoGameController(videoGameService);
//        ResponseEntity<VideoGameResponse> postResponse = controller.addGame(createVideoGameRequest);
//        ResponseEntity<VideoGameResponse> getResponse = controller.getGameByName(createVideoGameRequest.getVideoGameName());
//        Assert.assertEquals(postResponse.getBody().getName(),getResponse.getBody().getName());
//        Assert.assertEquals(postResponse.getBody().getDescription(),getResponse.getBody().getDescription());
//        Assert.assertEquals(postResponse.getBody().getConsoles(),getResponse.getBody().getConsoles());
//        Assert.assertEquals(getResponse.getStatusCodeValue(),200);
//         mvc.perform(post("/games")
//                        .content(mapper.writeValueAsString(createVideoGameRequest))
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
        //WHEN
//

    }

    @Test
    public void createVideoGame_CreateSuccessful() throws Exception {

        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());
        videoGameService.addNewVideoGame(createVideoGameRequest);

        MvcResult result = mvc.perform(post("/games")
                        .content(mapper.writeValueAsString(createVideoGameRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        VideoGameResponse response = mapper.readValue(result.getResponse().getContentAsString(), VideoGameResponse.class);

        assertThat(response.getName()).isNotEmpty().as("The Name is populated");
        assertThat(response.getName()).isEqualTo(createVideoGameRequest.getVideoGameName()).as("The name is correct");
        assertThat(response.getDescription()).isNotEmpty().as("The Description is populated");
        assertThat(response.getDescription()).isEqualTo(createVideoGameRequest.getDescription());
        assertThat(response.getConsoles()).isNotEmpty().as("Consoles are populated");
        assertThat(response.getConsoles()).isEqualTo(createVideoGameRequest.getConsoles());

//
//        VideoGameResponse response = mvc.perform(post("/games/")
//                        .content(mapper.writeValueAsString(createVideoGameRequest))
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("name").isNotEmpty())
//                .andExpect(jsonPath("name").value(createVideoGameRequest.getVideoGameName()))
//                .andExpect(jsonPath("description").isNotEmpty())
//                .andExpect(jsonPath("description").value(createVideoGameRequest.getDescription()))
//                .andExpect(jsonPath("consoles").isNotEmpty())
//                .andExpect(jsonPath("consoles").value(createVideoGameRequest.getConsoles()))
//                .andReturn()
//                .getResponse()
//                .getContentAsString()
//                .map(response -> mapper.readValue(response, VideoGameResponse.class))
//                .orElseThrow();

//        ResultActions actions = mvc.perform(post("/games/")
//                        .content(mapper.writeValueAsString(createVideoGameRequest))
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful());
//
//        String responseBody = actions.andReturn().getResponse().getContentAsString();
//        VideoGameResponse response = mapper.readValue(responseBody, VideoGameResponse.class);
//        assertThat(response.getName()).isNotEmpty().as("The Name is populated");
//        assertThat(response.getName()).isEqualTo(createVideoGameRequest.getVideoGameName()).as("The name is correct");
//        assertThat(response.getDescription()).isNotEmpty().as("The Description is populated");
//        assertThat(response.getDescription()).isEqualTo(createVideoGameRequest.getDescription());
//        assertThat(response.getConsoles()).isNotEmpty().as("Consoles are populated");
//        assertThat(response.getConsoles()).isEqualTo(createVideoGameRequest.getConsoles());

//        mvc.perform(post("/games/")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(createVideoGameRequest)))
//                .andExpect(jsonPath("name")
//                        .exists())
//                .andExpect(jsonPath("Description")
//                        .exists())
//                .andExpect(jsonPath("Consoles")
//                        .exists())
//                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void updateConsoles_PutSuccessful() throws Exception {
        // GIVEN
        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());

        VideoGameResponse persistedGame = videoGameService.addNewVideoGame(createVideoGameRequest);
        VideoGameRecord gameRecord = videoGameService.findByName(persistedGame.getName());
       // String newName = mockNeat.strings().valStr();
       Set<String> consolesList = new HashSet<>();
       consolesList.add(Consoles.IOS.getName());
       consolesList.add(Consoles.AND.getName());



        UpdateRequest gameUpdateRequest = new UpdateRequest();
        gameUpdateRequest.setVideoGameName(gameRecord.getName());
        gameUpdateRequest.setDescription(gameRecord.getDescription());
        gameUpdateRequest.setConsoles(consolesList);

//            return mvc.perform(put("/api/v1/reviewMyTeacher/teacher/{teacherName}", updateRequest.getTeacherName())
//                    .accept(MediaType.APPLICATION_JSON)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(mapper.writeValueAsString(updateRequest)));



        mapper.registerModule(new JavaTimeModule());
        videoGameService.updateVideoGame(gameUpdateRequest);

        // WHEN
        mvc.perform(put("/games/{name}",gameUpdateRequest.getVideoGameName())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(gameUpdateRequest)))
                .andExpect(status().isOk())
                        // THEN
                .andExpect(jsonPath("name")
                        .exists())
                .andExpect(jsonPath("Description")
                        .value(is(description)))
                .andExpect(jsonPath("Consoles")
                        .value(hasItems(Consoles.IOS.getName(),Consoles.AND.getName())));
    }
}