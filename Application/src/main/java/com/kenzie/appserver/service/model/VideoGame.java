package com.kenzie.appserver.service.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VideoGame {
    private String name;
    private String description;
    private String consoles;
    private int upwardVote;
    private int downwardVote;
    private int totalVote;
    private String image;
    public VideoGame(String name, String description, String image,String listOfConsoles){
        this.name = name;
        this.description = description;
        this.image = image;
        this.consoles = listOfConsoles;
        this.upwardVote = 0;
        this.downwardVote = 0;
        this.totalVote = 0;
    }
    public VideoGame(String name, String description, String image, String consoles, int upwardVote, int downwardVote, int totalVote){
        this.image = image;
        this.name = name;
        this.description = description;
        this.consoles = consoles;
        this.upwardVote = upwardVote;
        this.downwardVote = downwardVote;
        this.totalVote = totalVote;
    }

    public void setUpwardVote(int upwardVote) {
        this.upwardVote = upwardVote;
    }

    public void setDownwardVote(int downwardVote) {
        this.downwardVote = downwardVote;
    }

    public void setTotalVote(int totalVote) {
        this.totalVote = totalVote;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUpwardVote() {
        return upwardVote;
    }

    public int getDownwardVote() {
        return downwardVote;
    }

    public int getTotalVote() {
        return totalVote;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConsoles() {
        return consoles;
    }

    public void setConsoles(String consoles) {
        this.consoles = consoles;
    }
//    public static void main(String[] args) {
//        VideoGame videoGame = new VideoGame("cod","shooter",Consoles.PS5,Consoles.DS,Consoles.GC,Consoles.AND);
//        System.out.println(videoGame.consoles);
//    }

}