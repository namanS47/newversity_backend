package com.example.newversity.controller

import com.example.newversity.model.*
import com.example.newversity.model.payment.OrderRequestModel
import com.example.newversity.model.payment.phonepe.PhonePeCallbackResponseModel
import com.example.newversity.model.payment.phonepe.PhonePePGUrlRequestModel
import com.example.newversity.model.teacher.*
import com.example.newversity.services.CommonDetailService
import com.example.newversity.services.Razorpay.RazorpayService
import com.example.newversity.services.firebase.FirebaseMessagingService
import com.example.newversity.services.phonepe.PhonePeService
import com.example.newversity.services.teacher.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.Date


@RestController
@RequestMapping("/")
class AppController(
        @Autowired val teacherServices: TeacherServices,
        @Autowired val bankAccountService: BankAccountService,
        @Autowired val teacherExperienceService: TeacherExperienceService,
        @Autowired val teacherEducationService: TeacherEducationService,
        @Autowired val availabilityService: AvailabilityService,
        @Autowired val razorpayService: RazorpayService,
        @Autowired val firebaseMessagingService: FirebaseMessagingService,
        @Autowired val commonDetailService: CommonDetailService,
        @Autowired val phonePeService: PhonePeService
) {

    @GetMapping("/")
    fun getHello(): String = "Hello Naman"

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

    @DeleteMapping("/teacher/experience")
    fun deleteTeacherExperience(@RequestHeader id: String): ResponseEntity<*> {
        return teacherExperienceService.deleteTeacherExperience(id)
    }

    @PostMapping("/teacher/education")
    fun addEducation(@RequestBody teacherEducationModel: TeacherEducationModel): ResponseEntity<*> {
        return teacherEducationService.addTeacherEducationDetails(teacherEducationModel)
    }

    @GetMapping("teacher/education")
    fun getAllTeacherEducationDetails(@RequestHeader teacherId: String): ResponseEntity<*> {
        return teacherEducationService.getAllTeacherEducationDetails(teacherId)
    }

    @DeleteMapping("teacher/education")
    fun deleteTeacherEducationDetails(@RequestHeader educationDetailId: String): ResponseEntity<*> {
        return teacherEducationService.deleteTeacherEducation(educationDetailId)
    }

    @PostMapping("/teacher/availability")
    fun addAvailability(@RequestBody availabilityListModel: AvailabilityListModel): ResponseEntity<*> {
        return availabilityService.addAvailability(availabilityListModel.availabilityList)
    }

    @GetMapping("/teacher/availability")
    fun getAvailability(@RequestHeader teacherId: String, @RequestHeader date: Date?): ResponseEntity<*> {
        return availabilityService.getAllAvailabilityByTeacherIdAndDateResponse(teacherId, date)
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

    @PostMapping("/order")
    fun createPaymentOrder(@RequestBody orderDetails: OrderRequestModel): ResponseEntity<*> {
        return razorpayService.createOrder(orderDetails)
    }

    @GetMapping("/search/teacher")
    fun getAllTeacherBySearchKeyword(@RequestParam searchKeyword: String) : ResponseEntity<*> {
        return ResponseEntity.ok(teacherServices.getAllTeacherDetailsBySearchKeyword(searchKeyword))
    }

    @PostMapping("/pushNotification")
    fun sendPushNotification() {
        firebaseMessagingService.sendNotification("ckiJNLRhQm-gVTp8gBp5ik:APA91bGNIoZzci-8KJVYlSDSfDKcIG0RJR-LKJhhME3jzScTFc2D73LAx-vD_axlue30gVBqzlN1xnCi9iztvgBDXwhK2wyy3lLXLbAIv1XtKQ0UW1tUsW-P2UYHEIqJBZW7SjWEJhtD")
    }

    @PostMapping("/fcmToken")
    fun saveFcmToken(@RequestBody commonDetailsModel: CommonDetailsModel) {
        commonDetailService.saveCommonDetails(commonDetailsModel)
    }

    @PostMapping("/getPhonePePGUrl")
    fun getPhonePePaymentUrl(@RequestBody phonePePGUrlRequestModel: PhonePePGUrlRequestModel): ResponseEntity<*> {
        return phonePeService.fetchPhonePePaymentUrl(phonePePGUrlRequestModel)
    }

    @PostMapping("/phonePeCallbackUrl")
    fun handlePhonePeCallbackUrl(@RequestBody phonePeCallbackResponseModel: PhonePeCallbackResponseModel) {
        phonePeService.handleCallbackResponseModel(phonePeCallbackResponseModel)
    }

    @GetMapping("/phonePeTransactionStatus")
    fun checkTransactionStatusPhonePe(@RequestHeader merchantTransactionId: String): ResponseEntity<*> {
        return phonePeService.checkTransactionStatusApi(merchantTransactionId)
    }

    @GetMapping("/app/android/version")
    fun getAppVersionDetails() : ResponseEntity<*> {
        return commonDetailService.getAppVersionDetails()
    }
}