package com.but.parkour.parkour.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.parkour.ui.theme.ParkourTheme
import com.but.parkour.parkour.viewmodel.ParkourViewModel

class AjoutParkour : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    competition?.let {
                        AjtParkourPage(
                            modifier = Modifier.padding(innerPadding),
                            competition = it
                        )
                    } ?: run {
                        Text("Aucune compétition trouvée")
                    }
                }

            }
        }
    }
}

@Composable
fun AjtParkourPage(modifier: Modifier = Modifier, competition: Competition) {
    var nom by remember { mutableStateOf("") }
    var dureeMax by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Ajouter une course",
            modifier = Modifier.padding(32.dp)
        )

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.padding(top = 16.dp)
        )

        TextField(
            value = dureeMax,
            onValueChange = { dureeMax = it },
            label = { Text("Durée Maximum") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )

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
                    if (nom.isEmpty() || dureeMax.isEmpty()) {
                        errorMessage = "Tous les champs sont obligatoires"
                    } else {
                        val dureeMaxInt = dureeMax.toIntOrNull()
                        if (dureeMaxInt == null) {
                            errorMessage = "La durée doit être un nombre entier"
                        } else {
                            errorMessage = ""
                            onClickAjouterParkour(
                                nom,
                                dureeMax,
                                competition.id ?: -1,
                                context,
                                competition,
                            )
                        }
                    }
                },
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text("Ajouter")
            }
            Button(
                onClick = { onClickAnnuler(context, competition) },
                modifier = Modifier.padding(top = 32.dp, start = 64.dp)
            ) {
                Text("Annuler")
            }
        }
    }
}

fun onClickAjouterParkour(
    name : String,
    dureeMax : String,
    competitionId: Int,
    context: Context,
    competition: Competition
) {
    val course = CourseCreate(
        name = name,
        maxDuration = dureeMax.toInt(),
        competitionId = competitionId
    )

    val parkourViewModel = ParkourViewModel()
    parkourViewModel.addCourse(course)

    val intent = Intent(context, ListeParkours::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

fun onClickAnnuler(context: Context, competition: Competition) {
    val intent = Intent(context, ListeParkours::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}
