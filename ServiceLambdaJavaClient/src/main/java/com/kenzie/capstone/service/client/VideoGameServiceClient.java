package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;

import java.util.ArrayList;
import java.util.List;

public class VideoGameServiceClient {
    private static final String ADD_VIDEOGAME_ENDPOINT = "games/";
    private static final String DELETE_VIDEOGAME_ENDPOINT = "games/{name}";
    private static final String GET_VIDEOGAME_ENDPOINT = "games/{name}";
    private static final String GET_ALL_VIDEOGAME_ENDPOINT = "games/all";
    private static final String UPDATE_VIDEOGAME_ENDPOINT = "games/{name}";

    private ObjectMapper mapper;

    public VideoGameServiceClient(){this.mapper = new ObjectMapper();}
    public VideoGameResponse addVideoGame(VideoGameRequest videoGameRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(videoGameRequest);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(ADD_VIDEOGAME_ENDPOINT, request);

        VideoGameResponse videoGameResponse;
        try {
            videoGameResponse = mapper.readValue(response, VideoGameResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return videoGameResponse;
    }

    public boolean deleteVideoGame(String name){
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;

        try {
            request = mapper.writeValueAsString(name);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }

        String response = endpointUtility.postEndpoint(DELETE_VIDEOGAME_ENDPOINT, request);

        boolean outcome;

        try {
            outcome = mapper.readValue(response, Boolean.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return outcome;
    }
    public VideoGameResponse getVideoGame(String name) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_VIDEOGAME_ENDPOINT.replace("{name}", name));
        VideoGameResponse videoGameResponse;
        try {
            videoGameResponse = mapper.readValue(response, VideoGameResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return videoGameResponse;
    }
    public List<VideoGameResponse> getAllVideoGames() {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_ALL_VIDEOGAME_ENDPOINT);
        List<VideoGameResponse> videoGameResponse;
        try {
            videoGameResponse = mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return videoGameResponse;
    }
    public VideoGameResponse updateVideoGame(String name, VideoGameRequest videoGameRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();

        String request;
        try {
            request = mapper.writeValueAsString(videoGameRequest);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }

        String endpoint = UPDATE_VIDEOGAME_ENDPOINT.replace("{name}", name);
        String response = endpointUtility.putEndpoint(endpoint, request);

        VideoGameResponse videoGameResponse;
        try {
            videoGameResponse = mapper.readValue(response, VideoGameResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }

        return videoGameResponse;
    }

}