package com.netwizsoft.chatgptimagerecognize.domain.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {
	
	private int code;
	private String message;
}
