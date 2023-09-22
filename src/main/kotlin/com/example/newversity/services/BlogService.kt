package com.example.newversity.services

import com.example.newversity.aws.s3.service.AwsS3Service
import com.example.newversity.model.common.BlogDetailsConvertor
import com.example.newversity.model.common.BlogDetailsModel
import com.example.newversity.repository.BlogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class BlogService(
        @Autowired val awsS3Service: AwsS3Service,
        @Autowired val blogRepository: BlogRepository
) {
   fun addBlog(blogDetailsModel: BlogDetailsModel): ResponseEntity<*> {
      blogDetailsModel.id?.let {id ->
         val blogDetailsEntity = blogRepository.findById(id)
         if(blogDetailsEntity.isPresent) {
            val blogDetails = blogDetailsEntity.get()
            blogDetailsModel.title?.let {
               blogDetails.title = it
            }
            blogDetailsModel.content?.let {
               blogDetails.content = it
            }
            blogDetailsModel.createdDate?.let {
               blogDetails.createdAt = it
            }
            blogDetailsModel.featureImage?.let {
               blogDetails.featureImage = it
            }
            blogRepository.save(blogDetails)
            return ResponseEntity.ok(BlogDetailsConvertor.toModel(blogDetails))
         } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "id doesn't exist"))
         }
      } ?: run {
         val blogDetails = blogRepository.save(BlogDetailsConvertor.toEntity(blogDetailsModel))
         return ResponseEntity.ok(BlogDetailsConvertor.toModel(blogDetails))
      }
   }

   fun getBlog(id: String) : ResponseEntity<*> {
      val blogDetailsEntity = blogRepository.findById(id)
      if(blogDetailsEntity.isPresent) {
         return ResponseEntity.ok(BlogDetailsConvertor.toModel(blogDetailsEntity.get()))
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "id doesn't exist"))
   }

   fun getAllBlogs(): ResponseEntity<*> {
      val blogList = blogRepository.findAll()
      return ResponseEntity.ok(blogList.map { BlogDetailsConvertor.toModel(it) })
   }

   fun blogFeaturedImage(file: MultipartFile, blogId: String): ResponseEntity<*> {
      val blogDetailsEntity = blogRepository.findById(blogId)
      return if(blogDetailsEntity.isPresent) {
         val awsFilePath = awsS3Service.saveFile(file)
         return addBlog(BlogDetailsModel(id = blogId, featureImage = awsFilePath))
      } else {
         ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "id doesn't exist"))
      }
   }
}