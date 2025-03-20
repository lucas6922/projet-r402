package com.but.parkour.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Time(
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    var courseId: Int = 0,
    var competitorId: Int = 0,
    var times: ArrayList<Int> = ArrayList<Int>(),
)