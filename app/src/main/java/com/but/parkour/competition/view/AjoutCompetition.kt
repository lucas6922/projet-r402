package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.ui.theme.ParkourTheme

class AjoutCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                AjtCompetPage()
            }
        }
    }
}

@Composable
fun AjtCompetPage(modifier: Modifier = Modifier) {
    var nom by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Plusieurs essais ?") }
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("Genre") }
    var genderExpanded by remember { mutableStateOf(false) }
    var ageMin by remember { mutableStateOf("") }
    var ageMax by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Ajouter une competition",
            modifier = Modifier.padding(32.dp)
        )

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.padding(top = 16.dp)
        )

        TextField(
            value = ageMin,
            onValueChange = { ageMin = it },
            label = { Text("Age Minimum") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )

        TextField(
            value = ageMax,
            onValueChange = { ageMax = it },
            label = { Text("Age Maximum") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )

        Box(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = selectedGender,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
                    .clickable { genderExpanded = true }
            )
            DropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Homme") },
                    onClick = {
                        selectedGender = "Homme"
                        genderExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Femme") },
                    onClick = {
                        selectedGender = "Femme"
                        genderExpanded = false
                    }
                )
            }
        }

        Box(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = selectedOption,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Oui") },
                    onClick = {
                        selectedOption = "Oui"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Non") },
                    onClick = {
                        selectedOption = "Non"
                        expanded = false
                    }
                )
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Row {
            Button(
                onClick = {
                    if (nom.isEmpty() || ageMin.isEmpty() || ageMax.isEmpty() || selectedGender == "Genre" || selectedOption == "Plusieurs essais ?") {
                        errorMessage = "Tous les champs sont obligatoires"
                    } else {
                        val ageMinInt = ageMin.toIntOrNull()
                        val ageMaxInt = ageMax.toIntOrNull()
                        if (ageMinInt == null || ageMaxInt == null) {
                            errorMessage = "L'âge minimum et maximum doivent être des nombres entiers"
                        } else {
                            errorMessage = ""
                            onClickAjouterCompetition(
                                nom,
                                ageMin,
                                ageMax,
                                selectedGender,
                                selectedOption,
                                context
                            )
                        }
                    }
                },
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text("Ajouter")
            }
            Button(
                onClick = { onClickAnnuler(context) },
                modifier = Modifier.padding(top = 32.dp, start = 64.dp)
            ) {
                Text("Annuler")
            }
        }
    }
}

fun onClickAjouterCompetition(name: String, ageMin: String, ageMax: String, genderReceive: String, multipleAttempts: String, context: Context) {
    val competition = CompetitionCreate(
        name = name,
        ageMin = ageMin.toInt(),
        ageMax = ageMax.toInt(),
        gender = when(genderReceive) {
            "Homme" -> CompetitionCreate.Gender.H
            "Femme" -> CompetitionCreate.Gender.F
            else -> throw IllegalArgumentException("Genre invalide")
        },
        hasRetry = when(multipleAttempts) {
            "Oui" -> true
            "Non" -> false
            else -> throw IllegalArgumentException("Option invalide")
        }
    )

    val competitionViewModel = CompetitionViewModel()
    competitionViewModel.addCompetition(competition)
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

fun onClickAnnuler(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        AjtCompetPage()
    }
}