package com.example.newversity.repository

import com.example.newversity.entity.common.CommonDetails
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CommonDetailsRepository: MongoRepository<CommonDetails, String> {
    fun findByFirebaseUserId(firebaseUserId: String): Optional<CommonDetails>
}