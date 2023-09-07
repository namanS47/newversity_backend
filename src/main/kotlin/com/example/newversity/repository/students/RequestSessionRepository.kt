package com.example.newversity.repository.students

import com.example.newversity.entity.common.RequestSessionDetails
import org.springframework.data.mongodb.repository.MongoRepository

interface RequestSessionRepository: MongoRepository<RequestSessionDetails, String>