package com.example.newversity.controller

import com.example.newversity.model.*
import com.example.newversity.model.common.BlogDetailsModel
import com.example.newversity.model.common.RequestSessionModel
import com.example.newversity.model.payment.OrderRequestModel
import com.example.newversity.model.payment.phonepe.PhonePeCallbackResponseModel
import com.example.newversity.model.payment.phonepe.PhonePePGUrlRequestModel
import com.example.newversity.model.teacher.*
import com.example.newversity.services.BlogService
import com.example.newversity.services.CommonDetailService
import com.example.newversity.services.Razorpay.RazorpayService
import com.example.newversity.services.WebinarService
import com.example.newversity.services.firebase.FirebaseMessagingService
import com.example.newversity.services.phonepe.PhonePeService
import com.example.newversity.services.student.RequestSessionService
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
        @Autowired val phonePeService: PhonePeService,
        @Autowired val webinarService: WebinarService,
        @Autowired val requestSessionService: RequestSessionService,
        @Autowired val blogService: BlogService
) {

    @GetMapping("/")
    fun getHello(): String = "Hello Naman"

    @PostMapping("/addTeacher")
    fun addTeacher(@RequestBody teacherDetailModel: TeacherDetailModel, @RequestHeader("teacherId") teacherId: String): ResponseEntity<*> {
        return teacherServices.addTeacher(teacherDetailModel, teacherId)
    }

    @GetMapping("/teacher")
    fun getTeacher(@RequestHeader("teacherId") teacherId: String?, @RequestHeader("username") username: String?): ResponseEntity<*> {
        return teacherServices.getTeacher(teacherId, username)
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
    fun getBankAccountDetails(@RequestHeader teacherId: String): ResponseEntity<*> {
        return bankAccountService.getBankAccountDetails(teacherId)
    }

    @PostMapping("/order")
    fun createPaymentOrder(@RequestBody orderDetails: OrderRequestModel): ResponseEntity<*> {
        return razorpayService.createOrder(orderDetails)
    }

    @GetMapping("/search/teacher")
    fun getAllTeacherBySearchKeyword(@RequestParam searchKeyword: String): ResponseEntity<*> {
        return ResponseEntity.ok(teacherServices.getAllTeacherDetailsBySearchKeyword(searchKeyword))
    }

    @PostMapping("/pushNotification")
    fun sendPushNotification(@RequestBody notificationDetails: NotificationDetailsModel) {
        firebaseMessagingService.sendBulkNotification(notificationDetails)
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
    fun getAppVersionDetails(): ResponseEntity<*> {
        return commonDetailService.getAppVersionDetails()
    }

    @PostMapping("/webinar")
    fun addWebinar(@RequestBody webinarModel: WebinarModel): ResponseEntity<*> {
        return webinarService.addWebinar(webinarModel)
    }

    @GetMapping("/webinar")
    fun getWebinar(@RequestHeader webinarId: String): ResponseEntity<*> {
        return webinarService.getWebinarDetails(webinarId)
    }

    @GetMapping("/webinar/all")
    fun getAllWebinar(): ResponseEntity<*> {
        return webinarService.getAllFutureWebinar()
    }

    @PostMapping("/webinar/register")
    fun registerWebinar(@RequestBody studentInfoModel: StudentInfoModel, @RequestHeader webinarId: String): ResponseEntity<*> {
        return webinarService.registerInWebinar(studentInfoModel, webinarId)
    }

    @PostMapping("requestSession")
    fun addSessionRequest(@RequestBody requestSessionModel: RequestSessionModel): ResponseEntity<*> {
        return requestSessionService.addSessionRequest(requestSessionModel)
    }

    @PostMapping("/webinar/review")
    fun addWebinarReview(@RequestBody studentInfoModel: StudentInfoModel, @RequestHeader webinarId: String): ResponseEntity<*> {
        return webinarService.addWebinarReview(studentInfoModel, webinarId)
    }

    @GetMapping("/notifications")
    fun getAllNotificationList(@RequestHeader userId: String): ResponseEntity<*> {
        return firebaseMessagingService.getAllNotificationByUserId(userId)
    }

    @PostMapping("/teacher/content")
    fun saveTeacherContent(@RequestPart fileList: List<MultipartFile>, @RequestPart teacherId: String,
                           @RequestPart fileTitle: String?, @RequestPart description: String?): ResponseEntity<*> {
        return teacherServices.saveTeacherContent(fileList, teacherId, fileTitle, description)
    }

    @PostMapping("/blog")
    fun addBlog(@RequestBody blogDetailsModel: BlogDetailsModel) : ResponseEntity<*> {
        return blogService.addBlog(blogDetailsModel)
    }

    @GetMapping("/blog")
    fun getBlog(@RequestParam id: String): ResponseEntity<*> {
        return blogService.getBlog(id)
    }

    @GetMapping("/blog/list")
    fun getBlogsList(): ResponseEntity<*> {
        return blogService.getAllBlogs()
    }

    @PostMapping("blog/image")
    fun addBlogImage(@RequestPart file: MultipartFile, @RequestPart blogId: String): ResponseEntity<*> {
        return blogService.blogFeaturedImage(file, blogId)
    }
}