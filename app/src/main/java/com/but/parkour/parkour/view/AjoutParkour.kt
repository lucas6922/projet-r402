package com.but.parkour.parkour.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ParkourTheme {
        AjtParkourPage("Android")
    }
}