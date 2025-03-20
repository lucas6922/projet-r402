package com.but.parkour.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Time(
    @PrimaryKey (autoGenerate = true) var id: Int,
    var courseId: Int,
    var competitorId: Int,
    var times: ArrayList<Int> = ArrayList<Int>(),
    var totalTime: Int,
)