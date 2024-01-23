package com.netwizsoft.chatgptimagerecognize.infrastructure.config;

import java.time.Duration;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class RestConfiguration {
	
	@Value("${openai.api.url}")
	private String baseUrl;
	
	@Value("${openai.api.key}")
    private String openaiApiKey;
	
	@Bean
	WebClient webClient() {
		HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(45));
		
		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
		return WebClient.builder()
				.clientConnector(connector)
				.baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
				.build();
	}
}
