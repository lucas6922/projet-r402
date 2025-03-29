package com.but.parkour.concurrents.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
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

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InscriptionPage(
                        modifier = Modifier.padding(innerPadding),
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
}

@Composable
fun InscriptionPage(
    modifier: Modifier = Modifier,
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

    val competitionStatus = competition?.status


    LaunchedEffect(competitorsInscrit) {
        participants = competitorsInscrit
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp, bottom = 32.dp)
    ) {
        HeaderText(compet)
        Spacer(modifier = Modifier.height(8.dp))
        ListParticipants(
            concurrents = participants,
            modifier = Modifier.weight(1f),
            competitionId = competitionId,
            competitorViewModel = competitorViewModel,
        )

        //si la competition est not_ready ou not started on peut ajouter un concurrent
        if(competitionStatus == Competition.Status.not_ready
            || competitionStatus == Competition.Status.not_started) {
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
            )


            Spacer(modifier = Modifier.height(16.dp))
            InscriptionButton(
                selectedCompetitor = selectedCompetitor,
                onInscription = {
                    selectedCompetitor?.let { competitor ->
                        competitionId?.let { id ->
                            if (competitor.id != null) {
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

) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        Text(
            text = selectedCompetitor?.firstName ?: "Selectionner un concurrent",
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
    onChronoClick: ((Competitor) -> Unit)? = null,
    competitionId: Int?,
    competitorViewModel: CompetitorViewModel?,
) {
    var selectedCompetitor by remember { mutableStateOf<Competitor?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(concurrents) { competitor ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(
                        text = "${competitor.firstName} ${competitor.lastName}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if(EditionMode.isEnable.value) {
                        Button(onClick = {
                                selectedCompetitor = competitor
                                showDialog = true
                            },colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) { Text("Supprimer") }
                    }
                    if (onChronoClick != null) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onChronoClick(competitor) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Chronométrer")
                            }
                        }
                    }
                }
            }
        }
    }
    if(showDialog && selectedCompetitor != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cet obstacle ?") },
            confirmButton = {
                Button(onClick = {
                        selectedCompetitor?.let { onClickSupprimerCompetitor(it, competitionId, competitorViewModel!!) }
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

fun onClickSupprimerCompetitor(
    it: Competitor,
    competitionId: Int?,
    competitorViewModel: CompetitorViewModel
) {
    competitorViewModel.unregisterCompetitior(competitionId!!, it.id!!)
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

