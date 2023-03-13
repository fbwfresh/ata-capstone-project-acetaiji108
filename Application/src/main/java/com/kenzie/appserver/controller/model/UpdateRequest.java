package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Consoles;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UpdateRequest {
    @NotEmpty
    @JsonProperty("name")
    private String videoGameName;
    @NotEmpty
    @JsonProperty("Description")
    private String Description;
    @NotEmpty
    @JsonProperty("Consoles")
    private List<String> listOfConsoles = new ArrayList<>();
    //TODO map the JSON property and set up getters and setters
    @JsonProperty("UpwardVote")
    private int upwardVote;
    @JsonProperty("DownwardVote")
    private int downwardVote;
    @JsonProperty("TotalVote")
    private int votingPercentage;

    public UpdateRequest(){}

    public String getVideoGameName() {
        return videoGameName;
    }

    public void setVideoGameName(String videoGameName) {
        this.videoGameName = videoGameName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public List<String> getConsoles(){
        return listOfConsoles;
    }
    public void setConsoles(Consoles... consoles){
        for(Consoles console : consoles){
            listOfConsoles.add(console.getName());
        }
    }
    public void setConsoles(List<String> listOfConsoles){
        this.listOfConsoles = listOfConsoles;
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
