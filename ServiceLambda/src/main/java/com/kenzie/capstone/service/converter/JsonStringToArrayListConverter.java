package com.kenzie.capstone.service.converter;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kenzie.capstone.service.exceptions.InvalidGameException;

import java.util.ArrayList;

public class JsonStringToArrayListConverter {

    public ArrayList<String> convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            ArrayList<String> list = gson.fromJson(body, new TypeToken<ArrayList<String>>() {}.getType());
            return list;
        } catch (Exception e) {
            throw new InvalidGameException("Body \"" + body + "\" could not be deserialized into an ArrayList<String>");
        }
    }
}

