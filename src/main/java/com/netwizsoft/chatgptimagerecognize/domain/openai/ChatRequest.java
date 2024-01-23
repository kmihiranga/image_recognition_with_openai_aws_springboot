package com.netwizsoft.chatgptimagerecognize.domain.openai;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChatRequest {
	
	private String model;
	
	private List<ChatMessage> messages;
	
	@JsonProperty("max_tokens")
	private int maxTokens;
	
	private int n;
	
	private double temperature;
	
	public ChatRequest(String model, String prompt, int maxTokens) {
		this.model = model;
		
		this.maxTokens = maxTokens;
		
		this.messages = new ArrayList<>();
		
		this.messages.add(new ChatMessage("user", prompt));
	}
}
