package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.service.model.Consoles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DynamoDBTable(tableName = "VideoGames")
public class VideoGameRecord {
    private String name;
    private String description;
    private Set<String> consoles;
    private int upwardVote;
    private int downwardVote;
    private int totalVote;
    private String image;

    @DynamoDBHashKey(attributeName = "name")
    public String getName() {
        return name;
    }
    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }
    @DynamoDBAttribute(attributeName = "Consoles")
    public Set<String> getConsoles() {
        return consoles;
    }
    @DynamoDBAttribute(attributeName = "DownwardVote")
    public int getDownwardVote(){
        return downwardVote;
    }
    @DynamoDBAttribute(attributeName = "UpwardVote")
    public int getUpwardVote(){
        return upwardVote;
    }
    @DynamoDBAttribute(attributeName = "TotalVote")
    public int getVotingPercentage(){
        return totalVote;
    }
    @DynamoDBAttribute(attributeName = "image")
    public String getImage(){return this.image;}
    public void setImage(String image){this.image = image;}

    public void setUpwardVote(int upwardVote) {
        this.upwardVote = upwardVote;
    }

    public void setDownwardVote(int downwardVote) {
        this.downwardVote = downwardVote;
    }

    public void setVotingPercentage(int votingPercentage) {
        this.totalVote = votingPercentage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConsoles(Set<String> consoles) {
        this.consoles = consoles;

    }

    public void setConsoles(Consoles[] consoles) {
        this.consoles = new HashSet<>();
        for (Consoles console : consoles) {
            this.consoles.add(console.getName());
        }

    }
}