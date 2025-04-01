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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.components.PageTitle
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme

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
    val parkourViewModel: ParkourViewModel = viewModel()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PageTitle("Créer une course")

        ParkourFormCard(
            nom = nom,
            onNomChange = { nom = it },
            dureeMax = dureeMax,
            onDureeMaxChange = { dureeMax = it }
        )

        if (errorMessage.isNotEmpty()) {
            ErrorMessage(errorMessage)
        }

        Spacer(modifier = Modifier.weight(1f))

        FormActions(
            onAdd = {
                validateAndSubmit(
                    nom = nom,
                    dureeMax = dureeMax,
                    competition = competition,
                    context = context,
                    viewModel = parkourViewModel,
                    onError = { errorMessage = it }
                )
            },
            onCancel = { onClickAnnuler(context, competition) }
        )
    }
}

@Composable
private fun ParkourFormCard(
    nom: String,
    onNomChange: (String) -> Unit,
    dureeMax: String,
    onDureeMaxChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = nom,
                onValueChange = onNomChange,
                label = { Text("Nom") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = dureeMax,
                onValueChange = onDureeMaxChange,
                label = { Text("Durée Maximum (en secondes)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun FormActions(
    onAdd: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        ) {
            Text("Annuler")
        }
        Button(
            onClick = onAdd,
            modifier = Modifier.weight(1f)
        ) {
            Text("Ajouter")
        }
    }
}

private fun validateAndSubmit(
    nom: String,
    dureeMax: String,
    competition: Competition,
    context: Context,
    viewModel: ParkourViewModel,
    onError: (String) -> Unit
) {
    when {
        nom.isEmpty() || dureeMax.isEmpty() -> {
            onError("Tous les champs sont obligatoires")
        }
        else -> {
            val dureeMaxInt = dureeMax.toIntOrNull()
            when {
                dureeMaxInt == null -> {
                    onError("La durée doit être un nombre entier")
                }
                dureeMaxInt <= 0 -> {
                    onError("La durée doit être supérieure à 0")
                }
                else -> {
                    val course = CourseCreate(
                        name = nom,
                        maxDuration = dureeMaxInt,
                        competitionId = competition.id ?: -1
                    )
                    viewModel.addCourse(course)
                    val intent = Intent(context, ListeParkours::class.java)
                    intent.putExtra("competition", competition)
                    context.startActivity(intent)
                }
            }
        }
    }
}


fun onClickAnnuler(context: Context, competition: Competition) {
    val intent = Intent(context, ListeParkours::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}
