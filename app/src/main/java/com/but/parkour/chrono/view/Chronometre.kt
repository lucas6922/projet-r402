package com.but.parkour.chrono.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.but.parkour.ui.theme.ParkourTheme
import java.util.Timer
import java.util.TimerTask

class Chronometre : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                AffichageChrono()
            }
        }
    }
}

@Composable
fun AffichageChrono(modifier: Modifier = Modifier){
    var ticks = remember { mutableStateOf(0) }

    val timer = remember {
        Timer().apply {
            val task = object : TimerTask() {
                override fun run() {
                    ticks.value++
                }
            }
            schedule(task, 1000L, 1000L)
        }
    }
    Text(text = ticks.value.toString())
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        AffichageChrono()
    }
}