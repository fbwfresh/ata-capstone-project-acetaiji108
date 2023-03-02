package com.kenzie.appserver.service.model;


public class VideoGame {
    private String name;
    private String description;
    private Enum[] consoles;
    public VideoGame(String name, String description, Enum... consoles){
        this.name = name;
        this.description = description;
        this.consoles = consoles;
    }

//    public static void main(String[] args) {
//        VideoGame videoGame = new VideoGame("cod","shooter",Consoles.PS5,Consoles.DS,Consoles.GC,Consoles.AND);
//        System.out.println(videoGame.consoles.length);
//    }

}