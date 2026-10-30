package co.edu.uniqundio.tallerapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.edu.uniqundio.tallerapi.ui.navigation.PokemonAppNavGraph
import co.edu.uniqundio.tallerapi.ui.theme.TallerAPITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TallerAPITheme {
                PokemonAppNavGraph()
            }
        }
    }
}
