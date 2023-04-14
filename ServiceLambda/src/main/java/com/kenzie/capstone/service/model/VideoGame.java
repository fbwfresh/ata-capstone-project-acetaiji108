package com.kenzie.capstone.service.model;


import java.util.HashSet;
import java.util.Set;

public class VideoGame {
    private String name;
    private String description;
    private Set<String> consoles  = new HashSet<>();
    private int upwardVote = 0;
    private int downwardVote = 0;
    private int totalVote = 0;
    private String image;

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

    public VideoGame(String name, String description, String image, Consoles... listOfConsoles){
        this.name = name;
        this.image = image;
        this.description = description;
        for (Consoles console : listOfConsoles){
            this.consoles.add(console.getName());
        }
    }
    public VideoGame(String name, String description,String image, Set<String> consoles){
        this.image = image;
        this.name = name;
        this.description = description;
        this.consoles = consoles;
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

    public Set<String> getConsoles() {
        return consoles;
    }

    public void setConsoles(Set<String> consoles) {
        this.consoles = consoles;
    }
//    public static void main(String[] args) {
//        VideoGame videoGame = new VideoGame("cod","shooter",Consoles.PS5,Consoles.DS,Consoles.GC,Consoles.AND);
//        System.out.println(videoGame.consoles);
//    }

}