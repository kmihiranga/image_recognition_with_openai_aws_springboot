package com.netwizsoft.chatgptimagerecognize.application;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3ClientApplicationService {
	
	/**
	 * Retrive all specific aws bucket objects
	 * 
	 * @param awsBucketName
	 * @return
	 */
	List<String> listAllObjects(String awsBucketName);
	
	/**
	 * upload a file to s3 bucket
	 * 
	 * @param file
	 * @param bucketName
	 * @throws IOException
	 */
	void putObject(MultipartFile file, String bucketName, String awsKey) throws IOException;
	
	/**
	 * generate pre-signed url
	 * 
	 * @param awsBucketName
	 * @param awsKey
	 * @return
	 */
	String getPresignedUrl(String awsBucketName, String awsKey);
}
