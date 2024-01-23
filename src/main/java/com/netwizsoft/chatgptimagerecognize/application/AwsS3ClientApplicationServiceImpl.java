package com.netwizsoft.chatgptimagerecognize.application;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class AwsS3ClientApplicationServiceImpl implements AwsS3ClientApplicationService {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;
	
	@Autowired
	public AwsS3ClientApplicationServiceImpl(S3Client s3Client, S3Presigner s3Presigner) {
		this.s3Client = s3Client;
		this.s3Presigner = s3Presigner;
	}
	
	@Override
	public List<String> listAllObjects(String awsBucketName) {
		List<String> objectKeys = new ArrayList<>();
		String continuationToken = null;
		
		do {
			ListObjectsV2Request request = ListObjectsV2Request.builder()
					.bucket(awsBucketName)
					.continuationToken(continuationToken)
					.build();
			
			ListObjectsV2Response result = s3Client.listObjectsV2(request);
			
			for (S3Object object: result.contents()) {
				objectKeys.add(object.key());
			}
			continuationToken = result.nextContinuationToken();
		} while(continuationToken != null);
		
		return objectKeys;
		
	}

	@Override
	public void putObject(MultipartFile file, String bucketName, String awsKey) throws IOException {
		s3Client.putObject(
				PutObjectRequest.builder()
				.bucket(bucketName)
				.key(awsKey)
				.contentType(file.getContentType())
				.build(),
				RequestBody.fromInputStream(file.getInputStream(), file.getSize())
				);
	}

	@Override
	public String getPresignedUrl(String awsBucketName, String awsKey) {
		PresignedGetObjectRequest preSignedRequest = s3Presigner.presignGetObject(
				r -> r.signatureDuration(Duration.ofMinutes(60)).getObjectRequest(g -> g.bucket(awsBucketName).key(awsKey)));
		
		return preSignedRequest.url().toString();
	}
}
