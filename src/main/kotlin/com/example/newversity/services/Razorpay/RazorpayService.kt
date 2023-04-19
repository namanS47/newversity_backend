package com.example.newversity.services.Razorpay

import com.example.newversity.config.RazorpayConfiguration
import com.example.newversity.model.payment.OrderRequestModel
import com.razorpay.Order
import com.razorpay.RazorpayException
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RazorpayService {
    fun createOrder(orderDetails: OrderRequestModel): ResponseEntity<*> {
        return try {
            val orderRequest = JSONObject()
            orderRequest.put("amount", orderDetails.amount) // amount in the smallest currency unit
            orderRequest.put("currency", "INR")
            orderRequest.put("receipt", orderDetails.sessionId)
            val order: Order = RazorpayConfiguration().razorpayClient().orders.create(orderRequest)
            ResponseEntity.ok().body(order)
        } catch (e: RazorpayException) {
            println(e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("status" to "Couldn't create order"))
        }
    }
}