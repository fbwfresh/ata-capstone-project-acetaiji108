package com.kenzie.capstone.service.model;

import java.util.Set;

public class VideoGameRequest {
    private String name;
    private String description;
    private String consoles;
    private int upwardVote;
    private int downwardVote;
    private int totalVote;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
    //    public VideoGameRequest(String name, String description, Set<String> consoles){
//        this.name = name;
//        this.description = description;
//        this.consoles = consoles;
//    }

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

    public String getConsoles() {
        return consoles;
    }

    public void setConsoles(String consoles) {
        this.consoles = consoles;
    }
}
