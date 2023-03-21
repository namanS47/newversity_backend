package com.example.NewVersity.controller

import com.example.NewVersity.model.TeacherDetailModel
import com.example.NewVersity.model.TeacherExperienceModel
import com.example.NewVersity.services.teacher.TeacherServices
import com.example.NewVersity.services.room.RoomService
import com.example.NewVersity.services.teacher.TeacherExperienceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/")
class AppController(
        @Autowired val teacherServices: TeacherServices,
        @Autowired val roomService: RoomService,
        @Autowired val teacherExperienceService: TeacherExperienceService
) {

    @GetMapping("/springBoot")
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
}