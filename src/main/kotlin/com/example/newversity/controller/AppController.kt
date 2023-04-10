package com.example.newversity.controller

import com.example.newversity.model.*
import com.example.newversity.services.room.RoomService
import com.example.newversity.services.teacher.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/")
class AppController(
        @Autowired val teacherServices: TeacherServices,
        @Autowired val bankAccountService: BankAccountService,
        @Autowired val roomService: RoomService,
        @Autowired val teacherExperienceService: TeacherExperienceService,
        @Autowired val teacherEducationService: TeacherEducationService,
        @Autowired val availabilityService: AvailabilityService
) {

    @GetMapping("/")
    fun getHello(): String = "Hello Naman"

    @GetMapping("/spring")
    fun createRoom(): String {
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
    fun getTeacher(@RequestHeader("teacherId") teacherId: String): ResponseEntity<*> {
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
    fun getAllTeacherExperience(@RequestHeader teacherId: String): ResponseEntity<*> {
        return teacherExperienceService.getAllTeacherExperience(teacherId)
    }

    @PostMapping("/teacher/education")
    fun addEducation(@RequestBody teacherEducationModel: TeacherEducationModel): ResponseEntity<*> {
        return teacherEducationService.addTeacherEducationDetails(teacherEducationModel)
    }

    @GetMapping("teacher/education")
    fun getAllTeacherEducationDetails(@RequestHeader teacherId: String): ResponseEntity<*> {
        return teacherEducationService.getAllTeacherEducationDetails(teacherId)
    }

    @PostMapping("/teacher/availability")
    fun addAvailability(@RequestBody availabilityListModel: AvailabilityListModel): ResponseEntity<*> {
        return availabilityService.addAvailability(availabilityListModel.availabilityList)
    }

    @GetMapping("/teacher/availability")
    fun getAvailability(@RequestBody availabilityRequestModel: AvailabilityRequestModel): ResponseEntity<*> {
        return availabilityService.getAllAvailabilityByTeacherIdAndDate(availabilityRequestModel)
    }

    @DeleteMapping("/teacher/availability")
    fun deleteAvailability(@RequestHeader id: String): ResponseEntity<*> {
        return availabilityService.removeAvailability(id)
    }

    @PostMapping("/teacher/profileImage")
    fun uploadDocsToS3(@RequestPart("file") file: MultipartFile, teacherId: String): ResponseEntity<*> {
        return teacherServices.saveProfilePicture(file, teacherId)
    }

    @GetMapping("teacher/completion")
    fun getTeacherCompletionPercentage(@RequestHeader teacherId: String): ResponseEntity<*> {
        return teacherServices.getTeacherProfileCompletionPercentage(teacherId)
    }

    @PostMapping("/bankAccount")
    fun addBankAccountDetails(
            @RequestHeader teacherId: String,
            @RequestBody bankAccountDetailModel: BankAccountDetailModel): ResponseEntity<*> {
        return bankAccountService.addBankAccountDetails(bankAccountDetailModel, teacherId)
    }

    @GetMapping("/bankAccount")
    fun getBankAccountDetails(@RequestHeader teacherId: String) : ResponseEntity<*> {
        return bankAccountService.getBankAccountDetails(teacherId)
    }
}