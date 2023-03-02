package com.example.NewVersity.controller

import com.example.NewVersity.model.TeacherDetailModel
import com.example.NewVersity.services.TeacherServices
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/")
class AppController(
        @Autowired val teacherServices: TeacherServices
) {

    @GetMapping("/springBoot")
    fun getHello(): String = "Hello Naman"

    @PostMapping("/teacher")
    fun addNewTeacher(@RequestBody teacherDetailModel: TeacherDetailModel): ResponseEntity<*> {
        return teacherServices.save(teacherDetailModel)
    }

    @GetMapping("/teacher")
    fun getTeacher(@RequestHeader("teacherId") teacherId: String) : ResponseEntity<*> {
        return teacherServices.getTeacher(teacherId)
    }

    @PutMapping("/teacher")
    fun updateTeacher(@RequestBody teacherDetailModel: TeacherDetailModel, @RequestHeader teacherId: String): ResponseEntity<Boolean> {
        return teacherServices.updateTeacher(teacherDetailModel, teacherId);
    }
}