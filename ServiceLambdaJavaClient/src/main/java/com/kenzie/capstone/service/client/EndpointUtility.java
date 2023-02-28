package com.kenzie.capstone.service.client;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.GetRestApisRequest;
import com.amazonaws.services.apigateway.model.GetRestApisResult;
import com.amazonaws.services.apigateway.model.RestApi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EndpointUtility {
    private String apiEndpoint;

    public EndpointUtility() {
        this.apiEndpoint = getApiEndpint();
    }
    
    /*
     * Returns input string with environment variable references expanded, e.g. $SOME_VAR or ${SOME_VAR}
     *
     * Gratefully adapted from Tim Lewis's answer on
     * https://stackoverflow.com/questions/2263929/regarding-application-properties-file-and-environment-variable
     */
    public static String resolveEnvVars(Map<String,String> envVars, String input) {
        if (null == input) {
            return null;
        }
        // match ${ENV_VAR_NAME} or $ENV_VAR_NAME
        Pattern p = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");
        Matcher m = p.matcher(input); // get a matcher object
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String envVarName = null == m.group(1) ? m.group(2) : m.group(1);
            String envVarValue = envVars.get(envVarName);
            if (envVarValue == null) {
                throw new RuntimeException("Environment variable " + envVarName +
                        " expected but not found or is null. Please set variable to a non-null value.");
            }
            //how the shell works natively
            //m.appendReplacement(sb, null == envVarValue ? "" : envVarValue);
            m.appendReplacement(sb,envVarValue);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /*
     * Reads a line expecting an environment variable and resolves it if found.
     * If no environment variable is found, the line is skipped
     */
    public static Map<String,String> computeEnvVar(Map<String,String> envVars, String line){
        String[] parts = line.trim().split("\\s+");

        Map<String,String> newVars = new HashMap<>(envVars);

        if (parts.length > 1) {
            String[] var = parts[1].split("=");

            if (var.length > 1) {
                String varName = var[0];
                String initVarValue = var[1];

                String varValue = resolveEnvVars(envVars,initVarValue);
                newVars.put(varName,varValue);
            }
        }

        return newVars;
    }

    /*
     * Finds the file from the relative path provided (relative to the project root)
     * and returns a copy of the map of environment variables with the ones found in the file added.
     *
     * Throws an IllegalArgumentException if the given file is not found.
     */
    public static Map<String,String> getEnvVariablesFromFile(Map<String, String> envVars, List<String> pathFromProjectRoot, String filename){
        Path rootDir = Paths.get(".").normalize().toAbsolutePath().getParent();

        Path path = rootDir;
        for (String pathPart : pathFromProjectRoot) {
            path = path.resolve(pathPart);
        }
        path = path.normalize().resolve(filename);


        BufferedReader reader;

        Map<String,String> newEnvVars = new HashMap<>(envVars);

        try {
            reader = new BufferedReader(new FileReader(path.toFile()));
            String line = reader.readLine();

            while (line != null) {

                if (line.startsWith("export ")) {
                    newEnvVars = computeEnvVar(newEnvVars, line);
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File " + path.toString() + " not found.", e);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return newEnvVars;
    }

    /*
     * Parses the setupEnvironment.sh file for all environment variables set within. Resolves using
     * the environment variables set in System.getenv() and any variables in the file in found order.
     * Since the file is at the root of the project, the path from the root is empty
     */
    public static String getEnvVarFromFile(String envVarName){
        Map<String,String> envVars = System.getenv();
        List<String> pathToFile = new ArrayList<>();
        String filename = "setupEnvironment.sh";

        envVars = getEnvVariablesFromFile(envVars,pathToFile,filename);

        return envVars.get(envVarName);
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
            deploymentName = getEnvVarFromFile("CAPSTONE_SERVICE_STACK_DEV");
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
