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
                ChronometreScreen(viewModel = viewModel, parkourId = 1106, true) // Remplace par l'ID du parkour
            }
        }
    }
}

@Composable
fun ChronometreScreen(viewModel: ChronometreViewModel, parkourId: Int, hasTry: Boolean) {
    val obstacles = viewModel.obstacles.value
    var hasFell by remember { mutableStateOf(false) }

    var currentObstacleIndex by remember { mutableStateOf(0) }
    var time by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    val laps = remember { mutableStateListOf<Pair<String, String>>() }

    // Charger les obstacles
    LaunchedEffect(parkourId) {
        viewModel.fetchObstacles(parkourId)
    }

    // Timer
    LaunchedEffect(isRunning) {
        val startTime = System.currentTimeMillis() - time
        while (isRunning) {
            val elapsed = System.currentTimeMillis() - startTime
            time = elapsed
            delay(1L) // Mise à jour chaque milliseconde
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
                        obstacles[currentObstacleIndex].obstacleName
                    else "Terminé"
                }",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Text(
            text = formatTime(time),
            style = MaterialTheme.typography.displayLarge
        )

        // Première ligne de boutons (Démarrer/Pause, Tour/Effacer)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Pause" else "Démarrer")
            }

            if (obstacles != null) {
                if (isRunning) {
                    Button(
                        onClick = {
                            laps.add(Pair(obstacles[currentObstacleIndex].obstacleName ?: "Inconnu", formatTime(time)))

                            // Vérifier si c'est le dernier obstacle
                            if (currentObstacleIndex == obstacles.size - 1) {
                                isRunning = false
                            } else {
                                currentObstacleIndex++
                            }
                        },
                        enabled = obstacles.isNotEmpty() && currentObstacleIndex < obstacles.size
                    ) {
                        Text("Tour")
                    }
                } else {
                    Button(
                        onClick = {
                            time = 0L
                            currentObstacleIndex = 0
                            laps.clear()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Effacer", color = MaterialTheme.colorScheme.onError)
                    }

                    if (hasTry) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                hasFell = true
                                if (laps.isNotEmpty()) {
                                    val lastLapTime = laps.last().second
                                    time = parseTime(lastLapTime)
                                } else {
                                    time = 0L
                                }
                            }, enabled = !hasFell
                        ) {
                            Text("Recommencer l'obstacle")
                        }
                    }
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

// Convertit un temps en String
fun formatTime(time: Long): String {
    val minutes = (time / 60000) % 60
    val seconds = (time / 1000) % 60
    val milliseconds = time % 1000
    return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
}

// Convertit un un string en temps (nombre de millisecondes)
fun parseTime(timeString: String): Long {
    val parts = timeString.split(":").map { it.toIntOrNull() ?: 0 }
    return if (parts.size == 3) {
        (parts[0] * 60000L) + (parts[1] * 1000L) + parts[2]
    } else {
        0L
    }
}
