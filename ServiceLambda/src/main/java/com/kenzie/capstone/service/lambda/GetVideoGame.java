package com.kenzie.capstone.service.lambda;

import com.kenzie.capstone.service.VideoGameService;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.exceptions.InvalidGameException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GetVideoGame implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // Logging the request json to make debugging easier.
        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        VideoGameService videoGameService = serviceComponent.provideVideoGameService();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String videoGameId = input.getPathParameters().get("videoGameId");

        if (videoGameId == null || videoGameId.length() == 0) {
            return response
                    .withStatusCode(400)
                    .withBody("Video Game is invalid");
        }

        try {
            String output = gson.toJson(videoGameService.getVideoGame(videoGameId));
            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (InvalidGameException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        }
    }
}
