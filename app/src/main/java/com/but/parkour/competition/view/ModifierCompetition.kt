package com.but.parkour.competition.view

import android.content.Intent
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import com.but.parkour.clientkotlin.models.CompetitionUpdate.Gender
import com.but.parkour.ui.theme.ParkourTheme
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.components.PageTitle

class ModifierCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val competition = intent.getSerializableExtra("competition") as? Competition
        setContent {
            ParkourTheme {
                Scaffold (modifier = Modifier.fillMaxSize()){innerPadding ->
                    competition?.let {
                        ModifierCompetitionForm(
                            modifier = Modifier.padding(innerPadding),
                            oldCompetition = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModifierCompetitionForm(modifier: Modifier = Modifier, oldCompetition: Competition) {
    var name by remember { mutableStateOf(oldCompetition.name ?: "") }
    var ageMin by remember { mutableStateOf(oldCompetition.ageMin?.toString() ?: "") }
    var ageMax by remember { mutableStateOf(oldCompetition.ageMax?.toString() ?: "") }
    var gender by remember { mutableStateOf(oldCompetition.gender?.value ?: "") }
    var hasRetry by remember { mutableStateOf(oldCompetition.hasRetry ?: false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        PageTitle("Modifier la competition ${oldCompetition.name}")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ageMin,
            onValueChange = { ageMin = it },
            label = { Text("Age Min") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ageMax,
            onValueChange = { ageMax = it },
            label = { Text("Age Max") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Genre")
        Row {
            RadioButton(
                selected = gender == "H",
                onClick = { gender = "H" }
            )
            Text("Homme")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = gender == "F",
                onClick = { gender = "F" }
            )
            Text("Femme")
        }

        Text("Auroiser une chute ?")
        Row {
            RadioButton(
                selected = hasRetry,
                onClick = { hasRetry = true }
            )
            Text("Oui")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = !hasRetry,
                onClick = { hasRetry = false }
            )
            Text("Non")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = androidx.compose.ui.graphics.Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val validationResult = validateFields(name, ageMin, ageMax, gender)
                if (validationResult == null) {
                    val newCompetition = CompetitionUpdate(
                        name = name,
                        ageMin = ageMin.toIntOrNull(),
                        ageMax = ageMax.toIntOrNull(),
                        gender = Gender.values().find { it.value == gender },
                        hasRetry = hasRetry,
                    )
                    oldCompetition.id?.let {
                        modifCompetition(context, newCompetition, it)
                    }
                } else {
                    errorMessage = validationResult
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Modifier la competition")
        }
    }
}

fun validateFields(name: String, ageMin: String, ageMax: String, gender: String): String? {
    if (name.isBlank() || ageMin.isBlank() || ageMax.isBlank() || gender.isBlank() ) {
        return "Tous les champs sont obligatiores"
    }
    if (gender != "H" && gender != "F") {
        return "Le genre doit être Homme ou Femme"
    }
    return null
}

fun modifCompetition(context: Context, competition: CompetitionUpdate, competitionId: Int) {
    val competitionViewModel = CompetitionViewModel()
    competitionViewModel.updateCompetition(competitionId, competition)

    val intent = Intent(context, ListeCompetitions::class.java)
    context.startActivity(intent)
}