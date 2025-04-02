package com.but.parkour.obstacles.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.ObstacleCreate
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel
import androidx.compose.ui.platform.LocalContext
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.components.PageTitle


class AjoutObstacles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val course = intent.getSerializableExtra("course") as? Course
        val competitionStatus = intent.getSerializableExtra("competitionStatus") as Competition.Status
        enableEdgeToEdge()
        setContent{
            Scaffold (modifier = Modifier.fillMaxSize()){ innerPadding ->
                AjoutObstaclePage(
                    modifier = Modifier.padding(innerPadding),
                    course,
                    competitionStatus
                )
            }

        }
    }
}

@Composable
fun AjoutObstaclePage(modifier: Modifier = Modifier, course: Course?, competitionStatus: Competition.Status) {
    var obstacleName by remember { mutableStateOf("") }
    val context = LocalContext.current

    if(course == null) {
        Text("Aucune course selectionnée")
    }else{
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PageTitle("Creer un obstacle")
            OutlinedTextField(
                value = obstacleName,
                onValueChange = { obstacleName = it },
                label = { Text("Nom de l'obstacle") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    onObstacleAdded(obstacleName, course, context, competitionStatus)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Ajouter")
            }
        }
    }
}

fun onObstacleAdded(obstacle: String, course: Course, context: Context, competitionStatus: Competition.Status) {
    val obstaclesViewModel = ObstaclesViewModel()
    obstaclesViewModel.addObstacle(ObstacleCreate(name = obstacle))

    val intent = Intent(context, ListeObstacles::class.java)
    intent.putExtra("parkour", course)
    intent.putExtra("competitionStatus", competitionStatus)
    context.startActivity(intent)
}
