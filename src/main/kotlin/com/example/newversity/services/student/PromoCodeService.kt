package com.example.newversity.services.student

import com.example.newversity.model.student.PromoCodeConvertor
import com.example.newversity.model.student.PromoCodeModel
import com.example.newversity.repository.students.PromoCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PromoCodeService(
        @Autowired val promoCodeRepository: PromoCodeRepository
) {
    fun addPromoCode(promoCodeModel: PromoCodeModel): ResponseEntity<*> {
        val entity = promoCodeRepository.save(PromoCodeConvertor.toEntity(promoCodeModel))
        return ResponseEntity.ok(entity)
    }

    fun verifyPromoCode(promoCode: String): ResponseEntity<*> {
        val promoCodeEntity = promoCodeRepository.findByPromoCodeIgnoreCase(promoCode)
        return if(promoCodeEntity.isPresent) {
            ResponseEntity.ok(PromoCodeConvertor.toModel(promoCodeEntity.get()))
        } else {
            ResponseEntity.badRequest().body("Invalid promo code")
        }
    }
}