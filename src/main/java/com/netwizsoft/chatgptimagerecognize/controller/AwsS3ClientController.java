package com.netwizsoft.chatgptimagerecognize.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netwizsoft.chatgptimagerecognize.application.AwsS3ClientApplicationServiceImpl;
import com.netwizsoft.chatgptimagerecognize.application.OpenAIApplicationServiceImpl;
import com.netwizsoft.chatgptimagerecognize.domain.FileUploadRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class AwsS3ClientController {

	@Autowired
	private AwsS3ClientApplicationServiceImpl awsS3ClientApplicationServiceImpl;
	
	@Autowired
	private OpenAIApplicationServiceImpl openAIApplicationServiceImpl;
	
	@GetMapping("/buckets/{bucketName}/objects")
	public List<String> listBucketObjects(@PathVariable("bucketName") String bucketName) {
		return awsS3ClientApplicationServiceImpl.listAllObjects(bucketName);
	}
	
	@PostMapping("/upload/{bucketName}")
	public Mono<String> uploadFileToBucket(@PathVariable("bucketName") String bucketName, @ModelAttribute("file") FileUploadRequest fileRequest) throws IOException {
		log.info("file upload starting...");
		if (!fileRequest.getFile().isEmpty()) {
			String key = System.currentTimeMillis() + "-" + fileRequest.getFile().getOriginalFilename();
			
			awsS3ClientApplicationServiceImpl.putObject(fileRequest.getFile(), bucketName, key);
			log.info("File uploaded successfully!...");
			
			// get presigned url
			String preSignedUrl = awsS3ClientApplicationServiceImpl.getPresignedUrl(bucketName, key);
			log.info("Presigned URI: " + preSignedUrl);
			
			return openAIApplicationServiceImpl.sendRequest(preSignedUrl);
		} else {
			log.warn("cannot upload. file is empty!...");
		}
		return null;
	}
}
