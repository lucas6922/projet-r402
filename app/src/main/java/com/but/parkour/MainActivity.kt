package com.but.parkour

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.but.parkour.ui.theme.ParkourTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Competition()
            }
        }
    }
}

@Composable
fun Competition() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Bienvenue dans Parkour! \n Sélectionnez une competition.",
            modifier = Modifier.padding(bottom = 16.dp), // Correction du Modifier
            style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ListCompetitions(
            items = listOf(
                "Competition 1",
                "Competition 2",
                "Competition 3",
                "Competition 4",
                "Competition 5",
                "Competition 6",
                "Competition 7",
                "Competition 8",
                "Competition 9",
                "Competition 10"
            ),
            modifier = Modifier.weight(1f)
        ) {

        }
    }
}

@Composable
fun ListCompetitions(items: List<String>, modifier: Modifier = Modifier, onItemClick: (String) -> Unit) {
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
                        text = item,
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


fun onItemClickInscription(item: String,context: Context) {
    val intent = Intent(context, InscriptionConcurent::class.java)
    intent.putExtra("item", item)
    context.startActivity(intent)
}

fun onItemClickListeParkours(item: String, context: Context) {
    val intent = Intent(context, ListeParkours::class.java)
    intent.putExtra("item", item)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun CompetitionPreview() {
    ParkourTheme {
        Competition()
    }
}
