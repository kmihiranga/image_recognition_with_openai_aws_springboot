package com.netwizsoft.chatgptimagerecognize.application;

import java.util.Map;

import reactor.core.publisher.Mono;

public interface OpenAIApplicationService {
	
	/**
	 * openai message request content
	 * 
	 * @param role
	 * @param type
	 * @param text
	 * @param imageUrl
	 * @param detail
	 * @return
	 */
	Map<String, Object> createMessageContent(String role, String type, String text, String imageUrl, String detail);
	
	/**
	 * openai send request
	 * 
	 * @param url
	 */
	Mono<String> sendRequest(String imageUrl);
}
