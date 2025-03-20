package com.but.parkour.database.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.database.TimeDAO
import com.but.parkour.database.viewmodel.TimeViewModel
import com.but.parkour.ui.theme.ParkourTheme

class Vue : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Main(
                    modifier = Modifier.fillMaxSize().padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    var tvm: TimeViewModel = viewModel()
    Column(modifier = modifier) {
        Text("test")
        //tvm.getTimes()
        Text("test2")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        Main(Modifier.fillMaxSize().padding(2.dp))
    }
}