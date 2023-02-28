package com.kenzie.appserver.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.kenzie.appserver.repositories")
public class DynamoDbConfig {
    @Value("${dynamodb.override_endpoint}")
    String dynamoOverrideEndpoint;

    @Bean
    @ConditionalOnProperty(name = "dynamodb.override_endpoint", havingValue = "true")
    public AmazonDynamoDB amazonDynamoDB(@Value("${dynamodb.endpoint}") String dynamoEndpoint) {
        AwsClientBuilder.EndpointConfiguration endpointConfig = new
                AwsClientBuilder.EndpointConfiguration(dynamoEndpoint,
                "us-east-1");

        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfig)
                .build();

    }

    @Bean(name = "amazonDynamoDB")
    public AmazonDynamoDB defaultAmazonDynamoDb() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
