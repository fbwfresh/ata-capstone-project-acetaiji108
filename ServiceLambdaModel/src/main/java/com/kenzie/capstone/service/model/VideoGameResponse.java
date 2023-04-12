package com.kenzie.capstone.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class VideoGameResponse {


        private String name;

        private Set<String> consoles;

        private String description;

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

    public Set<String> getConsoles(){return consoles;}

        public void setConsoles(Set<String> consoles){this.consoles = consoles;}
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


