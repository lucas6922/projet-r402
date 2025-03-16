package com.but.parkour.concurrents.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CompetitorCreate
import com.but.parkour.clientkotlin.models.CompetitorCreate.Gender
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
import com.but.parkour.ui.theme.ParkourTheme
import java.time.LocalDate
import java.util.Locale

class AjoutConcurrent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val competition = intent.getSerializableExtra("competition") as? Competition
        setContent {
            ParkourTheme {
                AjoutConcurrentForm(competition)
            }
        }
    }
}

@Composable
fun AjoutConcurrentForm(competition: Competition?) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Genre") }
    var genderExpanded by remember { mutableStateOf(false) }
    var bornAt by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Ajouter un concurrent", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Prenom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Numero de telephone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = selectedGender,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
                    .clickable { genderExpanded = true }
            )
            DropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Homme") },
                    onClick = {
                        selectedGender = "Homme"
                        genderExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Femme") },
                    onClick = {
                        selectedGender = "Femme"
                        genderExpanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = bornAt,
            onValueChange = { bornAt = it },
            label = { Text("Date de naissance  (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Button(
            onClick = {
                competition?.let {
                    errorMessage = ""
                    val verif = verifChamps(
                        firstName,
                        lastName,
                        email,
                        phone,
                        selectedGender,
                        bornAt,
                    )
                    if(verif.isNotEmpty()){
                        errorMessage = verif
                    }else{

                        onAjoutCompetitorClick(
                            firstName,
                            lastName,
                            email,
                            phone,
                            selectedGender,
                            bornAt,
                            competition,
                            context,
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter")
        }
    }
}

private fun verifChamps(
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    gender: String,
    bornAt: String,
): String{
    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
        || phone.isEmpty() || gender == "Genre" || bornAt.isEmpty()){
        return "Tous les champs sont obligatoire"
    }

    if (phone.toIntOrNull() == null || phone.length != 10) {
        return "Le numéro de téléphone doit être valide"
    }

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$".toRegex()
    if (!email.matches(emailRegex)) {
        return "L'adresse email n'est pas valide"
    }

    var validDate: Boolean
    try {
        val date = LocalDate.parse(bornAt)
        validDate = true
    }catch (e: Exception){
        validDate = false
    }

    if(!validDate){
        return "La date doit être valide au format 'YYYY-MM-DD'"
    }

    return ""
}
fun onAjoutCompetitorClick(
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    gender: String,
    bornAt: String,
    competition: Competition,
    context: Context
){
    val competitor = CompetitorCreate(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        gender = when(gender) {
            "Homme" -> Gender.H
            "Femme" -> Gender.F
            else -> throw IllegalArgumentException("Genre invalide")
        },
        bornAt = LocalDate.parse(bornAt)
    )

    val competitorViewModel = CompetitorViewModel()
    competition.id?.let {
        competitorViewModel.addCompetitor(competitor, competition.id)
    }

    val intent = Intent(context, InscriptionConcurent::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        //AjoutConcurrentForm()
    }
}