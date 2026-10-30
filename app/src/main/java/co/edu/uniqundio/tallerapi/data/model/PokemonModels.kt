package co.edu.uniqundio.tallerapi.data.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonResult>
)

data class PokemonResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {
    val id: Int
        get() {
            val segments = url.trimEnd('/').split("/")
            return segments.lastOrNull()?.toIntOrNull() ?: 0
        }

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

data class PokemonDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sprites") val sprites: PokemonSprites,
    @SerializedName("types") val types: List<PokemonTypeSlot>,
    @SerializedName("stats") val stats: List<PokemonStatSlot>,
    @SerializedName("abilities") val abilities: List<PokemonAbilitySlot>
)

data class PokemonSprites(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("other") val other: PokemonOtherSprites?
)

data class PokemonOtherSprites(
    @SerializedName("official-artwork") val officialArtwork: PokemonOfficialArtwork?
)

data class PokemonOfficialArtwork(
    @SerializedName("front_default") val frontDefault: String?
)

data class PokemonTypeSlot(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: NamedApiResource
)

data class PokemonStatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("stat") val stat: NamedApiResource
)

data class PokemonAbilitySlot(
    @SerializedName("ability") val ability: NamedApiResource
)

data class NamedApiResource(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class TypeListResponse(
    @SerializedName("results") val results: List<NamedApiResource>
)

data class TypeDetailResponse(
    @SerializedName("pokemon") val pokemonSlots: List<TypePokemonSlot>
)

data class TypePokemonSlot(
    @SerializedName("pokemon") val pokemon: PokemonResult
)
