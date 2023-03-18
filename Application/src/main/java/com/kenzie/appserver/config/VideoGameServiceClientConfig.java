package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.VideoGameServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoGameServiceClientConfig {
    @Bean
    public VideoGameServiceClient videoGameServiceClient(){return new VideoGameServiceClient();}

}
