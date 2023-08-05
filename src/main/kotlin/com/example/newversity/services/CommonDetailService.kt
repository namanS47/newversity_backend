package com.example.newversity.services

import com.example.newversity.model.AppVersionConfigModel
import com.example.newversity.model.CommonDetailConverter
import com.example.newversity.model.CommonDetailsModel
import com.example.newversity.repository.CommonDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CommonDetailService(
        @Autowired val commonDetailsRepository: CommonDetailsRepository
) {
    fun saveCommonDetails(commonDetailsModel: CommonDetailsModel) {
        val commonDetailsEntity = commonDetailsModel.firebaseUserId?.let {
            commonDetailsRepository.findByFirebaseUserId(it) }
        if(commonDetailsEntity?.isPresent == true) {
            val commonDetails = commonDetailsEntity.get()
            commonDetails.fcmToken = commonDetailsModel.fcmToken
            commonDetailsRepository.save(commonDetails)
        } else {
            commonDetailsRepository.save(CommonDetailConverter.toEntity(commonDetailsModel))
        }
    }

    fun getAppVersionDetails(): ResponseEntity<*> {
        val appConfigResponseModel = AppVersionConfigModel()
        appConfigResponseModel.apply {
            version = "1.1.3"
            mandatory = true
        }

        return ResponseEntity.ok(appConfigResponseModel)
    }
}