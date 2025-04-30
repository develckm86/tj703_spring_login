package com.tj703.l09_spring_login.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValueTest {
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.s3.bucket}")
    private String bucket;    @Test
    public void print(){
        System.out.println(accessKey);
        System.out.println(secretKey);
        System.out.println(region);
        System.out.println(bucket);
    }
}
