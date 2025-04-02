package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.ui.theme.ParkourTheme
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import com.but.parkour.components.PageTitle


class AjoutCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AjtCompetPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun AjtCompetPage(modifier: Modifier = Modifier) {
    var nom by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Plusieurs essais ?") }
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("Genre") }
    var genderExpanded by remember { mutableStateOf(false) }
    var ageMin by remember { mutableStateOf("") }
    var ageMax by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val competitionViewModel: CompetitionViewModel = viewModel()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        PageTitle("Ajouter une competition")

        CompetitionFormCard(
            nom = nom,
            onNomChange = { nom = it },
            ageMin = ageMin,
            onAgeMinChange = { ageMin = it },
            ageMax = ageMax,
            onAgeMaxChange = { ageMax = it },
            selectedGender = selectedGender,
            onGenderSelect = { selectedGender = it },
            genderExpanded = genderExpanded,
            onGenderExpandedChange = { genderExpanded = it },
            selectedOption = selectedOption,
            onOptionSelect = { selectedOption = it },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        )

        if (errorMessage.isNotEmpty()) {
            ErrorMessage(errorMessage)
        }

        Spacer(modifier = Modifier.weight(1f))

        FormActions(
            onAdd = {
                validateAndSubmit(
                    nom = nom,
                    ageMin = ageMin,
                    ageMax = ageMax,
                    gender = selectedGender,
                    option = selectedOption,
                    context = context,
                    viewModel = competitionViewModel,
                    onError = { errorMessage = it }
                )
            },
            onCancel = { onClickAnnuler(context) }
        )
    }
}


@Composable
private fun CompetitionFormCard(
    nom: String,
    onNomChange: (String) -> Unit,
    ageMin: String,
    onAgeMinChange: (String) -> Unit,
    ageMax: String,
    onAgeMaxChange: (String) -> Unit,
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    genderExpanded: Boolean,
    onGenderExpandedChange: (Boolean) -> Unit,
    selectedOption: String,
    onOptionSelect: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = nom,
                onValueChange = onNomChange,
                label = { Text("Nom") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = ageMin,
                    onValueChange = onAgeMinChange,
                    label = { Text("Age Minimum") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                TextField(
                    value = ageMax,
                    onValueChange = onAgeMaxChange,
                    label = { Text("Age Maximum") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            SelectionDropdowns(
                selectedGender = selectedGender,
                onGenderSelect = onGenderSelect,
                genderExpanded = genderExpanded,
                onGenderExpandedChange = onGenderExpandedChange,
                selectedOption = selectedOption,
                onOptionSelect = onOptionSelect,
                expanded = expanded,
                onExpandedChange = onExpandedChange
            )
        }
    }
}

@Composable
private fun SelectionDropdowns(
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    genderExpanded: Boolean,
    onGenderExpandedChange: (Boolean) -> Unit,
    selectedOption: String,
    onOptionSelect: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Genre",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onGenderSelect("Homme") }
                    ) {
                        RadioButton(
                            selected = selectedGender == "Homme",
                            onClick = { onGenderSelect("Homme") }
                        )
                        Text("Homme")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onGenderSelect("Femme") }
                    ) {
                        RadioButton(
                            selected = selectedGender == "Femme",
                            onClick = { onGenderSelect("Femme") }
                        )
                        Text("Femme")
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Plusieurs essais ?",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onOptionSelect("Oui") }
                    ) {
                        RadioButton(
                            selected = selectedOption == "Oui",
                            onClick = { onOptionSelect("Oui") }
                        )
                        Text("Oui")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onOptionSelect("Non") }
                    ) {
                        RadioButton(
                            selected = selectedOption == "Non",
                            onClick = { onOptionSelect("Non") }
                        )
                        Text("Non")
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun FormActions(
    onAdd: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        ) {
            Text("Annuler")
        }
        Button(
            onClick = onAdd,
            modifier = Modifier.weight(1f)
        ) {
            Text("Ajouter")
        }
    }
}

private fun validateAndSubmit(
    nom: String,
    ageMin: String,
    ageMax: String,
    gender: String,
    option: String,
    context: Context,
    viewModel: CompetitionViewModel,
    onError: (String) -> Unit
) {
    when {
        nom.isEmpty() || ageMin.isEmpty() || ageMax.isEmpty() ||
                gender == "Genre" || option == "Plusieurs essais ?" -> {
            onError("Tous les champs sont obligatoires")
        }
        else -> {
            val ageMinInt = ageMin.toIntOrNull()
            val ageMaxInt = ageMax.toIntOrNull()
            when {
                ageMinInt == null || ageMaxInt == null -> {
                    onError("L'âge minimum et maximum doivent être des nombres entiers")
                }
                ageMinInt > ageMaxInt -> {
                    onError("L'âge minimum doit être inférieur à l'âge maximum")
                }
                else -> {
                    val competition = CompetitionCreate(
                        name = nom,
                        ageMin = ageMinInt,
                        ageMax = ageMaxInt,
                        gender = if (gender == "Homme") CompetitionCreate.Gender.H else CompetitionCreate.Gender.F,
                        hasRetry = option == "Oui"
                    )
                    viewModel.addCompetition(competition)
                    val intent = Intent(context, ListeCompetitions::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}


fun onClickAnnuler(context: Context) {
    val intent = Intent(context, ListeCompetitions::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        AjtCompetPage()
    }
}