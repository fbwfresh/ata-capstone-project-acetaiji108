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

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
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
    private VideoGame videoGame = new VideoGame(name,description,
            "https://assets.2k.com/1a6ngf98576c/2RNTmC7iLr6YVlxBSmE4M3/11177cffa2bdbedb226b089c4108726a/NBA23-WEBSITE-PRE_ORDER-HOMPAGE-MODULE2-RETAIL_CAROUSEL-CROSSGEN_EDITION-425x535.jpg",
            Consoles.PS5,Consoles.PC,Consoles.PS4);
    private VideoGameController controller = new VideoGameController(videoGameService);


    @BeforeAll
    static void setup(){
        mapper.registerModule(new Jdk8Module());
    }

    @AfterEach
    void cleanup() {
        //cleanup the tables so that we have clean data for each test
        //throws an exception because we updated the method to use the client and had it return a boolean if the
        //client deletes it from its DB and since this game isnt added to the database it isnt deleted
        try {
            //controller.deleteGame(name);
            videoGameService.deleteVideoGame(name);
            mvc.perform(delete("/games/{name}", name));
        }catch (Exception e){
            System.out.println("Exception thrown during cleanup");
        }
    }

    //this test fails because i am no longer using the repository for the method if I commented out the client then it will pass
    @Test
    public void getGameByName_Exists() throws Exception {
        //GIVEN
        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());
        createVideoGameRequest.setImage(videoGame.getImage());
        videoGameService.addNewVideoGame(createVideoGameRequest);

//       VideoGameResponse response = controller.getGameByName(createVideoGameRequest.getVideoGameName()).getBody();
//       assertEquals(response.getName(),createVideoGameRequest.getVideoGameName());
        mvc.perform(get("/games/{name}",createVideoGameRequest.getVideoGameName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name")
                        .value(is(createVideoGameRequest.getVideoGameName())))
                .andExpect(jsonPath("Description")
                        .value(is(description)))
                .andExpect(jsonPath("Consoles")
                        .value(hasItems(Consoles.PS5.getName(),Consoles.PC.getName(),Consoles.PS4.getName())))
                .andExpect(jsonPath("image")
                        .value(is(videoGame.getImage())))
                .andExpect(status().isOk());

    }

    @Test
    public void createVideoGame_CreateSuccessful() throws Exception {

        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());
        createVideoGameRequest.setImage(videoGame.getImage());
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
        assertThat(response.getImage()).isEqualTo(videoGame.getImage());

    }
    //this test fails because i am no longer using the repository for the method if I commented out the client then it will pass
    @Test
    public void updateConsoles_PutSuccessful() throws Exception {
        // GIVEN
        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());
        createVideoGameRequest.setImage(videoGame.getImage());

        VideoGameResponse persistedGame = videoGameService.addNewVideoGame(createVideoGameRequest);
        VideoGameRecord gameRecord = videoGameService.findByName(persistedGame.getName());
       // String newName = mockNeat.strings().valStr();
       Set<String> consolesList = new HashSet<>();
       consolesList.add(Consoles.IOS.getName());
       consolesList.add(Consoles.AND.getName());



        UpdateRequest gameUpdateRequest = new UpdateRequest();
        gameUpdateRequest.setName(gameRecord.getName());
        gameUpdateRequest.setDescription(gameRecord.getDescription());
        gameUpdateRequest.setConsoles(consolesList);
        gameUpdateRequest.setImage(gameRecord.getImage());

        mapper.registerModule(new JavaTimeModule());
        videoGameService.updateVideoGame(gameUpdateRequest.getName(),gameUpdateRequest);

        // WHEN
        mvc.perform(put("/games/{name}",gameUpdateRequest.getName())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(gameUpdateRequest)))
                .andExpect(status().isOk())
                        // THEN
                .andExpect(jsonPath("name")
                        .exists())
                .andExpect(jsonPath("Description")
                        .value(is(description)))
                .andExpect(jsonPath("image")
                        .value(gameRecord.getImage()))
                .andExpect(jsonPath("Consoles")
                        .value(hasItems(Consoles.IOS.getName(),Consoles.AND.getName())));
    }
}
