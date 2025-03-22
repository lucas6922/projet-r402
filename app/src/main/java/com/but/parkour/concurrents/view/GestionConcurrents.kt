package com.but.parkour.concurrents.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity

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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchAllCompetitors()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        PageTitle()
        AddCompetitorButton(
            onClick = {
                val intent = Intent(context, AjoutConcurrent::class.java)
                context.startActivity(intent)
            }
        )
        SearchField(searchQuery) { searchQuery = it }
        CompetitorsList(
            competitors = competitors,
            searchQuery = searchQuery,
        )
    }
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
            )
        }
    }
}


@Composable
private fun CompetitorCard(
    competitor: Competitor,
    gestionConcurrentViewModel: GestionConcurrentViewModel = viewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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

            ModifyButton(
                onClick = {
                    onModiffierConcurrent(context, competitor)
                }
            )
            DeleteButton(
                onClick = { showDeleteDialog = true }
            )

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
                        competitor.id?.let{
                            gestionConcurrentViewModel.deleteCompetitor(it)
                        }
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

private fun onModiffierConcurrent(context: Context, competitor: Competitor) {
    val intent = Intent(context, ModifierConcurrent::class.java)
    intent.putExtra("competitor", competitor)
    context.startActivity(intent)
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

@Composable
private fun AddCompetitorButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text("Ajouter un concurrent")
    }
}