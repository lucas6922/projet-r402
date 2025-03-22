package com.but.parkour.obstacles.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.obstacles.ui.theme.ParkourTheme
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel
import androidx.compose.runtime.*
import com.but.parkour.clientkotlin.models.AddCourseObstacleRequest
import com.but.parkour.clientkotlin.models.Obstacle
import okhttp3.internal.notifyAll

class ListeObstacles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val parkour = intent.getSerializableExtra("parkour") as? Course
        val competitionStatus = intent.getStringExtra("competitionStatus")
        val parkourId = parkour?.id
        Log.d("ListeParkour", "Parkour: $parkour")
        setContent {
            ParkourTheme {
                val obstacleViewModel: ObstaclesViewModel = viewModel()
                parkourId?.let {
                    LaunchedEffect(it) {
                        obstacleViewModel.fetchCoursesObstacles(it)
                    }
                }
                val obstaclesCourse by obstacleViewModel.obstaclesCourse.observeAsState(initial = emptyList())

                Scaffold (modifier = Modifier.fillMaxSize()){ innerPadding ->
                    ObstaclesPage(
                        obstacles = obstaclesCourse,
                        modifier = Modifier.padding(innerPadding),
                        parkour = parkour,
                        obstacleViewModel = obstacleViewModel,
                        competitionStatus = competitionStatus ?: ""
                    )
                }
            }
        }
    }
}

@Composable
fun ObstaclesPage(
    obstacles: List<CourseObstacle>,
    modifier: Modifier = Modifier,
    parkour: Course?,
    obstacleViewModel: ObstaclesViewModel,
    competitionStatus: String
) {
    if(parkour == null) {
        Text("Aucune course dans ce parkour")
        Log.d("ObstaclesPage", "Aucune course dans ce parkour : $parkour")
    }else{
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Obstacles",
                modifier = Modifier.padding(top = 16.dp)
            )
            ListObstacles(
                obstacles = obstacles,
                modifier = modifier.weight(1f),
                obstacleViewModel = obstacleViewModel,
                competitionStatus = competitionStatus
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (EditionMode.isEnable.value) {
                AjoutObstacle(parkour, competitionStatus)
            }
        }
    }
}

@Composable
fun ListObstacles(
    obstacles : List<CourseObstacle>,
    modifier: Modifier = Modifier,
    obstacleViewModel: ObstaclesViewModel,
    competitionStatus: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedObstacle by remember { mutableStateOf<CourseObstacle?>(null) }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(obstacles) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp)
            ) {
                Text(
                    text = item.obstacleName ?: "Unknown",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if(EditionMode.isEnable.value && competitionStatus.equals("not_ready")) {
                    Button(onClick = {
                        selectedObstacle = item
                        showDialog = true
                    }) {
                        Text("Supprimer")
                    }
                }


            }
        }
    }
    if (showDialog && selectedObstacle != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cet obstacle ?") },
            confirmButton = {
                Button(onClick = {
                    selectedObstacle?.let { onClickSupprimer(it, obstacleViewModel) }
                    showDialog = false
                }) {
                    Text("Oui")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Non")
                }
            }
        )
    }
}




@Composable
fun AjoutObstacle(parkour: Course, competitionStatus: String){

    if(competitionStatus.equals("not_ready")) {
        DropDownMenuObstacle(parkour.id)
    }
    CreerObstacleButton(parkour)
}

@Composable
fun DropDownMenuObstacle(parkourId: Int?) {
    val obstaclesViewModel: ObstaclesViewModel = viewModel()
    obstaclesViewModel.fetchAllObstacles()
    val obstacles by obstaclesViewModel.allObstacles.observeAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }
    var selectedObstacle by remember { mutableStateOf<Obstacle?>(null) }

    Log.d("ObstaclesPage", "Obstacles: $obstacles")
    Column {
        Text(
            text = selectedObstacle?.name ?: "Selectionnner un obstacle",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            obstacles.forEach { obstacle ->
                DropdownMenuItem(
                    onClick = {
                        selectedObstacle = obstacle
                        expanded = false
                    },
                    text = {
                        Text(text = obstacle.name ?: "Unknown")
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(parkourId != null){
            AjoutObstacleButton(
                selectedObstacle != null,
                parkourId,
                selectedObstacle,
                onObstacleAdded = {
                    selectedObstacle = null
                }
            )
        }
    }
}

@Composable
fun AjoutObstacleButton(
    isEnabled: Boolean,
    parkourId: Int,
    selectedObstacle: Obstacle?,
    onObstacleAdded: () -> Unit
) {
    val obstaclesViewModel: ObstaclesViewModel = viewModel()
    Button(
        onClick = {
            selectedObstacle?.id?.let { obstacleId ->
                obstaclesViewModel.addObstacleCourse(parkourId, AddCourseObstacleRequest(obstacleId))
            }
            onObstacleAdded()
        },
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Ajouter l'obstacle")
    }
}


@Composable
fun CreerObstacleButton(course: Course) {
    val context = LocalContext.current
    Button(
        onClick = {onCreerObstacleClick(context, course)},
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Creer un obstacle")
    }
}

fun onClickSupprimer(item: CourseObstacle, obstacleViewModel: ObstaclesViewModel) {
    val allObstacles = obstacleViewModel.allObstacles
    val allObstaclesList = allObstacles.value ?: emptyList()
    for (obstacle in allObstaclesList){
        if (obstacle.name == item.obstacleName){
            obstacleViewModel.removeObstacle((obstacle.id!!))
        }
    }

}

fun onCreerObstacleClick(context : Context, course: Course) {
    val intent = Intent(context, AjoutObstacles::class.java)
    intent.putExtra("course", course)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ParkourTheme {
        //ObstaclesPage("Android")
    }
}