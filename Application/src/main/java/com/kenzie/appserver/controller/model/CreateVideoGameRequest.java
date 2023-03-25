package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.*;

public class CreateVideoGameRequest {

    @NotEmpty
    @JsonProperty("name")
    private String videoGameName;
    @NotEmpty
    @JsonProperty("Description")
    private String description;
    @NotEmpty
    @JsonProperty("Consoles")
    private Set<String> consoles = new HashSet<>();
    //TODO map the JSON property and set up getters and setters
    @JsonProperty("UpwardVote")
    private int upwardVote;
    @JsonProperty("DownwardVote")
    private int downwardVote;
    @JsonProperty("TotalVote")
    private int totalVote;
    @JsonProperty("image")
    private String image;

    public void setImage(String image){this.image = image;}
    public String getImage(){return this.image;}

    public String getVideoGameName() {
        return videoGameName;
    }

    public void setVideoGameName(String videoGameName) {
            this.videoGameName = videoGameName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getConsoles(){
        return consoles;
    }

    public void setConsoles(Set<String> listOfConsoles){
        this.consoles = listOfConsoles;
    }

    public int getUpwardVote() {
        return upwardVote;
    }

    public void setUpwardVote(int upwardVote) {
        this.upwardVote = upwardVote;
    }

    public int getDownwardVote() {
        return downwardVote;
    }

    public void setDownwardVote(int downwardVote) {
        this.downwardVote = downwardVote;
    }

    public int getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(int totalVote) {
        this.totalVote = totalVote;
    }
}
