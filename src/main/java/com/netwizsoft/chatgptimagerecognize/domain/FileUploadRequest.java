package com.netwizsoft.chatgptimagerecognize.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequest {
	
	private MultipartFile file;
}
