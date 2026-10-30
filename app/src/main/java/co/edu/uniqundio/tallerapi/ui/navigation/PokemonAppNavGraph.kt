package co.edu.uniqundio.tallerapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.uniqundio.tallerapi.ui.screens.HomeScreen
import co.edu.uniqundio.tallerapi.ui.screens.PokemonDetailScreen
import co.edu.uniqundio.tallerapi.ui.screens.PokemonListScreen
import co.edu.uniqundio.tallerapi.ui.viewmodel.PokemonViewModel

@Composable
fun PokemonAppNavGraph() {
    val navController = rememberNavController()
    val viewModel: PokemonViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                isConnected = uiState.isConnected,
                onEnterPokedex = {
                    navController.navigate("pokemon_list")
                }
            )
        }
        composable("pokemon_list") {
            PokemonListScreen(
                viewModel = viewModel,
                onPokemonClick = { pokemonName ->
                    navController.navigate("pokemon_detail/$pokemonName")
                }
            )
        }
        composable(
            route = "pokemon_detail/{pokemonName}",
            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""
            PokemonDetailScreen(
                pokemonName = pokemonName,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
