package com.but.parkour

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object EditionMode {
    var isEnable: MutableState<Boolean> = mutableStateOf(false)
}
