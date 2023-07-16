package com.example.newversity.services.teacher

import com.example.newversity.aws.s3.service.AwsS3Service
import com.example.newversity.entity.Tags
import com.example.newversity.entity.TeacherDetails
import com.example.newversity.model.TagModel
import com.example.newversity.model.TeacherConverter
import com.example.newversity.model.TeacherDetailModel
import com.example.newversity.model.TeacherProfilePercentageModel
import com.example.newversity.repository.TeacherEducationRepository
import com.example.newversity.repository.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

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

    fun getTeacher(teacherId: String): ResponseEntity<*> {
        val teacherDetails = getCompleteTeacherDetails(teacherId)
        return ResponseEntity.ok(teacherDetails?.let { TeacherConverter.toModel(it) })
    }

    fun updateTeacher(teacherDetailModel: TeacherDetailModel, teacherId: String): ResponseEntity<*> {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        if (teacherDetailsEntity.isPresent) {
            var teacher = teacherDetailsEntity.get()
            teacherDetailModel.name?.let {
                teacher.name = it
            }

            if(teacher.mobileNumber == null) {
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

    fun isTeacherValid(teacherDetailModel: TeacherDetailModel): Boolean {
        var isTeacherValid = true
        teacherDetailModel.let {
            isTeacherValid = !(it.teacherId.isNullOrEmpty() || it.mobileNumber.isNullOrEmpty() || it.email.isNullOrEmpty() || it.name.isNullOrEmpty())
        }
        return isTeacherValid
    }

    fun getTeacherProfileCompletionPercentage(teacherId: String): ResponseEntity<*> {
        val teacherProfilePercentageModel = TeacherProfilePercentageModel()
        var completePercentage = 0
        var suggestion: String = ""

        val tagList = tagsService.getAllTagsWithTeacherId(teacherId)

        if (tagList.isNotEmpty()) {
            completePercentage += 20
        } else {
            suggestion = "please add tags"
        }

        var isAnyTagVerified = false

        tagList.forEach {
            if (it.teacherTagDetailList!![teacherId]?.tagStatus == TagStatus.Verified) {
                isAnyTagVerified = true
            }
        }

        if (isAnyTagVerified) {
            completePercentage += 20
        } else {
            suggestion = "please upload document proof"
        }

        if (checkForExperience(teacherId)) {
            completePercentage += 20
        } else {
            suggestion = "Add Experience"
        }

        if (checkForEducation(teacherId)) {
            completePercentage += 20
        } else {
            suggestion = "Add Education Details"
        }

        if (checkForPersonalInformation(teacherId)) {
            completePercentage += 20
        } else {
            suggestion = "Complete personal Info"
        }

        teacherProfilePercentageModel.apply {
            this.completePercentage = completePercentage
            this.suggestion = suggestion
        }
        return ResponseEntity.ok().body(teacherProfilePercentageModel)
    }

    fun checkForPersonalInformation(teacherId: String): Boolean {
        val teacherDetailsEntity = teacherRepository.findByTeacherId(teacherId)
        return if (teacherDetailsEntity.isPresent) {
            val teacher = teacherDetailsEntity.get()
            !(teacher.name.isNullOrEmpty() || teacher.profilePictureUrl.isNullOrEmpty() || teacher.title.isNullOrEmpty()
                    || teacher.info.isNullOrEmpty() || teacher.location.isNullOrEmpty() || teacher.language.isNullOrEmpty())
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
                }.map {
                    it.tagName!!
                }
            }
            return teacher
        }
        return null
    }

    fun getAllAvailableTeacher(tagModelList: List<TagModel>?): List<TeacherDetailModel> {
        val availableTeacherListByTagName = getAllAvailableTeachersDetailByTagName(tagModelList)
        if(availableTeacherListByTagName.isNotEmpty()) {
            return availableTeacherListByTagName
        }

        val allTeacherList= teacherRepository.findAll().filter { it.teacherId != null && it.isApproved == true }
        val result = arrayListOf<TeacherDetailModel>()

        allTeacherList.forEach {
            val allAvailability = availabilityService.getAllAvailabilityByTeacherIdAndDate(it.teacherId!!)
            val educationDetails = teacherEducationRepository.findAllByTeacherId(it.teacherId!!)
            val tagList = tagsService.getAllTagsModelWithTeacherId(it.teacherId!!)

            if (educationDetails.isNotEmpty()) {
                it.education = educationDetails[0].name
            }
            if (tagList.isNotEmpty()) {
                it.tags = tagList.filter {it1->
                    !it1.tagName.isNullOrEmpty()
                }.map {it1->
                    it1.tagName!!
                }
            }

            val teacherModel = TeacherConverter.toModel(it)

            if(allAvailability.isNotEmpty()) {
                teacherModel.nextAvailable = allAvailability[0].startDate
            }
            result.add(teacherModel)
        }

        return result
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
                //TODO: Naman: remove unverified tags
                if (tagDetail.tagStatus == TagStatus.Verified || tagDetail.tagStatus == TagStatus.Unverified) {
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
                if(allAvailability.isNotEmpty()) {
                    teacherModel.nextAvailable = allAvailability[0].startDate
                }
                result.add(teacherModel)
            }
        }
        return result
    }

    fun getAllTeacherDetailsBySearchKeyword(keyword: String): List<TeacherDetailModel> {
        if(keyword.isEmpty()) {
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
                if(allAvailability.isNotEmpty()) {
                    teacherModel.nextAvailable = allAvailability[0].startDate
                }
                resultedTeacherDetails.add(teacherModel)
            }
        }
        return resultedTeacherDetails + getAllTeacherDetailsByTagKeyword
    }

//    fun isTeacherApproved(teacherDetailModel: TeacherDetailModel):
}