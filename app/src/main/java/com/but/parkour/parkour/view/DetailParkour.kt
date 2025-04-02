package com.but.parkour.parkour.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.components.PageTitle
import com.but.parkour.concurrents.view.ListeConcurrentsParkour
import com.but.parkour.obstacles.view.ListeObstacles
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme

class DetailParkour : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val course = intent.getSerializableExtra("course") as Course
        val competition = intent.getSerializableExtra("competition") as Competition

        setContent {
            ParkourTheme {
                Scaffold (modifier = Modifier.fillMaxSize()){ innerPadding ->
                    DetailParkourPage(
                        modifier = Modifier.padding(innerPadding),
                        course = course,
                        competition = competition
                    )
                }
            }
        }
    }
}

@Composable
fun DetailParkourPage(modifier: Modifier = Modifier, course: Course, competition: Competition){
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){

        PageTitle("Detail de la course ${course.name}")

        CourseCard(course)

        Spacer(modifier = Modifier.height(16.dp))

        CourseActions(course, competition)
    }
}
@Composable
fun CourseCard(
    course: Course,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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

        }
    }
}



@Composable
fun CourseActions(course: Course, competition: Competition){
    val competitionStatus = competition.status
    val context = LocalContext.current




    when(competitionStatus){
        Competition.Status.not_ready -> {
            ListObstacleButton(context = context, course = course, competitionStatus = competition.status)
            if(EditionMode.isEnable.value){
                ModifButton(context = context, course = course, competition = competition)
                DeleteButton(context = context, competition = competition, course = course)
            }
        }
        Competition.Status.not_started -> {
            ListObstacleButton(context = context, course = course, competitionStatus = competition.status)
        }
        Competition.Status.started -> {
            ListObstacleButton(context = context, course = course, competitionStatus = competition.status)
            ListConcurrent(context = context, course = course, competition = competition)
        }
        Competition.Status.finished -> {

        }
        null -> {}
    }
}



@Composable
private fun ListObstacleButton(context: Context, course: Course, competitionStatus: Competition.Status?) {
    Button(
        onClick = {
            val intent = Intent(context, ListeObstacles::class.java)
            intent.putExtra("parkour", course)
            intent.putExtra("competitionStatus", competitionStatus)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Text("Obstacles")
    }
}

@Composable
private fun ListConcurrent(context: Context, course: Course, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ListeConcurrentsParkour::class.java)
            intent.putExtra("course", course)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Text("Chronometrer les concurrents")
    }
}


@Composable
private fun ModifButton(context: Context, course: Course, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ModifierCourse::class.java)
            intent.putExtra("course", course)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Text("Modifier")
    }
}

@Composable
private fun DeleteButton(context: Context, competition: Competition, course: Course) {
    var showDialog by remember { mutableStateOf(false) }

    val courseViewModel: ParkourViewModel = viewModel()

    Button(
        onClick = {
            showDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red
        )
    ) {
        Text("Supprimer la course")
    }

    if (showDialog ) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cette course ?") },
            confirmButton = {
                Button(onClick = {
                    course.id?.let{
                        courseViewModel.removeCourse(it, competition.id!!)
                        val intent = Intent(context, ListeParkours::class.java)
                        intent.putExtra("competition", competition)
                        context.startActivity(intent)
                    }
                    showDialog = false
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
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