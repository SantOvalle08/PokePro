package co.edu.uniqundio.tallerapi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.uniqundio.tallerapi.R
import co.edu.uniqundio.tallerapi.ui.theme.PokemonFontFamily
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    isConnected: Boolean,
    onEnterPokedex: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Background Image containing logo, characters, and button boxes
            Image(
                painter = painterResource(id = R.drawable.home_bg),
                contentDescription = "Fondo Home",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Content overlay positioned precisely over the background slots from top
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .padding(top = 575.dp), // Ajustado ligeramente al hacerlos más altos
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Botón 1: Entrar a la Pokédex (Más alto, 56.dp)
                Button(
                    onClick = onEnterPokedex,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = "Entrar a la Pokedex",
                        fontFamily = PokemonFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Espacio entre ambos botones adaptado a la nueva altura
                Spacer(modifier = Modifier.height(45.dp))

                // Botón 2: Consultar conexión (Más alto, 56.dp)
                Button(
                    onClick = {
                        scope.launch {
                            val message = if (isConnected) {
                                "🟢 ¡Conectado a Internet con éxito!"
                            } else {
                                "🔴 ¡Sin conexión a Internet!"
                            }
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(
                        text = "Consultar conexion",
                        fontFamily = PokemonFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
