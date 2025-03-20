package com.but.parkour.database.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import com.but.parkour.database.Base
import com.but.parkour.database.Time

class TimeViewModel(app: Application): AndroidViewModel(app) {
    val base = Base.getInstance(app)
    val dao = base.timeDAO()

    fun getTimes(): Cursor {
        return dao.getAll()
    }

    fun insert(time: Time){
        dao.insert(time)
    }

    fun update(time: Time){
        dao.update(time)
    }
}