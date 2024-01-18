package com.marcin.housing.configuration;

import com.marcin.housing.service.HousingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class Config {

    @Value("${housing.client.base-url}")
    private String housingClientBaseUrl;

    @Bean
    public HousingClient housingClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(housingClientBaseUrl)
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(HousingClient.class);
    }
}
