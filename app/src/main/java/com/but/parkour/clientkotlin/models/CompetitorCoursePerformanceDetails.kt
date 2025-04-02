package com.but.parkour.clientkotlin.models

data class CompetitorCoursePerformanceDetails(
    val competitor: Competitor,
    val performances: List<PerformanceObstacle>
)