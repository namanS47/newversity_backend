package com.example.newversity.services.teacher

import com.example.newversity.aws.s3.service.AwsS3Service
import com.example.newversity.entity.teacher.SharedContentEntity
import com.example.newversity.entity.teacher.Tags
import com.example.newversity.entity.teacher.TeacherDetails
import com.example.newversity.model.*
import com.example.newversity.model.teacher.*
import com.example.newversity.repository.teacher.TeacherEducationRepository
import com.example.newversity.repository.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import kotlin.collections.HashMap

@Service
class TeacherServices(
        @Autowired val teacherRepository: TeacherRepository,
        @Autowired val tagsService: TagsService,
        @Autowired val awsS3Service: AwsS3Service,
        @Autowired val teacherEducationService: TeacherEducationService,
        @Autowired val teacherExperienceService: TeacherExperienceService,
        @Autowired val teacherEducationRepository: TeacherEducationRepository,
        @Autowired val availabilityService: AvailabilityService
) {

    fun addTeacher(teacherDetailModel: TeacherDetailModel, teacherId: String): ResponseEntity<*> {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        return if (teacherDetailsEntity.isPresent) {
            updateTeacher(teacherDetailModel, teacherId)
        } else {
            save(teacherDetailModel)
        }
    }

    fun save(teacherDetailModel: TeacherDetailModel): ResponseEntity<*> {
        return if (!teacherRepository.findByTeacherId(teacherDetailModel.teacherId ?: "").isPresent) {
            teacherDetailModel.isNew = true
            val teacherDetails = teacherRepository.save(TeacherConverter.toEntity(teacherDetailModel))
            ResponseEntity.ok(TeacherConverter.toModel(teacherDetails))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Teacher Already Exist"))
        }
    }

    fun getTeacher(teacherId: String?, username: String?): ResponseEntity<*> {
        if(teacherId.isNullOrEmpty() && username.isNullOrEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("reason" to "invalid request"))
        }

        if(teacherId.isNullOrEmpty()) {
            val teacherDetailsEntity = teacherRepository.findByUserName(username!!)
            if(teacherDetailsEntity.isPresent) {
                val teacherDetails = getCompleteTeacherDetails(teacherDetailsEntity.get().teacherId!!)
                return ResponseEntity.ok(teacherDetails?.let { TeacherConverter.toModel(it) })
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("reason" to "teacher doesn't exist"))
            }
        } else {
            val teacherDetails = getCompleteTeacherDetails(teacherId)
            return ResponseEntity.ok(teacherDetails?.let { TeacherConverter.toModel(it) })
        }
    }

    fun updateTeacher(teacherDetailModel: TeacherDetailModel, teacherId: String): ResponseEntity<*> {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        if (teacherDetailsEntity.isPresent) {
            var teacher = teacherDetailsEntity.get()
            if(teacher.userName.isNullOrEmpty() && !teacherDetailModel.name.isNullOrEmpty()) {
                teacher.userName =  generateUserName(teacherDetailModel.name!!)
            }

            teacherDetailModel.name?.let {
                teacher.name = it
            }

            if (teacher.mobileNumber == null) {
                teacherDetailModel.mobileNumber?.let {
                    teacher.mobileNumber = it
                }
            }

            teacherDetailModel.isApproved?.let {
                teacher.isApproved = it
            }

            teacherDetailModel.introVideoUrl?.let {
                teacher.introVideoUrl = it
            }
            teacherDetailModel.location?.let {
                teacher.location = it
            }
            teacherDetailModel.title?.let {
                teacher.title = it
            }
            teacherDetailModel.info?.let {
                teacher.info = it
            }
            teacherDetailModel.email?.let {
                teacher.email = it
            }
//            teacherDetailModel.tags?.let {
//                tagsService.updateTagList(it, teacher.tags ?: arrayListOf(), teacher.teacherId ?: "")
//                teacher.tags = it
//            }
            teacherDetailModel.education?.let {
                teacher.education = it
            }
            teacherDetailModel.designation?.let {
                teacher.designation = it
            }
            teacherDetailModel.profileUrl?.let {
                teacher.profileUrl = it
            }
            teacherDetailModel.profilePictureUrl?.let {
                teacher.profilePictureUrl = it
            }
            teacherDetailModel.sessionPricing?.let {
                teacher.sessionPricing = it
            }
            teacherDetailModel.language?.let {
                teacher.language = it
            }
            teacherDetailModel.level?.let {
                teacher.level = it
            }
            teacherDetailModel.userName?.let {
                teacher.userName = it
            }
            teacherDetailModel.engagementType?.let {
                teacher.engagementType = it
            }
            teacherDetailModel.isNew = false
            teacher = teacherRepository.save(teacher)
            return ResponseEntity.ok(TeacherConverter.toModel(teacher))
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("status" to "Teacher Doesn't Exist"))
        }
    }

    fun saveProfilePicture(file: MultipartFile, id: String): ResponseEntity<*> {
        return try {
            val fileUrl = awsS3Service.saveFile(file)
            val teacherDetailModel = TeacherDetailModel(teacherId = id, profilePictureUrl = fileUrl)
            addTeacher(teacherDetailModel, id)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "Unable to upload file"))
        }
    }

    fun saveTeacherContent(fileList: List<MultipartFile>,
                           teacherId: String,
                           fileTitle: String?, description: String?): ResponseEntity<*> {
        val sharedContentModel = SharedContent(title = fileTitle, description = description)
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        return if(teacherDetailsEntity.isPresent) {
            val teacherDetails = teacherDetailsEntity.get()
            val fileUrl = fileList.map {
                awsS3Service.saveFile(it)
            }
            sharedContentModel.fileUrl = fileUrl
            teacherDetails.sharedContent?.add(SharedContentConvertor.toEntity(sharedContentModel)) ?: run {
                teacherDetails.sharedContent = arrayListOf(SharedContentConvertor.toEntity(sharedContentModel))
            }
            teacherRepository.save(teacherDetails)
            ResponseEntity.ok().body(TeacherConverter.toModel(teacherDetails))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("status" to "teacher id doesn't exist"))
        }
    }

    fun isTeacherValid(teacherDetailModel: TeacherDetailModel): Boolean {
        var isTeacherValid: Boolean
        teacherDetailModel.let {
            isTeacherValid = !(it.teacherId.isNullOrEmpty() || it.mobileNumber.isNullOrEmpty() || it.email.isNullOrEmpty() || it.name.isNullOrEmpty())
        }
        return isTeacherValid
    }

    fun getTeacherProfileCompletionPercentage(teacherId: String): ResponseEntity<*> {
        val teacherProfilePercentageModel = TeacherProfilePercentageModel()
        var completePercentage = 0
        var suggestion = ""
        val profileCompletionStageStatus: HashMap<ProfileCompletionStage, Boolean> = hashMapOf()

        val tagList = tagsService.getAllTagsWithTeacherId(teacherId, true)

        var isAnyTagVerified = false
        var isVerificationInProcess = false

        tagList.forEach {
            if (it.teacherTagDetailList!![teacherId]?.tagStatus == TagStatus.Verified) {
                isAnyTagVerified = true
            } else if (it.teacherTagDetailList!![teacherId]?.tagStatus == TagStatus.InProcess) {
                isVerificationInProcess = true
            }
        }

        if (isAnyTagVerified || isVerificationInProcess) {
            profileCompletionStageStatus[ProfileCompletionStage.VerifiedTags] = true
            completePercentage += 10
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.VerifiedTags] = false
            suggestion = "Please upload document proof"
        }

        if (tagList.isNotEmpty()) {
            profileCompletionStageStatus[ProfileCompletionStage.SelectTags] = true
            completePercentage += 20
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.SelectTags] = false
            suggestion = "Please add tags"
        }

        if (checkForPricing(teacherId)) {
            profileCompletionStageStatus[ProfileCompletionStage.Pricing] = true
            completePercentage += 10
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.Pricing] = false
            suggestion = "Please add session fees"
        }

        if (checkForExperience(teacherId)) {
            profileCompletionStageStatus[ProfileCompletionStage.Experience] = true
            completePercentage += 20
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.Experience] = false
            suggestion = "Add Experience"
        }

        if (checkForEducation(teacherId)) {
            profileCompletionStageStatus[ProfileCompletionStage.Education] = true
            completePercentage += 20
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.Education] = false
            suggestion = "Add Education Details"
        }

        if(checkForProfilePicture(teacherId)) {
            profileCompletionStageStatus[ProfileCompletionStage.ProfilePicture] = true
            completePercentage += 10
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.ProfilePicture] = false
            suggestion = "Add Profile Picture"
        }

        if (checkForPersonalInformation(teacherId)) {
            profileCompletionStageStatus[ProfileCompletionStage.Profile] = true
            completePercentage += 10
        } else {
            profileCompletionStageStatus[ProfileCompletionStage.Profile] = false
            suggestion = "Complete personal Info"
        }

        teacherProfilePercentageModel.apply {
            this.completePercentage = completePercentage
            this.suggestion = suggestion
            this.profileCompletionStageStatus = profileCompletionStageStatus
        }
        return ResponseEntity.ok().body(teacherProfilePercentageModel)
    }

    fun checkForPersonalInformation(teacherId: String): Boolean {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        return if (teacherDetailsEntity.isPresent) {
            val teacher = teacherDetailsEntity.get()
            !(teacher.name.isNullOrEmpty() ||  teacher.title.isNullOrEmpty()
                    || teacher.info.isNullOrEmpty() || teacher.location.isNullOrEmpty() || teacher.language.isNullOrEmpty())
        } else {
            false
        }
    }

    fun checkForProfilePicture(teacherId: String): Boolean {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        return if (teacherDetailsEntity.isPresent) {
            val teacher = teacherDetailsEntity.get()
            !teacher.profilePictureUrl.isNullOrEmpty()
        } else {
            false
        }
    }

    fun checkForPricing(teacherId: String): Boolean {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        return if (teacherDetailsEntity.isPresent) {
            val teacher = teacherDetailsEntity.get()
            !teacher.sessionPricing.isNullOrEmpty()
        } else {
            false
        }
    }

    fun checkForEducation(teacherId: String): Boolean {
        return teacherEducationService.getAllTeacherEducationDetailsList(teacherId).isNotEmpty()
    }

    fun checkForExperience(teacherId: String): Boolean {
        return teacherExperienceService.getAllExperienceListByTeacherId(teacherId).isNotEmpty()
    }

    fun getCompleteTeacherDetails(teacherId: String): TeacherDetails? {
        val teacherDetail = teacherRepository.findByTeacherId(teacherId)
        val educationDetails = teacherEducationRepository.findAllByTeacherId(teacherId)
        val tagList = tagsService.getAllTagsModelWithTeacherId(teacherId)

        if (teacherDetail.isPresent) {
            val teacher = teacherDetail.get()
            if (educationDetails.isNotEmpty()) {
                teacher.education = educationDetails[0].name
            }
            if (tagList.isNotEmpty()) {
                teacher.tags = tagList.filter {
                    !it.tagName.isNullOrEmpty()
                }
            }
            return teacher
        }
        return null
    }

    fun getAllAvailableTeacher(tagModelList: List<TagModel>?): List<TeacherDetailModel> {
        val availableTeacherListByTagName = getAllAvailableTeachersDetailByTagName(tagModelList)
        if (availableTeacherListByTagName.isNotEmpty()) {
            return availableTeacherListByTagName
        }

        val allTeacherList = teacherRepository.findAll().filter { it.teacherId != null && it.isApproved == true }
        val result = arrayListOf<TeacherDetailModel>()

        allTeacherList.forEach {
            val allAvailability = availabilityService.getAllAvailabilityByTeacherIdAndDate(it.teacherId!!)
            val educationDetails = teacherEducationRepository.findAllByTeacherId(it.teacherId!!)
            val tagList = tagsService.getAllTagsModelWithTeacherId(it.teacherId!!)

            if (educationDetails.isNotEmpty()) {
                it.education = educationDetails[0].name
            }
            if (tagList.isNotEmpty()) {
                it.tags = tagList.filter { it1 ->
                    !it1.tagName.isNullOrEmpty()
                }
            }

            val teacherModel = TeacherConverter.toModel(it)

            if (allAvailability.isNotEmpty()) {
                teacherModel.nextAvailable = allAvailability[0].startDate
            }
            result.add(teacherModel)
        }

        return result.sortedWith(compareBy(nullsLast()) { it.nextAvailable })
    }

    fun getAllAvailableTeachersDetailByTagName(tagModelList: List<TagModel>?): List<TeacherDetailModel> {
        val tagList = mutableListOf<Tags>()

        tagModelList?.forEach {
            val tag = it.tagName?.let { it1 -> tagsService.getTagByTagName(it1) }
            if (tag != null) {
                tagList.add(tag)
            }
        }

        val teacherList = mutableSetOf<String>()
        tagList.forEach { tag ->
            tag.teacherTagDetailList?.forEach {
                val teacherId = it.key
                val tagDetail = it.value
                //TODO: Naman: remove unverified tags and inProcess tags
                if (tagDetail.tagStatus == TagStatus.Verified || tagDetail.tagStatus == TagStatus.Unverified ||
                        tagDetail.tagStatus == TagStatus.InProcess) {
                    teacherList.add(teacherId)
                }
            }
        }

        val result = arrayListOf<TeacherDetailModel>()

        teacherList.forEach {
            val teacher = getCompleteTeacherDetails(it)
            val allAvailability = availabilityService.getAllAvailabilityByTeacherIdAndDate(it)
            if (teacher != null && teacher.isApproved == true) {
                val teacherModel = TeacherConverter.toModel(teacher)
                if (allAvailability.isNotEmpty()) {
                    teacherModel.nextAvailable = allAvailability[0].startDate
                }
                result.add(teacherModel)
            }
        }
        return result.sortedWith(compareBy(nullsLast()) { it.nextAvailable })
    }

    fun getAllTeacherDetailsBySearchKeyword(keyword: String): List<TeacherDetailModel> {
        if (keyword.isEmpty()) {
            return listOf()
        }
        val allTeacherList = teacherRepository.findAll()
        val resultedTeacherIds = allTeacherList.filter {
            it.name?.lowercase()?.contains(keyword.lowercase()) == true || it.info?.lowercase()?.contains(keyword.lowercase()) == true || it.title?.lowercase()?.contains(keyword.lowercase()) == true
        }.map {
            it.teacherId!!
        }
        val getAllTeacherDetailsByTagKeyword = getAllAvailableTeachersDetailByTagName(listOf(TagModel(tagName = keyword)))

        val resultedTeacherDetails = arrayListOf<TeacherDetailModel>()
        resultedTeacherIds.forEach {

            val allAvailability = availabilityService.getAllAvailabilityByTeacherIdAndDate(it)
            val teacherDetails = getCompleteTeacherDetails(it)
            if (teacherDetails != null && teacherDetails.isApproved == true) {
                val teacherModel = TeacherConverter.toModel(teacherDetails)
                if (allAvailability.isNotEmpty()) {
                    teacherModel.nextAvailable = allAvailability[0].startDate
                }
                resultedTeacherDetails.add(teacherModel)
            }
        }
        return (resultedTeacherDetails + getAllTeacherDetailsByTagKeyword).distinct().sortedWith(compareBy(nullsLast()) { it.nextAvailable })
    }

//    fun isTeacherApproved(teacherDetailModel: TeacherDetailModel):

    fun generateUserName(name: String) : String {
//        val teacherDetails = teacherRepository.findByTeacherId(teacherId).get()

        var teacherUserName = name.replace(" ", "_")
        var suffixNumber= 10
        while(teacherRepository.findByUserName(teacherUserName+suffixNumber.toString()).isPresent) {
            suffixNumber += 10
        }
        teacherUserName += suffixNumber.toString()
        return teacherUserName
    }
}