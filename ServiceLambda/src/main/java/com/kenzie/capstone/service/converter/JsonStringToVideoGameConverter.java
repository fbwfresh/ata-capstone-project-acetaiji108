package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.VideoGameRequest;

public class JsonStringToVideoGameConverter {
    public VideoGameRequest convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            VideoGameRequest videoGameRequest = gson.fromJson(body, VideoGameRequest.class);
            return videoGameRequest;
        } catch (Exception e) {
            throw new InvalidGameException("Game could not be deserialized");
        }
    }
}
