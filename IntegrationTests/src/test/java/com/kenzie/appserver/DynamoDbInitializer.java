package com.kenzie.appserver;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.env.MockPropertySource;
import org.testcontainers.containers.GenericContainer;

public class DynamoDbInitializer  implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static GenericContainer<?> dynamoDb;

    private static GenericContainer<?> getDynamoDbInstance() {
        if (dynamoDb == null) {
            dynamoDb = new GenericContainer<>("amazon/dynamodb-local:latest")
                    .withExposedPorts(8000);
        }
        return dynamoDb;
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        if (System.getenv("STACK_NAME") == null && System.getenv("ARTIFACT_BUCKET") == null) {
            getDynamoDbInstance().start();
            String dynamoDbEndpoint = "http://localhost:" + getDynamoDbInstance().getMappedPort(8000);
            configurableApplicationContext.getEnvironment()
                    .getPropertySources()
                    .addFirst(new MockPropertySource("dynamodb-initializer-properties")
                            .withProperty("dynamodb.override_endpoint", "true")
                            .withProperty("dynamodb.endpoint", dynamoDbEndpoint));
        }
    }
}
