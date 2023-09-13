package com.example.newversity.repository

import com.example.newversity.entity.common.WebinarDetails
import org.springframework.data.mongodb.repository.MongoRepository

interface WebinarRepository: MongoRepository<WebinarDetails, String>