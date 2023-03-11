package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.service.model.Consoles;

import javax.validation.constraints.NotEmpty;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateVideoGameRequest {

    @NotEmpty
    @JsonProperty("name")
    private String videoGameName;
    @NotEmpty
    @JsonProperty("Description")
    private String Description;
    @NotEmpty
    @JsonProperty("Consoles")
    private List<String> listOfConsoles = new ArrayList<>();
    public CreateVideoGameRequest(){}

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
}
