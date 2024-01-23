package com.netwizsoft.chatgptimagerecognize.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netwizsoft.chatgptimagerecognize.domain.openai.ChatResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OpenAIApplicationServiceImpl implements OpenAIApplicationService {
	
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	
	@Value("${openai.model}")
	private String openAIModel;
	
	@Value("${openai.max.tokens}")
	private int openAIMaxTokens;
	
	public OpenAIApplicationServiceImpl(WebClient webClient, ObjectMapper objectMapper) {
		this.webClient = webClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public Map<String, Object> createMessageContent(String role, String type, String text, String imageUrl,
			String detail) {
		Map<String, Object> message = new HashMap<>();
        message.put("role", role);
        
        List<Map<String, Object>> contentList = new ArrayList<>();
        if (text != null && !text.isEmpty()) {
            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", text);
            contentList.add(textContent);
        }
        
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Map<String, Object> imageContent = new HashMap<>();
            Map<String, String> imageDetails = new HashMap<>();
            imageContent.put("type", "image_url");
            imageDetails.put("url", imageUrl);
            imageDetails.put("detail", detail);
            imageContent.put("image_url", imageDetails);
            contentList.add(imageContent);
        }

        message.put("content", contentList);
        return message;
	}

	@Override
	public Mono<String> sendRequest(String imageUrl) {
		log.info("Starting initialize message context...");
		List<Map<String, Object>> messages = new ArrayList<>();
		
		log.info("Creating message content...");
		// create system message
		Map<String, Object> systemMessage = createMessageContent(
				"system", 
				"text", 
				"Return only JSON without any Markdown formatting or additional text.", 
				null, 
				null
				);
	    messages.add(systemMessage);
	    log.info("Created system message...");
	    
	    log.info("Creating user text message content...");
	    String userTextMessage = """
	    	    Extract data from this receipt.
	    	    Return only JSON, without any additional text or Markdown formatting.

	    	    Return the data as a JSON object with the following keys:
	    	    - `store`: The name of the business or store the receipt is from. Correct it if it isn't properly spelled or capitalized.
	    	    - `amount`: The grand total of the receipt without commas or currency symbols. If you are unsure, set this to an empty string; do not attempt to calculate it.
	    	    - `description`: A general description of what was purchased.
	    	    - `category`: Whichever category is most appropriate ($categories).

	    	    If you are unsure about any values, set them to an empty string.
	    	    """;
	    
	    Map<String, Object> userMessage = createMessageContent("user", "text", userTextMessage, imageUrl, "low");
	    messages.add(userMessage);
	    log.info("Created user text message...");
	    
	    Map<String, String> jsonResponse = new HashMap<>();
	    jsonResponse.put("type", "json_object");
	    
	    log.info("Processing chat completions api request...");
		
	    return webClient.post()
	    		.uri("/v1/chat/completions")
	    		.body(BodyInserters.fromValue(Map.of(
	    				"model", openAIModel,
	    				"max_tokens", openAIMaxTokens,
//	    				"response_format", jsonResponse,
	    				"messages", messages
	    				)))
	    		.retrieve()
	    		.bodyToMono(ChatResponse.class)
	    		.onErrorResume(error -> {

	                // The stream terminates with a `[DONE]` message, which causes a serialization error
	                // Ignore this error and return an empty stream instead
	                if (error.getMessage().contains("JsonToken.START_ARRAY")) {
	                    return Mono.empty();
	                }

	                // If the error is not caused by the `[DONE]` message, return the error
	                else {
	                    return Mono.error(error);
	                }
	            })
	            .filter(response -> {
	                var content = response.getChoices().get(0).getMessage().getContent();
	                return content != null && !content.equals("\n\n");
	            })
	            .map(response -> response.getChoices().get(0).getMessage().getContent());
	}
	
}
