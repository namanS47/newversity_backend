package com.example.newversity.services.teacher

import com.example.newversity.aws.s3.service.AwsS3Service
import com.example.newversity.entity.Tags
import com.example.newversity.entity.TeacherDetails
import com.example.newversity.entity.TeacherTagDetails
import com.example.newversity.model.*
import com.example.newversity.repository.TagsRepository
import com.example.newversity.repository.TeacherRepository
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
        @Autowired val teacherRepository: TeacherRepository,
        @Autowired val awsS3Service: AwsS3Service
) {
    @Async
    fun updateTagList(tagList: List<TagModel>?, teacherId: String, tagType: String) {
        val existingTagList = getAllTagsWithTeacherId(teacherId)
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

    fun getAllTags(): ResponseEntity<*> {
        val allTags =  tagsRepository.findAll().map {
            TagConvertor.toAllTagModel(it)
        }
        return ResponseEntity.ok(allTags)
    }

    fun getAllTagsWithTeacherId(teacherId: String): List<Tags> {
        val tagsList = tagsRepository.findAll()
        val filteredTagsList = tagsList.filter {
            it.teacherTagDetailList?.contains(teacherId) ?: false
        }
        return filteredTagsList
    }

    fun getAllTagsModelWithTeacherId(teacherId: String):  List<TagModel> {
        val tagsList = tagsRepository.findAll().map {
            TagConvertor.toAllTagModel(it)
        }
        val teacherTagModelList = tagsList
                .filter {
                    it.teacherTagDetailList?.contains(teacherId) ?: false
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
            val tag = it.tagName?.let { it1 -> tagsRepository.findByTagName(it1) }
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
            val tag = tagsRepository.findByTagName(tagName).get()
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

    fun getTagByTagName(tagName: String) : ResponseEntity<*> {
        val tag = tagsRepository.findByTagName(tagName)
        if(tag.isPresent) {
            return ResponseEntity.ok(TagConvertor.toAllTagModel(tag.get()))
        }
        return ResponseEntity.ok(EmptyJsonResponse())
    }
    fun getAllTeacherDetailsByTagNamesList(tagModelList: List<TagModel>?) : ResponseEntity<*> {

        val tagList = mutableListOf<Tags>()

        tagModelList?.forEach {
            val tag = it.tagName?.let { it1 -> tagsRepository.findByTagName(it1) }
            if(tag?.isPresent == true) {
                tagList.add(tag.get())
            }
        }

        val teacherAndTagDetailsList = mutableMapOf<String, ArrayList<String>>()
        tagList.forEach {tag ->
            tag.teacherTagDetailList?.forEach {
                val teacherId = it.key
                val tagDetail = it.value

                if(tagDetail.tagStatus == TagStatus.Verified || tagDetail.tagStatus == TagStatus.Unverified) {
                    if(teacherAndTagDetailsList.contains(teacherId)) {
                        teacherAndTagDetailsList[teacherId]!!.add(tag.tagName!!)
                    } else {
                        val arrayListOfTags = arrayListOf<String>()
                        arrayListOfTags.add(tag.tagName!!)
                        teacherAndTagDetailsList[teacherId] = arrayListOfTags
                    }
                }
            }
        }

        val result = arrayListOf<TeacherDetails>()

        teacherAndTagDetailsList.forEach {
            val teacherDetails = teacherRepository.findByTeacherId(it.key)
            if(teacherDetails.isPresent) {
                val teacher = teacherDetails.get()
                teacher.tags = it.value
                result.add(teacher)
            }
        }

        return ResponseEntity.ok().body(result.map { TeacherConverter.toModel(it) })
    }
}

enum class TagStatus {
    Verified, Unverified, Failed, InProcess
}