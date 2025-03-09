package com.but.parkour

import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.ui.theme.ParkourTheme

class InscriptionConcurent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val valeur = intent.getStringExtra("item")
        setContent {
            ParkourTheme {
                InscriptionPage(valeur!!)
            }
        }
    }
}

@Composable
fun InscriptionPage(compet: String, modifier: Modifier = Modifier) {
    var selectedCompetitor by remember { mutableStateOf("Select a competitor") }
    var expanded by remember { mutableStateOf(false) }
    var participants by remember { mutableStateOf(listOf(
        "Competitor 1",
        "Competitor 2",
        "Competitor 3",
        "Competitor 4",
        "Competitor 5"
    )) }
    val competitors = remember { mutableStateListOf("Competitor A", "Competitor B", "Competitor C") }
    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp, bottom = 32.dp)

    ) {
        Text(
            text = compet,
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ListParticipants(
            items = participants,
            modifier = Modifier.weight(1f)
        ) {
            // Handle item click
        }

        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Text(
                text = selectedCompetitor,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
                    .clickable { expanded = true }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search competitor") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    val filteredCompetitors = competitors.filter { it.contains(searchQuery, ignoreCase = true) }
                    filteredCompetitors.forEach { competitor ->
                        DropdownMenuItem(
                            text = { Text(competitor) },
                            onClick = {
                                selectedCompetitor = competitor
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                if (selectedCompetitor != "Select a competitor") {
                    participants = participants + selectedCompetitor
                    competitors.remove(selectedCompetitor)
                    selectedCompetitor = "Select a competitor"
                    searchQuery = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Inscrire un concurrent")
        }
    }
}

@Composable
fun ListParticipants(items: List<String>, modifier: Modifier = Modifier, onItemClick: (String) -> Unit) {
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
                    .padding(4.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InscriptionPreview() {
    ParkourTheme {
        InscriptionPage("euh")
    }
}