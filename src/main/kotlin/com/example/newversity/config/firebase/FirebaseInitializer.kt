package com.example.newversity.config.firebase


import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import lombok.extern.slf4j.Slf4j
import org.apache.logging.log4j.LogManager
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

@Configuration
class FirebaseInitialization {
    init {
        initializeFirebaseAdmin()
    }

    private fun initializeFirebaseAdmin() {
        try {
            val resource: Resource = ClassPathResource("firebase_key.json")
            val inputStream = resource.inputStream
            val options = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build()
            FirebaseApp.initializeApp(options)
        } catch (e: FileNotFoundException) {
            println("Firebase key not found! Firebase Admin functionality will not work.")
        } catch (e: IOException) {
            println("Error initialising Firebase Admin! Firebase Admin functionality will not work.")
        }
    }
}


