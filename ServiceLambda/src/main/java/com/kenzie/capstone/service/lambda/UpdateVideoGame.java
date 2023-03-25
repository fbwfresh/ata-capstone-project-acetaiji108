package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.VideoGameService;
import com.kenzie.capstone.service.converter.JsonStringToVideoGameConverter;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.exceptions.InvalidGameException;
import com.kenzie.capstone.service.model.VideoGameRequest;
import com.kenzie.capstone.service.model.VideoGameResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateVideoGame implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        JsonStringToVideoGameConverter jsonStringToVideoGameConverter = new JsonStringToVideoGameConverter();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // Logging the request json to make debugging easier.
        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        VideoGameService videoGameService = serviceComponent.provideVideoGameService();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            VideoGameRequest videoGameRequest = jsonStringToVideoGameConverter.convert(input.getBody());
            VideoGameResponse videoGameResponse = videoGameService.updateVideoGame(videoGameRequest);
            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(videoGameResponse));
        } catch (InvalidGameException e) {
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        }

    }
}

