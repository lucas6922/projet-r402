package com.but.parkour.data

data class PerformanceObstacle(
    val obstacleId: Int,
    val performanceId: Int,
    val hasFell: Boolean,
    val toVerify: Boolean,
    val time: Int
)