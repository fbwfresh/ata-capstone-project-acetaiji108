package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

public class UpdateRequest {
    @NotEmpty
    @JsonProperty("name")
    private String name;
    @NotEmpty
    @JsonProperty("Description")
    private String description;
    @NotEmpty
    @JsonProperty("Consoles")
    private String consoles;
    @JsonProperty("UpwardVote")
    private int upwardVote;
    @JsonProperty("DownwardVote")
    private int downwardVote;
    @JsonProperty("TotalVote")
    private int totalVote;
    @JsonProperty("image")
    private String image;

    public UpdateRequest(){}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public String getConsoles(){
        return consoles;
    }

    public void setConsoles(String listOfConsoles){
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
