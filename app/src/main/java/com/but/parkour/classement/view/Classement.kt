package com.but.parkour.classement.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.classement.view.ui.theme.ParkourTheme
import com.but.parkour.classement.viewmodel.PerformanceViewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.parkour.view.ListeParkours
import com.but.parkour.parkour.viewmodel.ParkourViewModel

class Classement : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competition = intent.getSerializableExtra("competition") as Competition

                val performanceViewModel: PerformanceViewModel = viewModel()
                val performances by performanceViewModel.performances.observeAsState(initial = emptyList())

                val competitionId = competition.id

                val parkourViewModel: ParkourViewModel = viewModel()
                competitionId?.let {
                    LaunchedEffect(it) {
                        parkourViewModel.fetchCourses(it)
                    }
                }
                val parkours by parkourViewModel.parkours.observeAsState(initial = emptyList())
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClassementPage(
                        modifier = Modifier.padding(innerPadding),
                        performances = performances,
                        parkours = parkours,
                        viewModel = performanceViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ClassementPage(modifier : Modifier, performances: List<Performance>, parkours: List<Course>, viewModel: PerformanceViewModel) {
    var parkoursExpanded by remember { mutableStateOf(false) }
    var selectedParkour by remember { mutableStateOf<Course?>(null) }
    var classement by remember { mutableStateOf<List<Competitor>>(emptyList()) }
    if(selectedParkour != null) {
        LaunchedEffect(selectedParkour) {
            classement = viewModel.filterCompetitorsWithPerformance(selectedParkour!!)
        }
    }


    Column(modifier) {
        Row {
            Text(
                text = "Parkour : ",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = selectedParkour?.name ?: "Sélectionner un parkour",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { parkoursExpanded = true }
            )
            DropdownMenu(
                expanded = parkoursExpanded,
                onDismissRequest = { parkoursExpanded = false }
            ) {
                parkours.forEach { parkour ->
                    DropdownMenuItem(
                        text = { Text(parkour.name ?: "") },
                        onClick = {
                            selectedParkour = parkour
                            parkoursExpanded = false
                        }
                    )
                }
            }
        }
        Classement(Modifier.padding(top = 16.dp), concurrents = classement)
    }
}

@Composable
fun Classement(modifier : Modifier, concurrents: List<Competitor>) {
    if(concurrents.isEmpty()) {
        Text(
            text = "Aucun concurrent trouvé",
            modifier = Modifier.padding(16.dp)
        )
        return
    }
    var i by remember { mutableStateOf(0) }
    LazyColumn(modifier) {
        items(concurrents) { concurrent ->
            Row(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "$i ${concurrent.firstName} ${concurrent.lastName}",
                    modifier = Modifier.padding(8.dp)
                )
            }
            i += 1
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ParkourTheme {
    }
}