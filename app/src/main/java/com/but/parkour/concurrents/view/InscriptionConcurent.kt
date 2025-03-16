package com.but.parkour.concurrents.view

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
import com.but.parkour.ui.theme.ParkourTheme


class InscriptionConcurent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        val competitionId = competition?.id
        Log.d("InscriptionConcurent", "Competition: $competition")
        setContent {
            ParkourTheme {
                val competitorViewModel: CompetitorViewModel = viewModel()
                competitionId?.let {
                    LaunchedEffect(it) {
                        competitorViewModel.fetchCompetitorsInscrit(it)
                        competitorViewModel.fetchUnregisteredCompetitors(it)
                    }
                }
                val competitors by competitorViewModel.competitors.observeAsState(initial = emptyList())
                val unregisteredCompetitors by competitorViewModel.unregisteredCompetitors.observeAsState(initial = emptyList())
                InscriptionPage(
                    competition?.name ?: "Unknown",
                    competitors,
                    unregisteredCompetitors,
                    competitorViewModel,
                    competitionId,
                    competition
                )
            }
        }
    }
}

@Composable
fun InscriptionPage(
    compet: String,
    competitorsInscrit: List<Competitor>,
    unregisteredCompetitors: List<Competitor>,
    competitorViewModel: CompetitorViewModel,
    competitionId: Int?,
    competition: Competition?
) {
    Log.d("InscriptionPage", "Competitors inscrit dans la course: $competitorsInscrit")
    var selectedCompetitor by remember { mutableStateOf<Competitor?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var participants by remember { mutableStateOf(emptyList<Competitor>()) }
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(competitorsInscrit) {
        participants = competitorsInscrit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp, bottom = 32.dp)
    ) {
        HeaderText(compet)
        Spacer(modifier = Modifier.height(8.dp))
        ListParticipants(
            concurrents = participants,
            modifier = Modifier.weight(1f),
            onItemClick = {}
        )
        CompetitorDropdown(
            selectedCompetitor = selectedCompetitor,
            expanded = expanded,
            searchQuery = searchQuery,
            competitors = unregisteredCompetitors,
            onCompetitorSelected = { competitor ->
                selectedCompetitor = competitor
                expanded = false
            },
            onSearchQueryChanged = { query ->
                searchQuery = query
            },
            onExpandedChanged = { isExpanded ->
                expanded = isExpanded
            },
            onCreateNewCompetitor = {
                competition?.let{
                    onAjoutConcurrentClick(context, competition)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InscriptionButton(
            selectedCompetitor = selectedCompetitor,
            onInscription = {
                selectedCompetitor?.let { competitor ->
                    competitionId?.let { id ->
                        if(competitor.id != null){
                            competitorViewModel.registerCompetitor(id, competitor.id)
                            selectedCompetitor = null
                            searchQuery = ""
                        }
                    }
                }
            }
        )
    }
}

private fun onAjoutConcurrentClick(context: Context, competition: Competition) {
    val intent = Intent(context, AjoutConcurrent::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

@Composable
fun HeaderText(compet: String) {
    Text(
        text = compet,
        modifier = Modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
    )
}

@Composable
fun CompetitorDropdown(
    selectedCompetitor: Competitor?,
    expanded: Boolean,
    searchQuery: String,
    competitors: List<Competitor>,
    onCompetitorSelected: (Competitor) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    onCreateNewCompetitor: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        Text(
            text = selectedCompetitor?.firstName ?: "Select a competitor",
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .padding(16.dp)
                .clickable { onExpandedChanged(true) }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChanged(false) }
        ) {
            Column {
                Button(
                    onClick = {
                        onCreateNewCompetitor()
                        onExpandedChanged(false)
                    },
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Text(text = "Creer un nouveau concurrent")
                }
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = { Text("Search competitor") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                val filteredCompetitors = competitors.filter { it.firstName?.contains(searchQuery, ignoreCase = true) == true }
                filteredCompetitors.forEach { competitor ->
                    DropdownMenuItem(
                        text = { Text(competitor.firstName ?: "Unknown") },
                        onClick = { onCompetitorSelected(competitor) }
                    )
                }
            }
        }
    }
}
@Composable
fun ListParticipants(
    concurrents: List<Competitor>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(concurrents) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onItemClick(item.firstName ?: "Unknown") }
                    .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp)
            ) {
                Text(
                    text = item.firstName ?: "Unknown",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun InscriptionButton(
    selectedCompetitor: Competitor?,
    onInscription: () -> Unit
) {
    Button(
        onClick = onInscription,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Inscrire un concurrent")
    }
}

