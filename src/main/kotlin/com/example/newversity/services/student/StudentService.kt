package com.example.newversity.services.student

import com.example.newversity.aws.s3.service.AwsS3Service
import com.example.newversity.model.TeacherProfilePercentageModel
import com.example.newversity.model.student.StudentConverter
import com.example.newversity.model.student.StudentDetailModel
import com.example.newversity.repository.students.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

@Service
class StudentService(
        @Autowired val studentRepository: StudentRepository,
        @Autowired val awsS3Service: AwsS3Service,
) {
    fun addStudent(studentDetailModel: StudentDetailModel, studentId: String): ResponseEntity<*> {
        val studentDetailEntity = studentRepository.findByStudentId(studentId)
        return if (studentDetailEntity.isPresent) {
            updateStudent(studentDetailModel, studentId)
        } else {
            save(studentDetailModel)
        }
    }

    fun save(studentDetailModel: StudentDetailModel): ResponseEntity<*> {
        return if (!studentRepository.findByStudentId(studentDetailModel.studentId ?: "").isPresent) {
            val studentDetail = studentRepository.save(StudentConverter.toEntity(studentDetailModel))
            return ResponseEntity.ok().body(StudentConverter.toModel(studentDetail))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Student Already Exist"))
        }
    }

    fun updateStudent(studentDetailModel: StudentDetailModel, studentId: String): ResponseEntity<*> {
        val studentDetailEntity = studentRepository.findByStudentId(studentId)
        if (studentDetailEntity.isPresent) {
            var student = studentDetailEntity.get()
            studentDetailModel.name?.let {
                student.name = it
            }
            studentDetailModel.email?.let {
                student.email = it
            }
            studentDetailModel.location?.let {
                student.location = it
            }
            studentDetailModel.tags?.let {
                student.tags = it
            }
            studentDetailModel.profilePictureUrl?.let {
                student.profilePictureUrl = it
            }
            studentDetailModel.language?.let {
                student.language = it
            }
            studentDetailModel.info?.let {
                student.info = it
            }
            student = studentRepository.save(student)
            return ResponseEntity.ok().body(StudentConverter.toModel(student))
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("status" to "Student Doesn't Exist"))
        }
    }

    fun getStudent(studentId: String): ResponseEntity<*> {
        val studentDetail = studentRepository.findByStudentId(studentId)
        return if (studentDetail.isPresent) {
            ResponseEntity.ok().body(StudentConverter.toModel(studentDetail.get()))
        } else {
            ResponseEntity.ok(null)
        }
    }

    fun saveProfilePicture(file: MultipartFile, id: String): ResponseEntity<*> {
        return try {
            val fileUrl = awsS3Service.saveFile(file)
            val studentDetailModel = StudentDetailModel(studentId = id, profilePictureUrl = fileUrl)
            addStudent(studentDetailModel, id)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Unable to upload file"))
        }
    }

    fun getStudentProfileCompletionPercentage(studentId: String): ResponseEntity<*> {
        val studentProfilePercentageModel = TeacherProfilePercentageModel()
        var completePercentage = 0
        var suggestion: String = ""

        val studentDetails = studentRepository.findByStudentId(studentId)

        if (studentDetails.isPresent) {
            val student = studentDetails.get()

            if(!student.info.isNullOrEmpty()) {
                completePercentage += 20
            } else {
                suggestion = "Please provide personal information about yourself"
            }

            if(!student.language.isNullOrEmpty()){
                completePercentage += 10
            } else {
                suggestion = "Please provide language details"
            }

            if(!student.location.isNullOrEmpty()) {
                completePercentage += 10
            } else {
                suggestion = "Please provide location details"
            }

            if(!student.profilePictureUrl.isNullOrEmpty()) {
                completePercentage += 20
            } else {
                suggestion = "Please provide profile picture url"
            }

            if(!student.tags.isNullOrEmpty()) {
                completePercentage += 20
            } else {
                suggestion = "Please select exams you are preparing for"
            }


            if (!student.name.isNullOrEmpty()) {
                completePercentage += 20
            } else {
                suggestion = "Please fill name"
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "student id doesn't exist"))
        }

        studentProfilePercentageModel.apply {
            this.completePercentage = completePercentage
            this.suggestion = suggestion
        }
        return ResponseEntity.ok().body(studentProfilePercentageModel)
    }
}