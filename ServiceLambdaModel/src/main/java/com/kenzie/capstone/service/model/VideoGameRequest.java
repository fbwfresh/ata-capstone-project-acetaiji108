package com.kenzie.capstone.service.model;

import java.util.Set;

public class VideoGameRequest {
    private String name;
    private String description;
    private Set<String> consoles;
    private int upwardVote;
    private int downwardVote;
    private int votingPercentage;

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

    public Set<String> getConsoles() {
        return consoles;
    }

    public void setConsoles(Set<String> consoles) {
        this.consoles = consoles;
    }
}