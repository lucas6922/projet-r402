package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.components.PageTitle
import com.but.parkour.concurrents.view.GestionConcurrents
import com.but.parkour.ui.theme.ParkourTheme
import kotlinx.coroutines.delay

class ListeCompetitions : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competitionViewModel: CompetitionViewModel = viewModel()
                val competitions by competitionViewModel.competitions.observeAsState(initial = emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CompetitionPage(
                        modifier = Modifier.padding(innerPadding),
                        competitions = competitions
                    )

                }
            }
        }
    }
}

@Composable
fun CompetitionPage(
    modifier: Modifier = Modifier,
    competitions: List<Competition>,
) {
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(500)
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {

        PageTitle("Bienvenue dans Parkour !")

        EditionMode()

        if(isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }else if(competitions.isEmpty()){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucune competition",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }else{
            ListCompetitions(
                items = competitions,
                modifier = Modifier.weight(1f)
            )
        }


        if(EditionMode.isEnable.value) {
            EditionModeEnable(context)
        }

    }
}

@Composable
fun EditionMode() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
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
}

@Composable
fun EditionModeEnable(context: Context){
    Column{
        Button(
            onClick = { onClickAjouterCompetition(context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ajouter une competition"
            )
        }

        Button(
            onClick = { onGestionConcurrents(context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Gerer les concurrents"
            )
        }

    }
}


@Composable
fun ListCompetitions(
    items: List<Competition>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(items) { competition ->
                CompetitionCard(
                    competition = competition,
                )
            }

        }
    }
}


@Composable
fun CompetitionCard(
    competition: Competition,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(2.dp, Color.Black, shape = MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = competition.name ?: "Nom inconnu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    val genre = when (competition.gender) {
                        Competition.Gender.H -> "Homme"
                        Competition.Gender.F -> "Femme"
                        else -> "Non défini"
                    }
                    Text(
                        text = "Age minimal: ${competition.ageMin} ans",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Age maximal: ${competition.ageMax} ans",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Genre: $genre",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    val status = when (competition.status) {
                        Competition.Status.not_ready -> "Non prête"
                        Competition.Status.not_started -> "Non commencée"
                        Competition.Status.started-> "Commencée"
                        Competition.Status.finished -> "Terminée"
                        else -> "Non défini"
                    }
                    Text(
                        text = "Status: $status",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Chute" + if (competition.hasRetry == true) " autorisée" else " non autorisée",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = {onItemClickDetails(competition, context)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Voir détails")
            }
        }
    }
}


fun onItemClickDetails(competition: Competition, context: Context) {
    val intent = Intent(context, DetailsCompetition::class.java)
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

fun onGestionConcurrents(context: Context) {
    val intent = Intent(context, GestionConcurrents::class.java)
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