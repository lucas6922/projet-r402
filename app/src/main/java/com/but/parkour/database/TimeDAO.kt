package com.but.parkour.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimeDAO {
    @Insert fun insert(vararg time: Time)
    //timeDAO.insert(Time(courseId=x,competitorId=x,times=ArrayList))

    @Update fun update(vararg time: Time)
    //timeDAO.update(Time(id=id, times=ArrayList))

    @Query("SELECT * FROM time") fun getAll() : Cursor
}