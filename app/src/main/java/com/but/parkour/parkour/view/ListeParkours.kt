package com.but.parkour.parkour

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.but.parkour.concurrents.view.ListeConcurrents
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.obstacles.ListeObstacles
import com.but.parkour.ui.theme.ParkourTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.parkour.view.AjoutParkour

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
                ParkoursPage(
                    competition?.name ?: "Unknown",
                    courses
                )
            }
        }
    }


}

@Composable
fun ListParkours(courses: List<Course>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(courses) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        onItemClickCourse(item, context)
                    }
                    .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp)
            ) {
                Text(
                    text = item.name ?: "Unknown",
                    modifier = Modifier.padding(8.dp)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = item.name ?: "Unknown",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = { onItemClickListeObstacles(context, item) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Liste des parkours")
                    }
                }

            }
        }
    }
    Button(onClick = {onItemClickAddCourse(context)}) { Text(text = "Ajouter un parkour") }
}

fun onItemClickCourse(course: Course, context: Context) {
    Log.d("     ListeParkours", "Course: $course")
    val intent = Intent(context, ListeConcurrents::class.java)
    intent.putExtra("course", course)
    context.startActivity(intent)
}

fun onItemClickAddCourse(context: Context) {
    val intent = Intent(context, AjoutParkour::class.java)
    context.startActivity(intent)
}

fun onItemClickListeObstacles(context : Context, course : Course) {
    val intent = Intent(context, ListeObstacles::class.java)
    intent.putExtra("item", course)
    context.startActivity(intent)
}


@Composable
fun ParkoursPage(
    compet: String,
    courses: List<Course>
) {
    Column(
        modifier = Modifier
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
            modifier = Modifier.weight(1f)
        )
    }
}




