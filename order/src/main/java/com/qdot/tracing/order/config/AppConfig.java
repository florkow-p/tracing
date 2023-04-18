package com.qdot.tracing.order.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

}
