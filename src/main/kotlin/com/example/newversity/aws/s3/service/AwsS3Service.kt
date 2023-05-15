package com.example.newversity.aws.s3.service

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.newversity.config.AmazonConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

@Service
class AwsS3Service(
        @Autowired val awsConfig: AmazonConfig
) {
    fun saveFile(file: MultipartFile) : String {
        val metadata = ObjectMetadata()
        metadata.contentLength = file.size
        val request = PutObjectRequest("newversity", file.originalFilename, file.inputStream, metadata)
        awsConfig.s3().putObject(request)
        return String.format("https://%s.s3.amazonaws.com/%s", "newversity", file.originalFilename)
    }
}