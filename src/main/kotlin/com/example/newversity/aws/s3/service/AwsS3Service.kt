package com.example.newversity.aws.s3.service

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.newversity.config.AmazonConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import java.time.Instant
import java.util.Date

@Service
class AwsS3Service(
        @Autowired val awsConfig: AmazonConfig
) {
    fun saveFile(file: MultipartFile) : String {
        val metadata = ObjectMetadata()
        metadata.contentLength = file.size
        var fileName = file.originalFilename?.replace(" ", "")
        fileName = Instant.now().epochSecond.toString() + fileName
        val request = PutObjectRequest("newversity", fileName, file.inputStream, metadata)
        awsConfig.s3().putObject(request)
        return String.format("https://%s.s3.amazonaws.com/%s", "newversity", fileName)
    }
}