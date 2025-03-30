package com.but.parkour.classement.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData

import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.classement.view.ui.theme.ParkourTheme
import com.but.parkour.classement.viewmodel.PerformanceViewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.clientkotlin.models.Obstacle
import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel
import com.but.parkour.parkour.view.ListeParkours
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import kotlinx.coroutines.time.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class Classement : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competition = intent.getSerializableExtra("competition") as Competition

                val performanceViewModel: PerformanceViewModel = viewModel()
                val performances by performanceViewModel.performances.observeAsState(initial = emptyList())

                val competitionId = competition.id

                val parkourViewModel: ParkourViewModel = viewModel()
                competitionId?.let {
                    LaunchedEffect(it) {
                        parkourViewModel.fetchCourses(it)
                    }
                }
                val parkours by parkourViewModel.parkours.observeAsState(initial = emptyList())
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClassementPage(
                        modifier = Modifier.padding(innerPadding),
                        performances = performances,
                        parkours = parkours,
                        viewModel = performanceViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ClassementPage(modifier : Modifier, performances: List<Performance>, parkours: List<Course>, viewModel: PerformanceViewModel) {
    var parkoursExpanded by remember { mutableStateOf(false) }
    var selectedParkour by remember { mutableStateOf<Course?>(null) }
    var obstacleExpanded by remember { mutableStateOf(false) }
    var selectedObstacle by remember { mutableStateOf<CourseObstacle?>(null) }
    var classement by remember { mutableStateOf<Map<Competitor, Int>>(emptyMap()) }
    var obstacles by remember { mutableStateOf<List<CourseObstacle>>(emptyList()) }
    val obstaclesViewModel = ObstaclesViewModel()
    if(selectedParkour != null) {
        LaunchedEffect(selectedParkour) {
            obstaclesViewModel.fetchCoursesObstacles(selectedParkour!!.id!!)
        }
        obstacles = obstaclesViewModel.obstaclesCourse.value?: emptyList()
            LaunchedEffect(selectedParkour) {
            classement = viewModel.filterCompetitorsWithPerformance(selectedParkour!!)
        }
    }
    else {
        LaunchedEffect(performances) {
            classement = viewModel.filterCompetitorsWithCompetition(parkours)
        }
    }
    Column(modifier) {
        Row {
            Text(
                text = "Parkour : ",
                modifier = Modifier.padding(16.dp)
            )
            Card {
                Text(
                    text = selectedParkour?.name ?: "All",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { parkoursExpanded = true }
                        .width(64.dp),

                    )
                DropdownMenu(
                    expanded = parkoursExpanded,
                    onDismissRequest = { parkoursExpanded = false },
                    modifier = Modifier.width(128.dp)

                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            selectedParkour = null
                            parkoursExpanded = false
                        }
                    )
                    parkours.forEach { parkour ->
                        DropdownMenuItem(
                            text = { Text(parkour.name ?: "") },
                            onClick = {
                                selectedParkour = parkour
                                parkoursExpanded = false
                            }
                        )
                    }
                }
            }
            Text(
                text = "Obstacle : ",
                modifier = Modifier.padding(16.dp)
            )
            Card {
                Text(
                    text = selectedObstacle?.obstacleName ?: "All",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { obstacleExpanded = true }
                        .width(64.dp),

                    )
                DropdownMenu(
                    expanded = obstacleExpanded,
                    onDismissRequest = { obstacleExpanded = false },
                    modifier = Modifier.width(128.dp)

                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            selectedObstacle = null
                            obstacleExpanded = false
                        }
                    )
                    obstacles.forEach { obstacle ->
                        DropdownMenuItem(
                            text = { Text(obstacle.obstacleName ?: "") },
                            onClick = {
                                selectedObstacle = obstacle
                                obstacleExpanded = false
                            }
                        )
                    }
                }
            }
        }
        Classement(Modifier.padding(16.dp), concurrents = classement)
    }
}

@Composable
fun Classement(modifier : Modifier, concurrents: Map<Competitor, Int>) {
    if(concurrents.isEmpty()) {
        Text(
            text = "Aucun concurrent trouvé",
            modifier = Modifier.padding(8.dp)
        )
        return
    }
    var i =1
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp).border(1.dp, Color.Gray)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            items(concurrents.toList()) { concurrent ->
                Text(
                    text = "$i : ${concurrent.first.firstName} ${concurrent.first.lastName} - temps total : ${
                        formatTime(
                            concurrent.second
                        )
                    }",
                    modifier = Modifier.padding(8.dp)
                )
                i += 1
            }
        }
    }

}

@SuppressLint("DefaultLocale")
fun formatTime(milliseconds: Int): String {
    var seconds = milliseconds / 1000
    val minutes = seconds / 60
    seconds %= 60
    val remainingMillis = milliseconds % 1000
    return String.format("%d'%02d'%03d", minutes, seconds, remainingMillis)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ParkourTheme {
    }
}