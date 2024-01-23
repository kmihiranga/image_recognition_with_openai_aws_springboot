package com.netwizsoft.chatgptimagerecognize.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netwizsoft.chatgptimagerecognize.domain.aws.AWSCredential;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AWSConfiguration {
	
	private final AWSCredential awsCredential;
	
	@Autowired
	public AWSConfiguration(AWSCredential awsCredential) {
		this.awsCredential = awsCredential;
	}

    @Bean
    public S3Client s3Client() {
		return S3Client.builder()
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsCredential.getAwsAccessKeyId(), awsCredential.getAwsSecretKey())))
				.region(Region.of(awsCredential.getAwsRegion()))
				.build();
	}
    
    @Bean
    public S3Presigner s3PreSigner() {
    	return S3Presigner.builder()
    			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsCredential.getAwsAccessKeyId(), awsCredential.getAwsSecretKey())))
    			.region(Region.of(awsCredential.getAwsRegion()))
    			.build();
    }
}
