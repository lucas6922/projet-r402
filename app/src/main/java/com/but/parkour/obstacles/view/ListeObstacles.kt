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
import androidx.compose.material3.ButtonDefaults
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
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import com.but.parkour.clientkotlin.models.AddCourseObstacleRequest
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CourseObstacleUpdate
import com.but.parkour.clientkotlin.models.Obstacle
import com.but.parkour.components.PageTitle
import com.but.parkour.ui.theme.ParkourTheme
import okhttp3.internal.notifyAll
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

class ListeObstacles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val parkour = intent.getSerializableExtra("parkour") as? Course
        val competitionStatus = intent.getSerializableExtra("competitionStatus") as Competition.Status
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
                        competitionStatus = competitionStatus
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
    competitionStatus: Competition.Status
) {

    var obstaclesList by remember { mutableStateOf(obstacles) }
    LaunchedEffect(obstacles) {
        obstaclesList = obstacles
    }
    if(parkour == null) {
        Text("Aucune course dans ce parkour")
        Log.d("ObstaclesPage", "Aucune course dans ce parkour : $parkour")
    }else{
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            PageTitle("Obstacles de la course : ${parkour.name}")
            ListObstacles(
                obstacles = obstaclesList,
                modifier = modifier.weight(1f),
                obstacleViewModel = obstacleViewModel,
                competitionStatus = competitionStatus,
                parkourId = parkour.id!!
            ){
                obstacleViewModel.fetchCoursesObstacles(parkour.id)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (EditionMode.isEnable.value) {
                AjoutObstacle(parkour, competitionStatus)
            }
        }
    }
}

@Composable
fun ListObstacles(
    obstacles: List<CourseObstacle>,
    modifier: Modifier = Modifier,
    obstacleViewModel: ObstaclesViewModel,
    competitionStatus: Competition.Status,
    parkourId: Int,
    onObstacleDeleted: () -> Unit
) {
    var reorderedObstacles by remember { mutableStateOf(obstacles) }
    val editionEnable = EditionMode.isEnable.value

    LaunchedEffect(obstacles) {
        reorderedObstacles = obstacles
    }

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                reorderedObstacles = reorderedObstacles.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            }
        },
        onDragEnd = { fromIndex, toIndex ->
            if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                reorderedObstacles.forEachIndexed { index, obstacle ->
                    val obstacleUpdate = CourseObstacleUpdate(position = index + 1, obstacleId = obstacle.obstacleId)
                    obstacleViewModel.updateObstaclePosition(parkourId, obstacleUpdate)

                }
                obstacleViewModel.fetchCoursesObstacles(parkourId)
            }
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(if (editionEnable && competitionStatus == Competition.Status.not_ready) Modifier.reorderable(state) else Modifier)
    ) {
        items(reorderedObstacles, key = { it.obstacleId!! }) { obstacle ->
            ReorderableItem(state, key = obstacle.obstacleId!!) { isDragging ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                        .padding(4.dp)
                        .then(if (editionEnable && competitionStatus == Competition.Status.not_ready) Modifier.detectReorder(state).alpha(if (isDragging) 0.5f else 1f) else Modifier)
                ) {
                    Text(
                        text = obstacle.obstacleName ?: "Unknown",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                        Button(
                            onClick = {
                                onClickSupprimer(obstacle.obstacleId!!, parkourId, obstacleViewModel, onObstacleDeleted)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Supprimer")
                        }
                    }
                }
            }
        }
    }
}





@Composable
fun AjoutObstacle(parkour: Course, competitionStatus: Competition.Status){

    if(competitionStatus == Competition.Status.not_ready) {
        DropDownMenuObstacle(parkour.id)
        CreerObstacleButton(parkour, competitionStatus)
    }
}

@Composable
fun DropDownMenuObstacle(parkourId: Int?) {
    val obstaclesViewModel: ObstaclesViewModel = viewModel()
    obstaclesViewModel.fetchCoursesObstaclesAvailable(parkourId!!)
    val obstacles by obstaclesViewModel.allObstaclesAvailable.observeAsState(initial = emptyList())

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
fun CreerObstacleButton(course: Course, competitionStatus: Competition.Status) {
    val context = LocalContext.current
    Button(
        onClick = {onCreerObstacleClick(context, course, competitionStatus)},
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Creer un obstacle")
    }
}

fun onClickSupprimer(obstacleId: Int, courseId: Int, obstacleViewModel: ObstaclesViewModel, onObstacleSup:() -> Unit) {
    obstacleViewModel.removeObstacle(obstacleId, courseId)
    onObstacleSup()
}

fun onCreerObstacleClick(context : Context, course: Course, competitionStatus: Competition.Status) {
    val intent = Intent(context, AjoutObstacles::class.java)
    intent.putExtra("course", course)
    intent.putExtra("competitionStatus", competitionStatus)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ParkourTheme {
        //ObstaclesPage("Android")
    }
}