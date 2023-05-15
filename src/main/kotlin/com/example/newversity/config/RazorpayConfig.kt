package com.example.newversity.config

import com.razorpay.RazorpayClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RazorpayConfiguration {
    @Value("\${rzp_key_id}")
    private lateinit var razorpayKeyId: String

    @Value("\${rzp_key_secret}")
    private lateinit var razorpayKeySecret: String

    @Bean
    fun razorpayClient(): RazorpayClient {
        return RazorpayClient(razorpayKeyId, razorpayKeySecret)
    }

}