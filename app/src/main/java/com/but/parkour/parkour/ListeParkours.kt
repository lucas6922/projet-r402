package com.but.parkour.parkour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.but.parkour.concurrents.InscriptionConcurent
import com.but.parkour.concurrents.ListeConcurrents
import com.but.parkour.obstacles.ListeObstacles
import com.but.parkour.ui.theme.ParkourTheme

class ListeParkours : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val valeur = intent.getStringExtra("item")
        setContent {
            ParkourTheme {
                ParkoursPage(valeur!!)
            }
        }
    }


}

@Composable
fun ListParkours(items: List<String>, modifier: Modifier = Modifier) {
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
                    .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = item ?: "Unknown",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(onClick = { onItemClickConcu(context, item) }) {
                        Text("Liste des concurrents")
                    }
                    Button(
                        onClick = { onItemClickListeObstacles(context, item ) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Liste des parkours")
                    }
                }

            }
        }
    }
    Button(onClick = { onClickAjouterParkour(context)}, modifier = Modifier.padding(bottom = 32.dp)) {
        Text(text = "Ajouter un parkour")
    }
}

fun onClickAjouterParkour(context: Context) {
    val intent = Intent(context, AjoutParkour::class.java)
    context.startActivity(intent)
}

fun onItemClickConcu(context : Context, item : String) {
    val intent = Intent(context, ListeConcurrents::class.java)
    intent.putExtra("item", item)
    context.startActivity(intent)
}

fun onItemClickListeObstacles(context : Context, item : String) {
    val intent = Intent(context, ListeObstacles::class.java)
    intent.putExtra("item", item)
    context.startActivity(intent)
}


@Composable
fun ParkoursPage(compet: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = compet,
            modifier = Modifier.padding(bottom = 16.dp), // Correction du Modifier
            style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ListParkours(
            items = listOf(
                "Parkour 1",
                "Parkour 2",
                "Parkour 3",
                "Parkour 4",
                "Parkour 5",
                "Parkour 6",
                "Parkour 7",
                "Parkour 8",
                "Parkour 9",
                "Parkour 10"
            ),
            modifier = Modifier.weight(1f)
        ) {
            // Handle item click
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ParkoursPreview() {
    ParkourTheme {
        ParkoursPage("euh")
    }
}



