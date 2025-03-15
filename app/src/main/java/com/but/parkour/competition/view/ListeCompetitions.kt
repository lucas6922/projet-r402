package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.parkour.view.ListeParkours
import com.but.parkour.ui.theme.ParkourTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competitionViewModel: CompetitionViewModel = viewModel()
                val competitions by competitionViewModel.competitions.observeAsState(initial = emptyList())
                Competition(competitions)
            }
        }
    }
}

@Composable
fun Competition(competitions: List<Competition>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Bienvenue dans Parkour! \n Sélectionnez une competition.",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ListCompetitions(
            items = competitions,
            modifier = Modifier.weight(1f)
        ) {

        }
    }
}

@Composable
fun ListCompetitions(items: List<Competition>, modifier: Modifier = Modifier, onItemClick: (Competition) -> Unit) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onItemClick(item) }
                    .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = item.name ?: "Unknown",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(onClick = { onItemClickInscription(item, context) }) {
                        Text("Inscription des concurrents")
                    }
                    Button(
                        onClick = { onItemClickListeParkours(item, context) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Liste des parkours")
                    }
                }
            }
        }
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

@Preview(showBackground = true)
@Composable
fun CompetitionPreview() {
    ParkourTheme {
        Competition(
            competitions = listOf(
                Competition(name = "Competition 1"),
                Competition(name = "Competition 2")
            )
        )
    }
}