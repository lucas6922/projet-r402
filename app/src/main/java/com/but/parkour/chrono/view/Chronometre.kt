package com.but.parkour.chrono.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.but.parkour.parkour.viewmodel.ChronometreViewModel
import com.but.parkour.ui.theme.ParkourTheme
import kotlinx.coroutines.delay

class Chronometre : ComponentActivity() {
    private val viewModel: ChronometreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                ChronometreScreen(viewModel = viewModel, parkourId = 488) // Remplace par l'ID du parkour
            }
        }
    }
}

@Composable
fun ChronometreScreen(viewModel: ChronometreViewModel, parkourId: Int) {
    // Observing LiveData with the new 'collectAsStateWithLifecycle'
    val obstacles = viewModel.obstacles.value

    var currentObstacleIndex by remember { mutableStateOf(0) }
    var time by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    val laps = remember { mutableStateListOf<Pair<String, String>>() }

    // Charger les obstacles
    LaunchedEffect(parkourId) {
        viewModel.fetchObstacles(parkourId)
    }

    // Timer
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10L)
            time++
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (obstacles != null) {
            Text(
                text = "Obstacle : ${
                    if (obstacles.isNotEmpty() && currentObstacleIndex < obstacles.size)
                        obstacles[currentObstacleIndex]?.obstacleName
                    else "Chargement..."
                }",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Text(
            text = formatTime(time),
            style = MaterialTheme.typography.displayLarge
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Pause" else "Démarrer")
            }

            if (obstacles != null) {
                Button(
                    onClick = {
                        if (obstacles != null) {
                            if (obstacles.isNotEmpty() && currentObstacleIndex < obstacles.size) {
                                // Correction ici : gestion du nullable
                                laps.add(Pair(obstacles?.get(currentObstacleIndex)?.obstacleName ?: "Inconnu", formatTime(time)))
                                currentObstacleIndex++
                            }
                        }
                    },
                    enabled = obstacles.isNotEmpty() && currentObstacleIndex < obstacles.size
                ) {
                    Text("Tour")
                }
            }
        }

        LazyColumn {
            items(laps) { (obstacleName, lapTime) ->
                Text(text = "$obstacleName : $lapTime")
            }
        }
    }
}

fun formatTime(time: Int): String {
    val minutes = time / 6000
    val seconds = (time / 100) % 60
    val centiseconds = time % 100
    return String.format("%02d:%02d:%02d", minutes, seconds, centiseconds)
}
