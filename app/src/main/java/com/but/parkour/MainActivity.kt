package com.but.parkour

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.ui.theme.ParkourTheme
import androidx.compose.runtime.*

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField

import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    CompetitionScreen()
                }
            }
        }
    }
}


@Composable
fun CompetitionScreen(modifier: Modifier = Modifier) {
    var competitions by remember { mutableStateOf(emptyList<Competition>()) }

    // Lance la requête API en arrière-plan
    LaunchedEffect(Unit) {
        competitions = fetchCompetitions()
        Log.d("API", "Compétitions récupérées: $competitions")
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        for (competition in competitions) {
            Text(text = competition.name ?: "Nom inconnu", modifier = Modifier.padding(8.dp))
        }
    }
}

// Fonction suspendue pour éviter les blocages
suspend fun fetchCompetitions(): List<Competition> {
    return withContext(Dispatchers.IO) { // Exécuter la requête en arrière-plan
        try {
            val apiClient = ApiClient()
            val apiService = apiClient.createService(CompetitionsApi::class.java)
            val response = apiService.getAllCompetitions().execute()
            response.body() ?: emptyList()
        } catch (e: Exception) {
            Log.e("API", "Erreur lors de la récupération des compétitions", e)
            emptyList()
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Helo $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        Greeting("Android")
    }
}