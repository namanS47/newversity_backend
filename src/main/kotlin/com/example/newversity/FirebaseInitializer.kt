package com.example.newversity


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


@Slf4j
@Configuration
class FirebaseServiceCredential {
    private val log = LogManager.getLogger(javaClass)

    @Throws(IOException::class)
    fun firebaseConnect() {
        try {
            val serviceAccount = FileInputStream("resources/firebase_config.json")
            val options = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://charity.firebaseio.com/")
                    .build()
            FirebaseApp.initializeApp(options)
            FirebaseDatabase.getInstance(FirebaseApp.getInstance()).setPersistenceEnabled(true)
        } catch (e: Exception) {
            log.info("Trying to login to firebase failed. Reason: " + e.message)
        }
    }
}

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


