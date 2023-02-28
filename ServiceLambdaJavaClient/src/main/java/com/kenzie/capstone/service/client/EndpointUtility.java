package com.kenzie.capstone.service.client;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.GetRestApisRequest;
import com.amazonaws.services.apigateway.model.GetRestApisResult;
import com.amazonaws.services.apigateway.model.RestApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EndpointUtility {
    private String apiEndpoint;

    public EndpointUtility() {
        this.apiEndpoint = getApiEndpint();
    }

    public static String getStackName() {
        String deploymentName = System.getenv("CAPSTONE_SERVICE_STACK_DEV");
        if (deploymentName == null) {
            deploymentName = System.getenv("SERVICE_STACK_NAME");
        }
        if (deploymentName == null) {
            deploymentName = System.getenv("STACK_NAME");
        }
        if (deploymentName == null) {
            throw new IllegalArgumentException("Could not find the deployment name in environment variables.  Make sure that you have set up your environment variables using the setupEnvironment.sh script.");
        }
        return deploymentName;
    }

    public static String getApiEndpint() {
        String region = System.getenv("AWS_REGION");
        if (region == null) {
            region = "us-east-1";
        }

        String deploymentName = getStackName();

        AmazonApiGateway apiGatewayClient = AmazonApiGatewayClientBuilder.defaultClient();
        GetRestApisRequest request = new GetRestApisRequest();
        request.setLimit(500);
        GetRestApisResult result = apiGatewayClient.getRestApis(request);

        String endpointId = null;
        for (RestApi restApi : result.getItems()) {
            if (restApi.getName().equals(deploymentName)) {
                endpointId = restApi.getId();
                break;
            }
        }
        if (endpointId == null) {
            throw new IllegalArgumentException("Could not locate the API Gateway endpoint.  Make sure that your API is deployed and that your AWS credentials are valid.");
        }

        return "https://" + endpointId + ".execute-api." + region + ".amazonaws.com/Prod/";
    }

    public String postEndpoint(String endpoint, String data) {
        String api = getApiEndpint();
        String url = api + endpoint;

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                return httpResponse.body();
            } else {
                throw new ApiGatewayException("GET request failed: " + statusCode + " status code received");
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }

    public String getEndpoint(String endpoint) {
        String api = getApiEndpint();
        String url = api + endpoint;

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                return httpResponse.body();
            } else {
                throw new ApiGatewayException("GET request failed: " + statusCode + " status code received");
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }
}
