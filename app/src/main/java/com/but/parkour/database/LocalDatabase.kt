package com.but.parkour.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Time::class],
    views = [],
    version = 1
)

abstract class Base : RoomDatabase() {
    abstract fun timeDAO(): TimeDAO

    companion object {
        fun getInstance(context: Context): Base = lazy {
            Room.databaseBuilder(
                context,
                Base::class.java, "modules.sqlite"
            ).build()
        }.value
    }
}