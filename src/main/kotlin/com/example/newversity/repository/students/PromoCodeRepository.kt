package com.example.newversity.repository.students

import com.example.newversity.entity.students.PromoCodeDetails
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface PromoCodeRepository: MongoRepository<PromoCodeDetails, String> {
    fun findByPromoCodeIgnoreCase(promoCode: String): Optional<PromoCodeDetails>
}