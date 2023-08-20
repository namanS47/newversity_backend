package com.example.newversity.services.teacher

import com.example.newversity.aws.s3.service.AwsS3Service
import com.example.newversity.entity.teacher.Tags
import com.example.newversity.entity.teacher.TeacherTagDetails
import com.example.newversity.model.*
import com.example.newversity.repository.TagsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

@Service
class TagsService(
        @Autowired val tagsRepository: TagsRepository,
        @Autowired val awsS3Service: AwsS3Service,
) {
    @Async
    fun updateTagList(tagList: List<TagModel>?, teacherId: String, tagType: String) {
        val existingTagList = getAllTagsWithTeacherId(teacherId, false)
        existingTagList.forEach { existingTag ->
            var removeTeacherIdFromTag = true
            tagList?.forEach { newTag ->
                if (newTag.tagName == existingTag.tagName) {
                    removeTeacherIdFromTag = false
                }
            }
            if (removeTeacherIdFromTag && existingTag.tagCategory == tagType) {
                existingTag.teacherTagDetailList?.remove(teacherId)
                tagsRepository.save(existingTag)
            }
        }
        mapNewTags(tagList, teacherId)
    }

    fun getAllTags(adminApprove: Boolean?): List<TagModel>{
        return tagsRepository.findAll().filter { adminApprove == false || it.adminApprove == true }
                .map {
            TagConvertor.toAllTagModel(it)
        }
    }

    fun getAllTagsWithTeacherId(teacherId: String, filterByAdminApproval: Boolean): List<Tags> {
        val tagsList = tagsRepository.findAll()
        val filteredTagsList = tagsList.filter {
            (!filterByAdminApproval || it.adminApprove == true ) && it.teacherTagDetailList?.contains(teacherId) ?: false
        }
        return filteredTagsList
    }

    fun getAllTagsModelWithTeacherId(teacherId: String):  List<TagModel> {
        val tagsList = tagsRepository.findAll().map {
            TagConvertor.toAllTagModel(it)
        }
        val teacherTagModelList = tagsList
                .filter {
                    it.adminApprove == true && it.teacherTagDetailList?.contains(teacherId) ?: false
                }
                .map {
                    TagModel(
                            tagName = it.tagName,
                            tagCategory = it.tagCategory,
                            teacherTagDetails = it.teacherTagDetailList!![teacherId]
                    )
                }
        return teacherTagModelList
    }

    fun mapNewTags(tagList: List<TagModel>?, teacherId: String?) {
        tagList?.forEach {
            val tag = it.tagName?.let { it1 -> tagsRepository.findByTagNameIgnoreCase(it1) }
            if (tag != null) {
                if (tag.isPresent) {
                    val list = tag.get().teacherTagDetailList ?: mutableMapOf()
                    if (teacherId != null) {
                        list[teacherId] = TeacherTagDetails(TagStatus.Unverified)
                    }
                    tag.get().teacherTagDetailList = list
                    tagsRepository.save(tag.get())
                } else {
                    val newTag = Tags()
                    val teacherDetailList = mutableMapOf<String, TeacherTagDetails>()
                    if (teacherId != null) {
                        teacherDetailList[teacherId] = TeacherTagDetails(TagStatus.Unverified)
                    }
                    newTag.apply {
                        tagName = it.tagName
                        teacherTagDetailList = teacherDetailList
                        tagCategory = it.tagCategory
                    }
                    tagsRepository.save(newTag)
                }
            }
        }
    }

    fun addTagProofForTeacher(file: MultipartFile, tagName: String, teacherId: String): ResponseEntity<*> {
        return try {
            val fileUrl = awsS3Service.saveFile(file)
            val tag = tagsRepository.findByTagNameIgnoreCase(tagName).get()
            if(tag.teacherTagDetailList?.contains(teacherId) == true) {
                if(tag.teacherTagDetailList!![teacherId]?.documents.isNullOrEmpty()) {
                    val docs = ArrayList<String>()
                    docs.add(fileUrl)
                    tag.teacherTagDetailList!![teacherId]?.documents = docs
                } else {
                    tag.teacherTagDetailList!![teacherId]?.documents?.add(fileUrl)
                }
                tag.teacherTagDetailList!![teacherId]?.tagStatus = TagStatus.InProcess
                tagsRepository.save(tag)
                ResponseEntity.ok(EmptyJsonResponse())
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Tag is not added for this teacher, please add tag first"))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("status" to "Something went wrong"))
        }
    }

    fun getTagByTagNameResponse(tagName: String) : ResponseEntity<*> {
        val tag = tagsRepository.findByTagNameIgnoreCase(tagName).filter{
            it.adminApprove == true
        }
        if(tag.isPresent) {
            return ResponseEntity.ok(TagConvertor.toAllTagModel(tag.get()))
        }
        return ResponseEntity.ok(EmptyJsonResponse())
    }

    fun getTagByTagName(tagName: String) : Tags? {
        val tag = tagsRepository.findByTagNameIgnoreCase(tagName).filter {
            it.adminApprove == true
        }
        if(tag.isPresent) {
            return tag.get()
        }
        return null
    }

    fun approveTagsByTagName(tagList: List<TagModel>?) {
        tagList?.forEach {
            val tag = it.tagName?.let { it1 -> tagsRepository.findByTagNameIgnoreCase(it1) }
            if (tag != null && tag.isPresent) {
                val tagEntity = tag.get()
                tagEntity.adminApprove = true
                tagsRepository.save(tagEntity)
            }
        }
    }
}

enum class TagStatus {
    Verified, Unverified, Failed, InProcess
}