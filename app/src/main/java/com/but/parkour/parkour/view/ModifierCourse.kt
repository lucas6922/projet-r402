package com.but.parkour.parkour.view

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseUpdate
import com.but.parkour.components.PageTitle
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme

class ModifierCourse : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val course = intent.getSerializableExtra("course") as Course
        val competition = intent.getSerializableExtra("competition") as Competition

        setContent {
            ParkourTheme {
                Scaffold { innerPadding ->
                    ModifierCoursePage(
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
fun ModifierCoursePage(modifier: Modifier, course: Course, competition: Competition) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        PageTitle("Modifier la course ${course.name}")

        ModifierCourseForm(course, competition)
    }
}



@Composable
private fun ModifierCourseForm(course: Course, competition: Competition){
    var nom by remember { mutableStateOf(course.name ?: "") }
    var duree by remember { mutableStateOf(course.maxDuration?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val parkourViewModel: ParkourViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            isError = errorMessage.isNotEmpty() && nom.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = duree,
            onValueChange = { duree = it },
            label = { Text("Durée (en secondes)") },
            isError = errorMessage.isNotEmpty() && !isValidDuration(duree),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val validationResult = validateFields(nom, duree)
                    if (validationResult == null) {
                        val updatedCourse = CourseUpdate(
                            name = nom,
                            maxDuration = duree.toInt()
                        )
                        parkourViewModel.updateCourse(course.id!!, updatedCourse)

                        val newCourse = course.copy(
                            name = nom,
                            maxDuration = duree.toInt()
                        )
                        val intent = Intent(context, DetailParkour::class.java)
                        intent.putExtra("course", newCourse)
                        intent.putExtra("competition", competition)
                        context.startActivity(intent)
                    } else {
                        errorMessage = validationResult
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Enregistrer")
            }

            Button(
                onClick = { activity.finish() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Annuler")
            }
        }
    }
}


private fun validateFields(nom: String, duree: String): String? {
    return when {
        nom.isBlank() -> "Le nom est requis"
        duree.isBlank() -> "La durée est requise"
        !isValidDuration(duree) -> "La durée doit être un nombre entier positif"
        else -> null
    }
}

private fun isValidDuration(duree: String): Boolean {
    return try {
        val duration = duree.toInt()
        duration > 0
    } catch (e: NumberFormatException) {
        false
    }
}