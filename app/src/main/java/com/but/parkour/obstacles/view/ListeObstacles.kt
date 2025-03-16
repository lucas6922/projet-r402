package com.but.parkour.obstacles.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.obstacles.ui.theme.ParkourTheme
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel

class ListeObstacles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val parkour = intent.getSerializableExtra("parkour") as? Course
        val parkourId = parkour?.id
        Log.d("ListeParkour", "Parkour: $parkour")
        setContent {
            com.but.parkour.ui.theme.ParkourTheme {
                val obstacleViewModel: ObstaclesViewModel = viewModel()
                parkourId?.let {
                    LaunchedEffect(it) {
                        obstacleViewModel.fetchCourses(it)
                    }
                }
                val obstacles by obstacleViewModel.obstacles.observeAsState(initial = emptyList())
                ObstaclesPage(
                    obstacles?.name ?: "Unknown",

                )
            }
        }
    }
}

@Composable
fun ObstaclesPage(parkour: String, modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ParkourTheme {
        ObstaclesPage("Android")
    }
}