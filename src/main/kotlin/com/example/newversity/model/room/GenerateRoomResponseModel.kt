package com.example.newversity.model.room

import com.google.gson.annotations.SerializedName

data class GenerateRoomResponseModel (
        @SerializedName("id"             ) var id            : String?  = null,
        @SerializedName("name"           ) var name          : String?  = null,
        @SerializedName("enabled"        ) var enabled       : Boolean? = null,
        @SerializedName("description"    ) var description   : String?  = null,
        @SerializedName("customer_id"    ) var customerId    : String?  = null,
        @SerializedName("app_id"         ) var appId         : String?  = null,
        @SerializedName("recording_info" ) var recordingInfo : String?  = null,
        @SerializedName("template_id"    ) var templateId    : String?  = null,
        @SerializedName("template"       ) var template      : String?  = null,
        @SerializedName("region"         ) var region        : String?  = null,
        @SerializedName("created_at"     ) var createdAt     : String?  = null,
        @SerializedName("updated_at"     ) var updatedAt     : String?  = null,
        @SerializedName("customer"       ) var customer      : String?  = null
)
