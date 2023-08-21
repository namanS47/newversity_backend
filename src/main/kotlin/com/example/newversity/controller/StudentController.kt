package com.example.newversity.controller

import com.example.newversity.model.student.PromoCodeModel
import com.example.newversity.model.student.StudentDetailModel
import com.example.newversity.services.student.PromoCodeService
import com.example.newversity.services.student.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/")
class StudentController(
        @Autowired val studentService: StudentService,
        @Autowired val promoCodeService: PromoCodeService
) {
    @PostMapping("/student")
    fun addStudent(@RequestHeader studentId: String, @RequestBody studentDetailModel: StudentDetailModel) : ResponseEntity<*> {
        return studentService.addStudent(studentDetailModel, studentId)
    }

    @GetMapping("/student")
    fun getStudent(@RequestHeader studentId: String): ResponseEntity<*> {
        return studentService.getStudent(studentId)
    }

    @PostMapping("/student/profileImage")
    fun uploadDocsToS3(@RequestPart("file") file: MultipartFile, studentId: String) : ResponseEntity<*> {
        return studentService.saveProfilePicture(file, studentId)
    }

    @GetMapping("/student/completion")
    fun getStudentCompletionPercentage(@RequestHeader studentId: String) : ResponseEntity<*> {
        return studentService.getStudentProfileCompletionPercentage(studentId)
    }

    @PostMapping("/promoCode")
    fun addPromoCode(@RequestBody promoCodeModel: PromoCodeModel): ResponseEntity<*> {
        return promoCodeService.addPromoCode(promoCodeModel)
    }

    @GetMapping("/promoCode")
    fun getPromoCodeDetails(@RequestParam promoCode: String) : ResponseEntity<*> {
        return promoCodeService.verifyPromoCode(promoCode)
    }
}