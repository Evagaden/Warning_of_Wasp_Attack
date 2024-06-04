package com.example.waspas.model

data class NotificationInfo(
    val beeDensity: Double = 0.0,
    val beeInfo: List<BeeInfo> = arrayListOf(),
    val deviceID: String = "",
    val farmID: String = "",
    val timestamp: String = "",
)
