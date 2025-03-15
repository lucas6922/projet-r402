package com.but.parkour.concurrents.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.concurrents.ui.theme.ParkourTheme

class ListeConcurrents : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val valeur = intent.getStringExtra("item")
        setContent {
            ParkourTheme {
                ConcurrentPage(valeur!!)
            }
        }
    }
}

@Composable
fun ConcurrentPage(parkour: String, modifier: Modifier = Modifier) {
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
            text = parkour,
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    ParkourTheme {
        ConcurrentPage("Android")
    }
}



