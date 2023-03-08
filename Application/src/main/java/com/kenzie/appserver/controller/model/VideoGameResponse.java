package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoGameResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("Consoles")
    private List<String> consoles;
    @JsonProperty("Description")
    private String description;

   public List<String> getConsoles(){return consoles;}
    public void setConsoles(List<String> consoles){this.consoles = consoles;}
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
