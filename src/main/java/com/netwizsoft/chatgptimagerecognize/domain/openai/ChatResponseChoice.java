package com.netwizsoft.chatgptimagerecognize.domain.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseChoice {
	
	private int index;
	
	private ChatResponseMessage message;
}
