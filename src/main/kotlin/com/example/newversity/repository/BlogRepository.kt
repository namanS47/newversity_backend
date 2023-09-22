package com.example.newversity.repository

import com.example.newversity.entity.common.BlogDetails
import org.springframework.data.mongodb.repository.MongoRepository

interface BlogRepository : MongoRepository<BlogDetails, String>