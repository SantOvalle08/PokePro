package co.edu.uniqundio.tallerapi.data.repository

import co.edu.uniqundio.tallerapi.data.model.PokemonDetail
import co.edu.uniqundio.tallerapi.data.model.PokemonResult
import co.edu.uniqundio.tallerapi.data.network.RetrofitClient

class PokemonRepository {
    private val api = RetrofitClient.apiService

    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<PokemonResult>> {
        return try {
            val response = api.getPokemonList(limit, offset)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemonDetail(name: String): Result<PokemonDetail> {
        return try {
            val detail = api.getPokemonDetail(name.lowercase())
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemonTypes(): Result<List<String>> {
        return try {
            val response = api.getPokemonTypes()
            Result.success(response.results.map { it.name })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemonsByType(typeName: String): Result<List<PokemonResult>> {
        return try {
            val response = api.getPokemonsByType(typeName.lowercase())
            Result.success(response.pokemonSlots.map { it.pokemon })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
