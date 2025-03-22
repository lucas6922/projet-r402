package com.but.parkour.concurrents.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.but.parkour.concurrents.ui.theme.ParkourTheme
import com.but.parkour.concurrents.viewmodel.GestionConcurrentViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competitor
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.but.parkour.EditionMode

class GestionConcurrents : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GestionConcurrentsPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GestionConcurrentsPage(
    modifier: Modifier = Modifier,
    viewModel: GestionConcurrentViewModel = viewModel(),
) {
    val competitors by viewModel.competitors.observeAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedCompetitor by remember { mutableStateOf<Competitor?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllCompetitors()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        PageTitle()
        SearchField(searchQuery) { searchQuery = it }
        CompetitorsList(
            competitors = competitors,
            searchQuery = searchQuery,
            onModifyClick = { competitor ->
                selectedCompetitor = competitor
                showDialog = true
            }
        )
    }

    ModifyCompetitorDialog(
        showDialog = showDialog,
        selectedCompetitor = selectedCompetitor,
        onDismiss = { showDialog = false }
    )
}


@Composable
private fun PageTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Liste des concurrents",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Composable
private fun SearchField(
    searchQuery: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onValueChange,
        label = { Text("Rechercher un concurrent") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}


@Composable
private fun CompetitorsList(
    competitors: List<Competitor>,
    searchQuery: String,
    onModifyClick: (Competitor) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(
            competitors.filter {
                it.firstName?.contains(searchQuery, ignoreCase = true) == true ||
                it.lastName?.contains(searchQuery, ignoreCase = true) == true
            }
        ) { competitor ->
            CompetitorCard(
                competitor = competitor,
                onModifyClick = onModifyClick
            )
        }
    }
}


@Composable
private fun CompetitorCard(
    competitor: Competitor,
    onModifyClick: (Competitor) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(2.dp, Color.Black, MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            CompetitorInfo(competitor)

            if(EditionMode.isEnable.value) {
                ModifyButton(
                    onClick = { onModifyClick(competitor) }
                )
                DeleteButton(
                    onClick = { showDeleteDialog = true }
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer ce concurrent ?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}


@Composable
private fun CompetitorInfo(competitor: Competitor) {
    Text(
        text = "${competitor.firstName} ${competitor.lastName}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = "Email: ${competitor.email ?: "Non renseigné"}")
    Text(text = "Téléphone: ${competitor.phone ?: "Non renseigné"}")
    Text(text = "Genre: ${if (competitor.gender == Competitor.Gender.H) "Homme" else "Femme"}")
    Text(text = "Date de naissance: ${competitor.bornAt ?: "Non renseignée"}")
}


@Composable
private fun ModifyButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text("Modifier")
    }
}



@Composable
private fun ModifyCompetitorDialog(
    showDialog: Boolean,
    selectedCompetitor: Competitor?,
    onDismiss: () -> Unit
) {
    if (showDialog && selectedCompetitor != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Modifier un concurrent") },
            text = { Text("Cette fonctionnalité sera bientôt disponible") },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        )
    ) {
        Text("Supprimer")
    }
}