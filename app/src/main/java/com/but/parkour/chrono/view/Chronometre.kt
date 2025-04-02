package com.but.parkour.chrono.view

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.clientkotlin.models.CourseUpdate
import com.but.parkour.clientkotlin.models.PerformanceCreate
import com.but.parkour.clientkotlin.models.PerformanceObstacleCreate
import com.but.parkour.components.PageTitle
import com.but.parkour.parkour.viewmodel.ChronometreViewModel
import com.but.parkour.ui.theme.ParkourTheme
import kotlinx.coroutines.delay

class Chronometre : ComponentActivity() {
    private val viewModel: ChronometreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val competition = intent.getSerializableExtra("competition") as Competition
            val competitor = intent.getSerializableExtra("competitor") as Competitor
            val course = intent.getSerializableExtra("course") as Course
            ParkourTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    course.id?.let { competition.hasRetry?.let { it2 ->
                        ChronometreScreen(
                            viewModel = viewModel,
                            parkourId = it,
                            hasRetry = it2,
                            competitor = competitor,
                            course = course,
                            competitionId = competition.id,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } }
                }

            }
        }
    }
}

@Composable
fun ChronometreScreen(
    viewModel: ChronometreViewModel,
    parkourId: Int,
    hasRetry: Boolean,
    competitor: Competitor,
    course: Course,
    competitionId: Int?,
    modifier: Modifier
) {
    val obstacles = viewModel.obstacles.value
    var hasFell by remember { mutableStateOf(false) }
    var lastLapTime by remember { mutableStateOf(0L) }
    var isFinished by remember { mutableStateOf(false) }
    var fallenObstacleIndex by remember { mutableStateOf(-1) }
    var currentObstacleIndex by remember { mutableStateOf(0) }
    var time by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) } // Ajout de l'état pour la confirmation
    val laps = remember { mutableStateListOf<Pair<String, String>>() }
    val context = LocalContext.current

    LaunchedEffect(parkourId) {
        viewModel.fetchObstacles(parkourId)
    }

    LaunchedEffect(isRunning) {
        val startTime = System.currentTimeMillis() - time
        while (isRunning) {
            time = System.currentTimeMillis() - startTime
            delay(1L)
        }
    }


    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        PageTitle("Chronometrer le concurrent : ${competitor.firstName} pour la course ${course.name}")

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
                lastLapTime = 0L
                isFinished = false
                isSaved = false
            },
            hasRetry = hasRetry,
            hasFell = hasFell,
            isFinished = isFinished,
            onRestart = {
                fallenObstacleIndex = currentObstacleIndex
                hasFell = true
                if (laps.isNotEmpty()) {
                    time = laps.sumOf { parseTime(it.second) }
                } else {
                    time = 0L
                }
            },
            onLap = {
                if (obstacles != null && currentObstacleIndex < obstacles.size) {
                    val lapTime = time - lastLapTime
                    laps.add(Pair(obstacles[currentObstacleIndex].obstacleName ?: "Inconnu", formatTime(lapTime)))
                    lastLapTime = time

                    if (currentObstacleIndex == obstacles.size - 1) {
                        isRunning = false
                        isFinished = true
                    } else {
                        currentObstacleIndex++
                    }
                }
            },
            isLapEnabled = obstacles != null && currentObstacleIndex < obstacles.size
        )

        if (!isRunning && !isFinished && !isSaved) {
            Button(
                onClick = {
                    enregistrerPerformance(
                        course.id ?: 0,
                        competitor.id!!,
                        competitionId,
                        time.toInt(),
                        true,
                        laps,
                        fallenObstacleIndex,
                        context,
                        viewModel
                    ) {
                        isSaved = true
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Arrêter et enregistrer")
            }
        }

        if (isFinished && !isSaved) {
            Button(
                onClick = {
                    val totalTime = laps.sumOf { parseTime(it.second).toInt() }
                    enregistrerPerformance(
                        course.id ?: 0,
                        competitor.id!!,
                        competitionId,
                        totalTime,
                        false,
                        laps,
                        fallenObstacleIndex,
                        context,
                        viewModel
                    ) {
                        isSaved = true
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Enregistrer les performances")
            }
        }

        if (isSaved) {
            Text(
                text = "Performances enregistrées avec succès !",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LapList(laps)
    }
}



// Affichage de l'obstacle en cours
@Composable
fun ObstacleDisplay(obstacles: List<CourseObstacle>?, currentObstacleIndex: Int) {
    Text(
        text = "Obstacle : ${obstacles?.getOrNull(currentObstacleIndex)?.obstacleName ?: "Chargement ..."}",
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
    hasRetry: Boolean,
    hasFell: Boolean,
    onRestart: () -> Unit,
    onLap: () -> Unit,
    isLapEnabled: Boolean,
    isFinished: Boolean
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
                enabled = !isFinished,
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

        Box(modifier = Modifier.height(48.dp), contentAlignment = Alignment.Center) {
            if (hasRetry && !isRunning) {
                Button(
                    onClick = onRestart,
                    enabled = !hasFell && !isFinished,
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
    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        // Légende
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Obstacle", style = MaterialTheme.typography.labelLarge)
            Text(text = "Temps", style = MaterialTheme.typography.labelLarge)
        }

        // Liste des tours
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
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
}




fun enregistrerPerformance(
    courseId: Int,
    competitorId: Int,
    competitionId: Int?,
    totalTime: Int,
    failed : Boolean,
    laps: List<Pair<String, String>>,
    fallenObstacleIndex: Int,
    context: Context,
    chronoModel: ChronometreViewModel,
    onComplete: () -> Unit
) {
    var status = PerformanceCreate.Status.to_finish
    if(failed){
        status = PerformanceCreate.Status.over
    }

    val perf = PerformanceCreate(courseId = courseId, competitorId = competitorId, status = status, totalTime = totalTime)
    chronoModel.addPerformance(perf)

    chronoModel.fetchAllPerformances()
    chronoModel.performances.observeForever { perfs ->
        perfs?.find { it.courseId == courseId && it.competitorId == competitorId }?.let { newPerf ->
            val performanceId = newPerf.id ?: return@let

            chronoModel.obstacles.value?.let { obstaclesList ->
                for ((index, lap) in laps.withIndex()) {
                    val obstacleId = obstaclesList.getOrNull(index)?.obstacleId ?: 0
                    val timeInMs = parseTime(lap.second).toInt()
                    Log.d("Chronometre", "Tombé à : $fallenObstacleIndex")
                    Log.d("Chronometre", "Index à : $index")

                    enregistrerPerformanceObstacles(
                        obstacleId = obstacleId,
                        performanceId = performanceId,
                        hasFell = (index == fallenObstacleIndex),
                        toVerify = false,
                        time = timeInMs,
                        context = context,
                        chronoModel = chronoModel
                    )
                }
            }
            chronoModel.fetchCompetitorsCourse(competitionId?: 0)
            chronoModel.competitorsCourse.observeForever { competitors ->
                if (competitors != null && competitors.isNotEmpty()) {
                    val totalCompetitors = competitors.size
                    val recordedPerformances = perfs.count { it.courseId == courseId }

                    Log.d("Chronometre", "Total competitors: $totalCompetitors, Recorded performances: $recordedPerformances")

                    if (recordedPerformances >= totalCompetitors) {
                        Log.d("Chronometre", "Course terminée")
                        val course = CourseUpdate(isOver = true)
                        chronoModel.updateCourse(courseId, course)
                    }
                } else {
                    Log.d("Chronometre", "competitorsCourse est vide ou non encore chargé courseId : $courseId")
                }
            }
            onComplete()
        }
    }
}

fun enregistrerPerformanceObstacles(
    obstacleId: Int,
    performanceId: Int,
    hasFell: Boolean,
    toVerify: Boolean,
    time: Int,
    context: Context,
    chronoModel: ChronometreViewModel
) {
    Log.d("Chronometre", "hasFell : $hasFell")
    val perfObstacle = PerformanceObstacleCreate(obstacleId, performanceId, hasFell, toVerify, time)
    chronoModel.addPerformanceObstacle(perfObstacle)
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
