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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.concurrents.ui.theme.ParkourTheme
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.but.parkour.chrono.view.Chronometre
import com.but.parkour.clientkotlin.models.Competition

class ListeConcurrents : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as Competition
        val course = intent.getSerializableExtra("course") as Course
        setContent {
            ParkourTheme {
                ConcurrentPage(competition, course)
            }
        }
    }
}

@Composable
fun ConcurrentPage(competition: Competition, course: Course) {
    val competitorViewModel: CompetitorViewModel = viewModel()
    val participants by competitorViewModel.competitorsCourse.observeAsState(emptyList())

    LaunchedEffect(competition) {
        competition.id?.let { id ->
            competitorViewModel.fetchCompetitorsCourse(id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp, bottom = 32.dp)

    ) {
        Text(
            text = competition.name ?: "Unknown",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Log.d("ListeConcurrents", "Participants: $participants")
        val context = LocalContext.current
        ListParticipants(
            concurrents = participants,
            modifier = Modifier.weight(1f),
            onItemClick = {  },
            onChronoClick = { competitor ->
                onItemClickChrono(competition, course, context)
            },
            competitionId = null,
            competitorViewModel = competitorViewModel,
            buttonSupp = false
        )

    }
}




fun onItemClickChrono(competition: Competition, course: Course, context: Context) {
    val intent = Intent(context, Chronometre::class.java).apply {
        putExtra("competition", competition)
        putExtra("course", course)
    }
    context.startActivity(intent)
}
