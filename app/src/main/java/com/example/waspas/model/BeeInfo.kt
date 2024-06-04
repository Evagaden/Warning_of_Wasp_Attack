package com.example.waspas.model

data class BeeInfo(
    val bee_count: Int,
    val boxes: List<List<Double>>,
    val imageID: String
)