package com.example.NewVersity.controller

import com.example.NewVersity.model.TagModel
import com.example.NewVersity.model.TeacherDetailModel
import com.example.NewVersity.model.TeacherEducationModel
import com.example.NewVersity.model.TeacherExperienceModel
import com.example.NewVersity.services.teacher.TeacherServices
import com.example.NewVersity.services.room.RoomService
import com.example.NewVersity.services.teacher.TagsService
import com.example.NewVersity.services.teacher.TeacherEducationService
import com.example.NewVersity.services.teacher.TeacherExperienceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/")
class AppController(
        @Autowired val teacherServices: TeacherServices,
        @Autowired val roomService: RoomService,
        @Autowired val teacherExperienceService: TeacherExperienceService,
        @Autowired val tagsService: TagsService,
        @Autowired val teacherEducationService: TeacherEducationService
) {

    @GetMapping("/")
    fun getHello(): String = "Hello Naman"

    @GetMapping("/spring")
    fun createRoom(): String{
        return roomService.generateRoom()
    }

    @GetMapping("/getRoomToken")
    fun fetchRoomToken(): ResponseEntity<*> {
        return ResponseEntity.ok(roomService.generateHmsClientToken())
    }

    @PostMapping("/addTeacher")
    fun addTeacher(@RequestBody teacherDetailModel: TeacherDetailModel, @RequestHeader("teacherId") teacherId: String): ResponseEntity<*> {
        return teacherServices.addTeacher(teacherDetailModel, teacherId)
    }

    @GetMapping("/teacher")
    fun getTeacher(@RequestHeader("teacherId") teacherId: String) : ResponseEntity<*> {
        return teacherServices.getTeacher(teacherId)
    }

    @PutMapping("/teacher")
    fun updateTeacher(@RequestBody teacherDetailModel: TeacherDetailModel, @RequestHeader teacherId: String): ResponseEntity<*> {
        return teacherServices.updateTeacher(teacherDetailModel, teacherId);
    }

    @PostMapping("/teacher/experience")
    fun addTeacherExperience(@RequestBody teacherExperienceModel: TeacherExperienceModel): ResponseEntity<*> {
        return teacherExperienceService.addTeacherExperience(teacherExperienceModel)
    }

    @GetMapping("/teacher/experience")
    fun getAllTeacherExperience(@RequestHeader teacherId: String) : ResponseEntity<*> {
        return teacherExperienceService.getAllTeacherExperience(teacherId)
    }

    @PostMapping("/teacher/education")
    fun addEducation(@RequestBody teacherEducationModel: TeacherEducationModel): ResponseEntity<*> {
        return teacherEducationService.addTeacherEducationDetails(teacherEducationModel)
    }

    @GetMapping("teacher/education")
    fun getAllTeacherEducationDetails(@RequestHeader teacherId: String) : ResponseEntity<*> {
        return teacherEducationService.getAllTeacherEducationDetails(teacherId)
    }

    @PostMapping("/teacher/tags")
    fun addTags(@RequestHeader teacherId: String, @RequestBody tagList: List<TagModel>) : ResponseEntity<*> {
        tagsService.updateTagList(tagList, teacherId)
        return ResponseEntity.ok(true)
    }

    @GetMapping("/teacher/tags")
    fun getTagsWithTeacherId(@RequestHeader teacherId: String): ResponseEntity<*> {
        return ResponseEntity.ok(tagsService.getAllTagsWithTeacherId(teacherId))
    }

    @PostMapping("/tags")
    fun addTagsForAdmin(@RequestBody tagList: List<TagModel>) : ResponseEntity<*> {
        tagsService.mapNewTags(tagList, null)
        return ResponseEntity.ok(true)
    }

    @GetMapping("/tags")
    fun getAllTags(): ResponseEntity<*> {
        return ResponseEntity.ok(tagsService.getAllTags())
    }
}