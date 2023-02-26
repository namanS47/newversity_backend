package com.example.NewVersity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext


@Configuration
//@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class MongoConfig {
    @Autowired
    var mongoDbFactory: MongoDatabaseFactory? = null

    @Autowired
    var mongoMappingContext: MongoMappingContext? = null
    @Bean
    fun mappingMongoConverter(): MappingMongoConverter? {
        val dbRefResolver: DefaultDbRefResolver? = mongoDbFactory?.let { DefaultDbRefResolver(it) }
        val converter = dbRefResolver?.let { MappingMongoConverter(it, mongoMappingContext!!) }
        if (converter != null) {
            converter.setTypeMapper(DefaultMongoTypeMapper(null))
        }
        return converter
    }
}
