package com.example.newversity.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.Region
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AmazonConfig {
    @Bean
    fun s3(): AmazonS3 {
        val awsCredentials: AWSCredentials = BasicAWSCredentials("AKIAVMVRYBJQ6R435WN3", "UN8kYbQf6QA+8bu7RZyENTQstFiduCxjF2Ad+j2m")
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("ap-south-1")
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .build()
    }
}