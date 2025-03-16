package com.but.parkour.parkour.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.competition.view.MainActivity
import com.but.parkour.parkour.ui.theme.ParkourTheme

class AjoutParkour : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                AjtParkourPage("")
            }
        }
    }
}

@Composable
fun AjtParkourPage(compet : String, modifier: Modifier = Modifier) {
    var nom by remember { mutableStateOf("") }
    var dureeMax by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Ajouter une competition",
            modifier = Modifier.padding(32.dp)
        )

        TextField(
            value = "",
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.padding(top = 16.dp)
        )

        TextField(
            value = dureeMax,
            onValueChange = { dureeMax = it },
            label = { Text("Durée Maximum") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )

        TextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Position dans la competition") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )
        Row() {
            Button(
                onClick = {
                    onClickAjouterParkour(
                        nom,
                        dureeMax,
                        position,
                        context
                    )
                },
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text("Ajouter")
            }
            Button(
                onClick = { onClickAnnuler(context) },
                modifier = Modifier.padding(top = 32.dp, start = 64.dp)
            ) {
                Text("Annuler")
            }
        }
    }
}

fun onClickAjouterParkour(name : String, ageMin : String, ageMax : String, context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}

fun onClickAnnuler(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ParkourTheme {
        AjtParkourPage("Android")
    }
}