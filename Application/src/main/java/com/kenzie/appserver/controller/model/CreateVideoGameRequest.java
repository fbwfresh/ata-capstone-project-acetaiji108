package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Consoles;

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
    private int votingPercentage;
    public CreateVideoGameRequest(){}

    public String getVideoGameName() {
        return videoGameName;
    }

    public void setVideoGameName(String videoGameName) {
        //This is how I am able to encode the uri
//        if(videoGameName.contains(" ")){
//         this.videoGameName =  videoGameName.replaceAll(" ","-");
      //  }else {
            this.videoGameName = videoGameName;
       // }
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

    public int getVotingPercentage() {
        return votingPercentage;
    }

    public void setVotingPercentage(int votingPercentage) {
        this.votingPercentage = votingPercentage;
    }
}
