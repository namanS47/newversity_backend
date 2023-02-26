package com.example.NewVersity.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*


abstract class AppEntity{
        @CreatedDate
        var createdAt: Date? = null

        @LastModifiedDate
        @Field(name = "modified_at")
        //TODO: @ER Mark::ZonedDateTime
        var modifiedAt: Date? = null

        @CreatedBy
        @Field(name = "created_by")
        var createdBy: String? = null

        @LastModifiedBy
        @Field(name = "modified_by")
        var modifiedBy: String? = null

        @Field(name = "is_deleted")
        var isDeleted: Boolean = false
}
