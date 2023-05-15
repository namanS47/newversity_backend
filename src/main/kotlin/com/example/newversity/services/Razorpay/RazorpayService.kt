package com.example.newversity.services.Razorpay

import com.example.newversity.config.RazorpayConfiguration
import com.example.newversity.model.EmptyJsonResponse
import com.example.newversity.model.payment.OrderRequestModel
import com.example.newversity.model.payment.PaymentOrderModel
import com.example.newversity.model.room.GenerateRoomResponseModel
import com.google.gson.Gson
import com.razorpay.Order
import com.razorpay.RazorpayException
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RazorpayService(
        @Autowired val razorpayConfiguration: RazorpayConfiguration
) {
    fun createOrder(orderDetails: OrderRequestModel): ResponseEntity<*> {
        return try {
            val orderRequest = JSONObject()
            orderRequest.put("amount", orderDetails.amount) // amount in the smallest currency unit
            orderRequest.put("currency", "INR")
            orderRequest.put("receipt", orderDetails.availabilityId)

            val notes = JSONObject()
            notes.put("student_id", orderDetails.userId)
            notes.put("availability_id", orderDetails.availabilityId)

            orderRequest.put("notes", notes)

            val order: Order = razorpayConfiguration.razorpayClient().orders.create(orderRequest)
            val orderModel = Gson().fromJson(order.toString(), PaymentOrderModel::class.java)

            ResponseEntity.ok().body(orderModel)
        } catch (e: RazorpayException) {
            println(e.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("status" to "Couldn't create order"))
        }
    }
}