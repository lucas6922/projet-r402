package com.but.parkour.competition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.ui.theme.ParkourTheme

class AjoutCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                AjtCompetPage()
            }
        }
    }
}

@Composable
fun AjtCompetPage( modifier: Modifier = Modifier) {
    Column (modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Ajouter une competition",
            modifier = Modifier.padding(32.dp)
        )

        TextField(
            value = "",
            onValueChange = {  },
            label = { Text("Nom") },
            modifier = Modifier.padding(top = 16.dp, start = 32.dp)
        )

        TextField(
            value = "",
            onValueChange = {  },
            label = { Text("Age Minimum") },
            modifier = Modifier.padding(top = 16.dp, start = 32.dp)
        )

        TextField(
            value = "",
            onValueChange = {  },
            label = { Text("Age Maximum") },
            modifier = Modifier.padding(top = 16.dp, start = 32.dp)
        )

        TextField(
            value = "",
            onValueChange = {  },
            label = { Text("Genre") },
            modifier = Modifier.padding(top = 16.dp, start = 32.dp)
        )

        TextField(
            value = "",
            onValueChange = {  },
            label = { Text("Droit de réessayer") },
            modifier = Modifier.padding(top = 16.dp, start = 32.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        AjtCompetPage()
    }
}