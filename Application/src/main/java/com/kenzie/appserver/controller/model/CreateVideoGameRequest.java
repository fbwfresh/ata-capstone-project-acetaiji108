package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class CreateVideoGameRequest {

    @NotEmpty
    @JsonProperty("VideoGameName")
    private String videoGameName;

    @JsonProperty("VideoGameId")
    private Optional<String> videoGameId;

    public CreateVideoGameRequest() {}

    public String getVideoGameName() {
        return videoGameName;
    }

    public void setVideoGameName(String videoGameName) {
        this.videoGameName = videoGameName;
    }

    public Optional<String> getVideoGameId() {
        return videoGameId;
    }

    public void setVideoGameId(Optional<String> videoGameId) {
        this.videoGameId = videoGameId;
    }
}
