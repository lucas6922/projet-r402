package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.parkour.view.ListeParkours
import com.but.parkour.parkour.view.onClickSupprimerParkour
import com.but.parkour.ui.theme.ParkourTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competitionViewModel: CompetitionViewModel = viewModel()
                val competitions by competitionViewModel.competitions.observeAsState(initial = emptyList())

                Competition(competitions, competitionViewModel)
            }
        }
    }
}

@Composable
fun Competition(competitions: List<Competition>, competitionViewModel: CompetitionViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = "Bienvenue dans Parkour! \n Sélectionnez une competition.",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ListCompetitions(
            items = competitions,
            modifier = Modifier.weight(1f),
            competitionViewModel = competitionViewModel
        )

        if(EditionMode.isEnable.value) {
            Button(
                onClick = { onClickAjouterCompetition(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ajouter une competition"
                )
            }
        }

    }
}

@Composable
fun ListCompetitions(
    items: List<Competition>,
    modifier: Modifier = Modifier,
    competitionViewModel: CompetitionViewModel
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Activer le mode édition",
            )
            RadioButton(
                selected = EditionMode.isEnable.value,
                onClick = {
                    EditionMode.isEnable.value = !EditionMode.isEnable.value
                },
            )
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(items) { competition ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = competition.name ?: "Nom inconnu",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(onClick = { onItemClickInscription(competition, context) }) {
                            Text("Inscription des concurrents")
                        }
                        Button(
                            onClick = { onItemClickListeParkours(competition, context) },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Liste des parkours")
                        }

                        if(EditionMode.isEnable.value) {
                            Button(
                                onClick = { onModifierCompetition(context, competition) },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Modifier la competition")
                            }
                        }

                        Button(
                            onClick = {
                                selectedCompetition = item
                                showDialog = true
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Supprimer")
                        }
                    }
                }
            }

        }
    }
    if (showDialog && selectedCompetition != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cet obstacle ?") },
            confirmButton = {
                Button(onClick = {
                    selectedCompetition?.let { onClickSupprimerCompetition(it, competitionViewModel) }
                    showDialog = false
                }) {
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

fun onItemClickInscription(competition: Competition, context: Context) {
    val intent = Intent(context, InscriptionConcurent::class.java)
    Log.d("ListeCompetitions", "Competition: $competition")
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

fun onItemClickListeParkours(competition: Competition, context: Context) {
    val intent = Intent(context, ListeParkours::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

fun onClickSupprimerCompetition(competition: Competition, competitionViewModel: CompetitionViewModel) {
    competitionViewModel.removeCompetition(competition.id!!)
}

fun onClickAjouterCompetition(context: Context) {
    val intent = Intent(context, AjoutCompetition::class.java)
    context.startActivity(intent)
}

fun onModifierCompetition(context: Context, competition: Competition){
    val intent = Intent(context, ModifierCompetition::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}




@Preview(showBackground = true)
@Composable
fun CompetitionPreview() {
    ParkourTheme {
        Competition(
//            competitions = listOf(
//                Competition(name = "Competition 1"),
//                Competition(name = "Competition 2")
//            )
        )
    }
}