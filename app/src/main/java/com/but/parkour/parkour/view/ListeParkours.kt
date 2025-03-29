package com.but.parkour.parkour.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.concurrents.view.ListeConcurrentsParkour
import com.but.parkour.obstacles.view.ListeObstacles
import com.but.parkour.ui.theme.ParkourTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Course

class ListeParkours : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        val competitionId = competition?.id
        Log.d("ListeParkour", "Competition: $competition")
        setContent {
            ParkourTheme {
                val parkourViewModel : ParkourViewModel = viewModel()
                competitionId?.let {
                    LaunchedEffect(it) {
                        parkourViewModel.fetchCourses(it)
                    }
                }
                val courses by parkourViewModel.parkours.observeAsState(initial = emptyList())
                Scaffold (modifier = Modifier.fillMaxSize()){ innerPadding ->
                    ParkoursPage(
                        modifier = Modifier.padding(innerPadding),
                        competition?.name ?: "Unknown",
                        courses,
                        competition
                    )
                }
            }
        }
    }
}


@Composable
fun ParkoursPage(
    modifier: Modifier = Modifier,
    compet: String,
    courses: List<Course>,
    competition: Competition?
) {

    if(competition == null) {
        Text("Aucune compétition trouvée")
    }else{
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 32.dp)
        ) {
            Text(
                text = compet,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ListParkours(
                courses = courses,
                modifier = Modifier.weight(1f),
                competition = competition
            )
        }
    }
}




@Composable
fun ListParkours(
    courses: List<Course>,
    modifier: Modifier = Modifier,
    competition: Competition
) {
    Log.d("ListeParkours", "competition: $competition")
    val context = LocalContext.current

    val competitionStatus = competition.status

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(courses) { course ->

            CourseCard(
                course = course,
                onDetailsClick = {
                    onCourseDetailsClick(context, course, competition)
                }
            )
        }
    }

    if(EditionMode.isEnable.value && competitionStatus == Competition.Status.not_ready) {
        Button(
            onClick = {onItemClickAddCourse(context, competition)},
            modifier = Modifier.fillMaxWidth()
            ) {
            Text(text = "Ajouter une course")
        }
    }
}



@Composable
fun CourseCard(
    course: Course,
    onDetailsClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(2.dp, Color.Black, shape = MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = course.name ?: "Nom inconnu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Position: ${course.position ?: "Non définie"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Durée max: ${course.maxDuration ?: "Non définie"} sec",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (course.isOver == true) "Terminée" else "En cours",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = { onDetailsClick(course) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Voir détails")
            }
        }
    }
}


fun onCourseDetailsClick(context: Context, course: Course, competition: Competition) {
    val intent = Intent(context, DetailParkour::class.java)
    intent.putExtra("course", course)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

fun onItemClickAddCourse(context: Context, competition: Competition) {
    val intent = Intent(context, AjoutParkour::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}










