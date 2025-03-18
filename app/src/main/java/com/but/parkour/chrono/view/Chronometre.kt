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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.CourseObstacle
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
                ChronometreScreen(viewModel = viewModel, parkourId = 1106, true) // à remplacer par l'ID du parkour et le hasTry de la compétition
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

    // Charge les obstacles depuis l'API
    LaunchedEffect(parkourId) {
        viewModel.fetchObstacles(parkourId)
    }

    // Timer
    LaunchedEffect(isRunning) {
        val startTime = System.currentTimeMillis() - time
        while (isRunning) {
            time = System.currentTimeMillis() - startTime
            delay(1L)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ObstacleDisplay(obstacles, currentObstacleIndex)
        ChronometerDisplay(time)
        ChronometerButtons(
            isRunning,
            onToggle = { isRunning = !isRunning },
            onReset = {
                time = 0L
                currentObstacleIndex = 0
                laps.clear()
                hasFell = false
            },
            hasTry = hasTry,
            hasFell = hasFell,
            onRestart = {
                hasFell = true
                time = if (laps.isNotEmpty()) parseTime(laps.last().second) else 0L
            },
            onLap = {
                if (obstacles != null && currentObstacleIndex < obstacles.size) {
                    laps.add(Pair(obstacles[currentObstacleIndex].obstacleName ?: "Inconnu", formatTime(time)))

                    if (currentObstacleIndex == obstacles.size - 1) {
                        isRunning = false
                    } else {
                        currentObstacleIndex++
                    }
                }
            },
            isLapEnabled = obstacles != null && currentObstacleIndex < obstacles.size
        )
        LapList(laps)
    }
}

// Affichage de l'obstacle en cours
@Composable
fun ObstacleDisplay(obstacles: List<CourseObstacle>?, currentObstacleIndex: Int) {
    Text(
        text = "Obstacle : ${obstacles?.getOrNull(currentObstacleIndex)?.obstacleName ?: "Terminé"}",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 75.dp)
    )
}

// Affichage du chronomètre
@Composable
fun ChronometerDisplay(time: Long) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formatTime(time),
            style = MaterialTheme.typography.displayLarge
        )
    }
}

// Boutons de contrôle du chronomètre
@Composable
fun ChronometerButtons(
    isRunning: Boolean,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    hasTry: Boolean,
    hasFell: Boolean,
    onRestart: () -> Unit,
    onLap: () -> Unit,
    isLapEnabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bouton "Démarrer/Pause"
            Button(
                onClick = onToggle,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isRunning) "Pause" else "Démarrer")
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Bouton "Tour" (si le chrono tourne) ou "Réinitialiser" (si en pause)
            Button(
                onClick = if (isRunning) onLap else onReset,
                enabled = if (isRunning) isLapEnabled else true,
                colors = if (isRunning) ButtonDefaults.buttonColors() else ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    if (isRunning) "Tour" else "Réinitialiser",
                    color = if (!isRunning) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Réserve toujours de l'espace pour éviter que la liste des temps ne bouge
        Box(modifier = Modifier.height(48.dp), contentAlignment = Alignment.Center) {
            if (hasTry && !isRunning) {
                Button(
                    onClick = onRestart,
                    enabled = !hasFell,
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Recommencer obstacle")
                }
            }
        }
    }
}


// Liste des temps (laps)
@Composable
fun LapList(laps: List<Pair<String, String>>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(laps) { (obstacleName, lapTime) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = obstacleName, style = MaterialTheme.typography.bodyLarge)
                    Text(text = lapTime, style = MaterialTheme.typography.bodyLarge)
                }
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

// Convertit un string en temps (nombre de millisecondes)
fun parseTime(timeString: String): Long {
    val parts = timeString.split(":").map { it.toIntOrNull() ?: 0 }
    return if (parts.size == 3) {
        (parts[0] * 60000L) + (parts[1] * 1000L) + parts[2]
    } else {
        0L
    }
}
