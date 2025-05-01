package com.tj703.l09_spring_login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.IOException;
import java.util.Arrays;
@Service
public class FileUploadS3Service {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    private String PATH_PREFIX = "public/img/";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String[] imageTypes = {
            "png", "jpg", "jpeg", "webp", "gif", "bmp", "tiff", "svg"
    };
    private S3Client s3Client() {
        //객체 생성시 접속
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }
    //요청시 받아온 파일이 이미지인지 확인 : 아니면 오류
    public void imgTypeTest(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IOException("File is empty.");


        String[] contentType = file.getContentType().split("/");
        if (!contentType[0].equals("image"))
            throw new IOException("이미지만 저장 가능");


        boolean match = Arrays.stream(imageTypes).anyMatch(type -> contentType[1].equals(type));
        if (!match)
            throw new IOException("Only png, jpg, jpeg, webp files are allowed.");
    }
    //이미지 이름 작성 후 S3에 업로드
    public String uploadProfileImage(MultipartFile file, String id) throws IOException {
        // 이미지인지 확인
        imgTypeTest(file);
        // image/png -> png
        String ext = file.getContentType().split("/")[1];
        //파일 이름 작성 : public/dog1_profile.png
        String fileName = PATH_PREFIX+id + "_profile." + ext;
        //S3에 저장한 파일 객체 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();
        //S3에 저장요청
        s3Client().putObject(putObjectRequest, RequestBody.fromInputStream(
                file.getInputStream(),
                file.getSize()
        ));
        //https://deveckm-img-test.s3.ap-northeast-2.amazonaws.com/public/develckm_profile.webp
        //https://tj703-img-up-develckm.s3.ap-northeast-2.amazonaws.com/public/img/비숑.jpeg
        //https://[버킷명].s3.[버킷 위치].amazonaws.com/[파일 이름 및 경로]
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
        return fileUrl;
    }


    public void delete(String fileUrl) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileUrl)
                .build();


        s3Client().deleteObject(deleteObjectRequest);
        logger.info("S3 파일 삭제 : {}", fileUrl);
    }


}
