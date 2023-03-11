package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateVideoGameRequest;
import com.kenzie.appserver.controller.model.VideoGameResponse;
import com.kenzie.appserver.repositories.model.VideoGameRecord;
import com.kenzie.appserver.service.VideoGameService;
import com.kenzie.appserver.service.model.Consoles;
import com.kenzie.appserver.service.model.VideoGame;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
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
    VideoGameService videoGameService;

    private static final ObjectMapper mapper = new ObjectMapper();
    private String name = "NBA 2K23";
    private String description = "Basketball game";
    private VideoGame videoGame = new VideoGame(name,description,Consoles.PS5,Consoles.PC,Consoles.PS4);

    @BeforeAll
    static void setup(){
        mapper.registerModule(new Jdk8Module());

    }

    @AfterEach
    void cleanup() {
        //cleanup the tables so that we have clean data for each test
        try {
            videoGameService.deleteVideoGame(name);
            mvc.perform(delete("/games/{name}", name));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getGameByName_Exists() throws Exception {
//        String name = "NBA 2K23";
//        String description = "Basketball game";
//        VideoGame videoGame = new VideoGame(name,description,Consoles.PS5,Consoles.PC,Consoles.PS4);

        VideoGameResponse gameResponse = videoGameService.addNewVideoGame(videoGame.getName(), videoGame.getDescription(),videoGame.getConsoles());
        mvc.perform(get("/games/{name}", gameResponse.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("Description")
                        .value(is(description)))
                .andExpect(jsonPath("Consoles")
                        .value(hasItems(Consoles.PS5.getName(),Consoles.PC.getName(),Consoles.PS4.getName())))
                .andExpect(status().isOk());
    }

    @Test
    public void createVideoGame_CreateSuccessful() throws Exception {

        CreateVideoGameRequest createVideoGameRequest = new CreateVideoGameRequest();
        createVideoGameRequest.setVideoGameName(videoGame.getName());
        createVideoGameRequest.setDescription(videoGame.getDescription());
        createVideoGameRequest.setConsoles(videoGame.getConsoles());
        videoGameService.addNewVideoGame(videoGame.getName(), videoGame.getDescription(), videoGame.getConsoles());

        MvcResult result = mvc.perform(post("/games/")
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
//
//    @Test
//    public void updateDoctor_PutSuccessful() throws Exception {
//        // GIVEN
//        String doctorId = UUID.randomUUID().toString();
//        String name = mockNeat.strings().valStr();
//        String dob = mockNeat.strings().valStr();
//        boolean isActive = true;
//
//        Doctor doctor = new Doctor(name, dob, doctorId, isActive);
//        DoctorRecord persistedDoctor = videoGameService.addNewDoctor(doctor);
//        Doctor testDoctor = videoGameService.findById(doctorId);
//        String newName = mockNeat.strings().valStr();
//
//
//        DoctorUpdateRequest doctorUpdateRequest = new DoctorUpdateRequest();
//        doctorUpdateRequest.setDoctorId(doctorId);
//        doctorUpdateRequest.setName(newName);
//        doctorUpdateRequest.setDob(dob);
//        doctorUpdateRequest.setActive(isActive);
//
//
//        mapper.registerModule(new JavaTimeModule());
//
//        // WHEN
//        mvc.perform(put("/doctor")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(doctorUpdateRequest)))
//                // THEN
//                .andExpect(jsonPath("doctorId")
//                        .exists())
//                .andExpect(jsonPath("name")
//                        .value(is(newName)))
//                .andExpect(jsonPath("dob")
//                        .value(is(dob)))
//                .andExpect(jsonPath("isActive")
//                        .value(is(true)))
//                .andExpect(status().isOk());
//    }
}
