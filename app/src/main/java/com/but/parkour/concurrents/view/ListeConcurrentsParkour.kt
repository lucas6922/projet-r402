package com.but.parkour.concurrents.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.but.parkour.chrono.view.Chronometre
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.components.PageTitle

class ListeConcurrentsParkour : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as Competition
        val course = intent.getSerializableExtra("course") as Course
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->
                ConcurrentPage(
                    modifier = Modifier.padding(innerPadding),
                    competition,
                    course
                )
            }
        }
    }
}

@Composable
fun ConcurrentPage(modifier: Modifier = Modifier, competition: Competition, course: Course) {
    val competitorViewModel: CompetitorViewModel = viewModel()
    val participants by competitorViewModel.competitorsCourse.observeAsState(emptyList())

    LaunchedEffect(competition) {
        competition.id?.let { id ->
            competitorViewModel.fetchCompetitorsCourse(id)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp, bottom = 32.dp)

    ) {

        PageTitle("Concurrents de la competition : ${competition.name}")

        Spacer(modifier = Modifier.height(8.dp))

        Log.d("ListeConcurrents", "Participants: $participants")
        val context = LocalContext.current
        ListParticipants(
            concurrents = participants,
            modifier = Modifier.weight(1f),
            onChronoClick = { competitor ->
                onItemClickChrono(competition, course, competitor, context)
            },
            competition = competition,
            competitorViewModel = competitorViewModel,
        )

    }
}




fun onItemClickChrono(competition: Competition, course: Course, competitor: Competitor, context: Context) {
    val intent = Intent(context, Chronometre::class.java).apply {
        putExtra("competition", competition)
        putExtra("competitor", competitor)
        putExtra("course", course)
    }
    context.startActivity(intent)
}
