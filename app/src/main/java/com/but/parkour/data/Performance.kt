package com.but.parkour.data

data class Performance(
    val competitorId: Int,
    val courseId: Int,
    val status: String,
    val totalTime: Int
)