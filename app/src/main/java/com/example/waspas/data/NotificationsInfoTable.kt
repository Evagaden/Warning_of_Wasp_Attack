package com.example.waspas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotificationsInfo")
data class NotificationsInfoTable(
    @PrimaryKey(autoGenerate = true)
    val order: Int = 0,
    val check: Boolean = false,
    val beeDensity: Double = 0.0,
    val beeInfo: String = "",
    val deviceID: String = "",
    val farmID: String = "",
    val timestamp: String = "",
)
