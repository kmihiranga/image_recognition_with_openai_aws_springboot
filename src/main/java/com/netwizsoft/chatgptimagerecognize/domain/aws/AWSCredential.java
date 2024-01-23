package com.netwizsoft.chatgptimagerecognize.domain.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class AWSCredential {
	
	@Value("${aws.accessKeyId}")
	private String awsAccessKeyId;
	
	@Value("${aws.secretKey}")
	private String awsSecretKey;
	
	@Value("${aws.region}")
	private String awsRegion;
}
