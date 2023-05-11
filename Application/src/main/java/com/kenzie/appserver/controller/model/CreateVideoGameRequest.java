package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.*;

public class CreateVideoGameRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;
    @NotEmpty
    @JsonProperty("Description")
    private String description;
    @NotEmpty
    @JsonProperty("Consoles")
    private String consoles;
    //TODO map the JSON property and set up getters and setters
    @JsonProperty("UpwardVote")
    private int upwardVote;
    @JsonProperty("DownwardVote")
    private int downwardVote;
    @JsonProperty("TotalVote")
    private int totalVote;
    @NotEmpty
    @JsonProperty("image")
    private String image;
    public CreateVideoGameRequest(){}
    public CreateVideoGameRequest(String name, String description, String image,String consoles){
        this.name = name;
        this.description = description;
        this.image =image;
        this.consoles = consoles;
        this.upwardVote = 0;
        this.downwardVote = 0;
        this.totalVote = 0;
    }
    public void setImage(String image){this.image = image;}
    public String getImage(){return this.image;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
