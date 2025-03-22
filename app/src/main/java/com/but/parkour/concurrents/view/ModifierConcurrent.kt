package com.but.parkour.concurrents.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.concurrents.viewmodel.GestionConcurrentViewModel
import com.but.parkour.ui.theme.ParkourTheme
import java.time.LocalDate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.but.parkour.clientkotlin.models.CompetitorUpdate


class ModifierConcurrent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competitor = intent.getSerializableExtra("competitor") as? Competitor
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ModifierConcurrentPage(
                        competitor = competitor,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun ModifierConcurrentPage(
    competitor: Competitor?,
    modifier: Modifier = Modifier
) {
    if (competitor == null) {
        Text("Erreur: Concurrent non trouvé")
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ConcurrentTitle(competitor = competitor)
        ModifierConcurrentForm(competitor = competitor)
    }
}



@Composable
fun ConcurrentTitle(competitor: Competitor) {
    Text(
        text = "Modifier le concurrent ${competitor.firstName} ${competitor.lastName}",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun ModifierConcurrentForm(
    competitor: Competitor?,
    gestionConcurrentViewModel: GestionConcurrentViewModel = viewModel()
) {
    var firstName by remember { mutableStateOf(competitor?.firstName ?: "") }
    var lastName by remember { mutableStateOf(competitor?.lastName ?: "") }
    var email by remember { mutableStateOf(competitor?.email ?: "") }
    var phone by remember { mutableStateOf(competitor?.phone ?: "") }
    var bornAt by remember { mutableStateOf(competitor?.bornAt?.toString() ?: "") }

    var errorMessageGlobal by remember { mutableStateOf("") }

    val context = LocalContext.current

    OutlinedTextField(
        value = firstName,
        onValueChange = { firstName = it },
        label = { Text("Prénom") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    OutlinedTextField(
        value = lastName,
        onValueChange = { lastName = it },
        label = { Text("Nom") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Téléphone") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    OutlinedTextField(
        value = bornAt,
        onValueChange = { bornAt = it },
        label = { Text("Date de naissance") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    if(errorMessageGlobal.isNotBlank()) {
        Text(
            text = errorMessageGlobal,
            color = Color.Red,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    Button(
        onClick = {
            val errorMessage = validateForm(firstName, lastName, email, phone, bornAt)
            if(errorMessage.isEmpty()) {
                val concurrentUpdate = CompetitorUpdate(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    bornAt = LocalDate.parse(bornAt)
                )
                competitor?.id?.let {
                    gestionConcurrentViewModel.updateCompetitor(it, concurrentUpdate)
                }

                onModifierConcurrentClick(context)
            } else {
                errorMessageGlobal = errorMessage
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text("Modifier")
    }

}

fun onModifierConcurrentClick(context: Context) {
    context.startActivity(Intent(context, GestionConcurrents::class.java))
}


fun validateForm(
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    bornAt: String
): String {
    if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || bornAt.isEmpty()) {
        return "Veuillez remplir tous les champs"
    }

    if(!email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
        return "Email invalide"
    }

    if (phone.isNotBlank() && !phone.matches(Regex("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}\$"))) {
        return "Numéro de téléphone invalide"
    }

    try{
        LocalDate.parse(bornAt)
    } catch (e: Exception) {
        return "Date de naissance invalide"
    }

    return ""
}