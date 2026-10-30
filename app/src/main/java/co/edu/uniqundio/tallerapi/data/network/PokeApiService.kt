package co.edu.uniqundio.tallerapi.data.network

import co.edu.uniqundio.tallerapi.data.model.PokemonDetail
import co.edu.uniqundio.tallerapi.data.model.PokemonListResponse
import co.edu.uniqundio.tallerapi.data.model.TypeListResponse
import co.edu.uniqundio.tallerapi.data.model.TypeDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetail

    @GET("type")
    suspend fun getPokemonTypes(): TypeListResponse

    @GET("type/{name}")
    suspend fun getPokemonsByType(
        @Path("name") typeName: String
    ): TypeDetailResponse

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}
