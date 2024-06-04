package com.example.waspas.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FarmInfor(
    @SerialName("__v")
    val __v: Int,
    @SerialName("_id")
    val _id: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("name")
    val name: String,
    @SerialName("ownerID")
    val ownerID: String,
    @SerialName("updatedAt")
    val updatedAt: String,


    val notificationCount: Int,
)