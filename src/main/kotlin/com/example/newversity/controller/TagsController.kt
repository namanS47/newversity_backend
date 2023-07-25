package com.example.newversity.controller

import com.example.newversity.model.TagListModel
import com.example.newversity.services.student.SearchService
import com.example.newversity.services.teacher.TagsService
import com.example.newversity.services.teacher.TeacherServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/")
class TagsController(
        @Autowired val tagsService: TagsService,
        @Autowired val searchService: SearchService,
        @Autowired val teacherServices: TeacherServices
) {
    @PostMapping("/teacher/tags")
    fun addTags(@RequestHeader teacherId: String, @RequestBody tagListModel: TagListModel, @RequestParam category: String): ResponseEntity<*> {
        tagsService.updateTagList(tagListModel.tagModelList, teacherId, category)
        return ResponseEntity.ok(true)
    }

    @GetMapping("/teacher/tags")
    fun getTagsWithTeacherId(@RequestHeader teacherId: String): ResponseEntity<*> {
        return ResponseEntity.ok(tagsService.getAllTagsModelWithTeacherId(teacherId))
    }

    @PostMapping("/tags")
    fun addTagsForAdmin(@RequestBody tagListModel: TagListModel): ResponseEntity<*> {
        tagsService.mapNewTags(tagListModel.tagModelList, null)
        return ResponseEntity.ok(true)
    }

    @GetMapping("/tags")
    fun getAllTags(@RequestParam adminApprove: Boolean?): ResponseEntity<*> {
        return ResponseEntity.ok().body(tagsService.getAllTags(adminApprove))
    }

    @PostMapping("/teacher/tags/verify")
    fun uploadDocumentProof(
            @RequestPart("file") file: MultipartFile,
            @RequestPart("teacherId") teacherId: String,
            @RequestPart("tag") tag: String): ResponseEntity<*> {
        return tagsService.addTagProofForTeacher(file, tag, teacherId)
    }

    @GetMapping("tag/search")
    fun getAllTagsBySearchKeyword(@RequestParam tag: String) : ResponseEntity<*> {
        return searchService.searchTags(tag)
    }

    @GetMapping("/tag")
    fun getTagByTagName(@RequestParam tagName: String): ResponseEntity<*> {
        return tagsService.getTagByTagNameResponse(tagName)
    }

    @PostMapping("/tag/allTeacher")
    fun getAllTeacherDetailsByTagName(@RequestBody tagListModel: TagListModel) : ResponseEntity<*> {
        return ResponseEntity.ok(teacherServices.getAllAvailableTeacher(tagListModel.tagModelList))
    }

    @PostMapping("/tag/approve")
    fun adminApproveTagList(@RequestBody tagListModel: TagListModel) : ResponseEntity<*> {
        return ResponseEntity.ok(tagsService.approveTagsByTagName(tagListModel.tagModelList))
    }
}