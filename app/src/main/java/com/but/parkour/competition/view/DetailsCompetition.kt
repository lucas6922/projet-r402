package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.ui.theme.ParkourTheme
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.parkour.view.ListeParkours

class DetailsCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val competition = intent.getSerializableExtra("competition") as Competition

        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailsCompetitionPage(
                        competition = competition,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun DetailsCompetitionPage(
    competition: Competition,
    modifier: Modifier = Modifier
) {
    Log.d("DetailsCompetitionPage", "Competition: $competition")

    var currentCompetition by remember { mutableStateOf(competition) }
    val competitionViewModel: CompetitionViewModel = viewModel()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PageTitle()
        }
        CompetitionDetailsCard(currentCompetition)
        Spacer(modifier = Modifier.height(16.dp))
        CompetitionActions(currentCompetition){
            updatedCompetition -> currentCompetition = updatedCompetition
        }
    }
}

@Composable
fun PageTitle(){
    Text(
        text = "Détails de la compétition",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@Composable
private fun CompetitionDetailsCard(competition: Competition) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = competition.name ?: "Nom inconnu",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val genre = when (competition.gender) {
                Competition.Gender.H -> "Homme"
                Competition.Gender.F -> "Femme"
                else -> "Non défini"
            }

            val status = when (competition.status) {
                Competition.Status.not_ready -> "Non prête"
                Competition.Status.not_started -> "Non commencée"
                Competition.Status.started -> "Commencée"
                Competition.Status.finished -> "Terminée"
                else -> "Non défini"
            }

            Text("Age minimal: ${competition.ageMin} ans")
            Text("Age maximal: ${competition.ageMax} ans")
            Text("Genre: $genre")
            Text("Status: $status")
            Text("Chute" + if (competition.hasRetry == true) " autorisée" else " non autorisée")
        }
    }
}


@Composable
private fun CompetitionActions(
    competition: Competition,
    onCompetitionUpdate: (Competition) -> Unit
) {
    val context = LocalContext.current
    val status = competition.status

    when (status) {
        Competition.Status.not_ready -> {
            //peut supprimer la compétition
            //peut inscrire des concurrents
            ConcurrentButton(context, competition)
            ParkoursButton(context, competition)
            ValiderCompetitionButton(competition, onCompetitionUpdate)
            //peut modifier la compétition et ses courses
            if (EditionMode.isEnable.value) {
                ModifyButton(context, competition)
                DeleteButton(context, competition)
            }
        }
        Competition.Status.not_started -> {
            ConcurrentButton(context, competition)
            ParkoursButton(context, competition)
            StartCompetition(competition, onCompetitionUpdate)
            //peut inscrire des concurrents
        }
        Competition.Status.started -> {
            ConcurrentButton(context, competition)
            ParkoursButton(context, competition)
            //aucune modif autorisée
        }
        Competition.Status.finished -> {
            ViewResultsButton()
            //on peut que consulter les resultats
        }
        null -> {}
    }
}

@Composable
fun ValiderCompetitionButton(
    competition: Competition,
    onCompetitionUpdate: (Competition) -> Unit
) {
    val competitionViewModel: CompetitionViewModel = viewModel()

    val competValid = CompetitionUpdate(
        status = CompetitionUpdate.Status.not_started
    )

    Button(
        onClick = {
            competitionViewModel.updateCompetition(competition.id!!, competValid)
            val updatedCompetition = competition.copy(status = Competition.Status.not_started)
            onCompetitionUpdate(updatedCompetition)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text("Valider les parcours")
    }

}


@Composable
fun StartCompetition(
    competition: Competition,
    onCompetitionUpdate: (Competition) -> Unit
) {
    val competitionViewModel: CompetitionViewModel = viewModel()

    val competValid = CompetitionUpdate(
        status = CompetitionUpdate.Status.started
    )

    Button(
        onClick = {
            competitionViewModel.updateCompetition(competition.id!!, competValid)
            val updatedCompetition = competition.copy(status = Competition.Status.started)
            onCompetitionUpdate(updatedCompetition)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text("Commencer la competition")
    }

}

@Composable
fun ViewResultsButton() {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text("Afficher les resultats")
    }
}


@Composable
private fun ModifyButton(context: Context, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ModifierCompetition::class.java)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text("Modifier la compétition")
    }
}

@Composable
private fun ConcurrentButton(context: Context, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, InscriptionConcurent::class.java)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text("Concurrents")
    }
}

@Composable
private fun ParkoursButton(context: Context, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ListeParkours::class.java)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text("Courses")
    }
}

@Composable
private fun DeleteButton(context: Context, competition: Competition) {
    var showDialog by remember { mutableStateOf(false) }

    val competitionViewModel: CompetitionViewModel = viewModel()

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
        Text("Supprimer la compétition")
    }

    if (showDialog ) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cette competition ?") },
            confirmButton = {
                Button(onClick = {
                    competition.id?.let{
                        competitionViewModel.removeCompetition(it)
                        val intent = Intent(context, ListeCompetitions::class.java)
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

