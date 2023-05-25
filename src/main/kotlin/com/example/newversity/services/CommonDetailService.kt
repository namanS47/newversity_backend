package com.example.newversity.services

import com.example.newversity.model.CommonDetailConverter
import com.example.newversity.model.CommonDetailsModel
import com.example.newversity.repository.CommonDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
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
}