package com.example.newversity.services.firebase

import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.util.HashMap

@Service
class FirebaseDynamicLinkService {
    fun generateMentorLink(mentorId: String): String{
        val restTemplate = RestTemplate()
        val headers = LinkedMultiValueMap<String, String>()

        headers["Content-Type"] = MediaType.APPLICATION_JSON.toString()
        val firebaseWebApiKey = "AIzaSyDN9Thg-pJn84qGKBBBtz2IzDG0NHq9uA4"

        val map: HashMap<String, Any> = HashMap()
        val longDynamicLink = "https://newversity.page.link/?link=https://www.newversity.in/student/mentor/$mentorId&apn=com.newversity.android&ibi=com.newversity.ios"
        map["longDynamicLink"] = longDynamicLink

        val uri = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=$firebaseWebApiKey"

        val entity: HttpEntity<Map<String, Any>> = HttpEntity(map, headers)
        val response = restTemplate.exchange(uri, HttpMethod.POST, entity, String::class.java)
        print(response)
        return ""
//        return Gson().fromJson(response.body, PhonePeTransactionStatusResponseModel::class.java)
    }
}