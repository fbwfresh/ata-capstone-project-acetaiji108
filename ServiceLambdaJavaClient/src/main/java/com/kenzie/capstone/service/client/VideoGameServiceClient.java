package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;

public class VideoGameServiceClient {
    private static final String ADD_VIDEOGAME_ENDPOINT = "games/";
  //  private static final String SET_EXAMPLE_ENDPOINT = "example";
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

}
