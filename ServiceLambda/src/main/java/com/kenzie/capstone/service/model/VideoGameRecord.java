package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


import java.util.Objects;
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
    public int getTotalVote(){
        return totalVote;
    }
    @DynamoDBAttribute(attributeName = "Image")
    public String getImage(){return image;}

    public void setImage(String image){this.image = image;}

    public void setUpwardVote(int upwardVote) {
        this.upwardVote = upwardVote;
    }

    public void setDownwardVote(int downwardVote) {
        this.downwardVote = downwardVote;
    }

    public void setTotalVote(int totalVote) {
        this.totalVote = totalVote;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGameRecord that = (VideoGameRecord) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getImage(), that.getImage()) &&
                Objects.equals(getConsoles(), that.getConsoles());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getImage(), getConsoles());
    }


//    public void setConsoles(Consoles[] consoles) {
//        this.consoles = new HashSet<>();
//        for (Consoles console : consoles) {
//            this.consoles.add(console.getName());
//        }
//   }
}