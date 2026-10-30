package co.edu.uniqundio.tallerapi.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniqundio.tallerapi.data.model.PokemonDetail
import co.edu.uniqundio.tallerapi.data.model.PokemonResult
import co.edu.uniqundio.tallerapi.data.repository.PokemonRepository
import co.edu.uniqundio.tallerapi.utils.NetworkConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonUiState(
    val pokemonList: List<PokemonResult> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedType: String? = null,
    val availableTypes: List<String> = emptyList(),
    val isConnected: Boolean = true,
    val selectedPokemonDetail: PokemonDetail? = null,
    val isDetailLoading: Boolean = false
)

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokemonRepository()
    private val connectivityObserver = NetworkConnectivityObserver(application)

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20
    private var allPokemonsCache: List<PokemonResult> = emptyList()

    init {
        observeNetwork()
        loadTypes()
        loadPokemonList()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connectivityObserver.observe.collect { connected ->
                _uiState.update { it.copy(isConnected = connected) }
            }
        }
    }

    private fun loadTypes() {
        viewModelScope.launch {
            repository.getPokemonTypes().onSuccess { types ->
                _uiState.update { it.copy(availableTypes = types) }
            }
        }
    }

    fun loadPokemonList() {
        if (_uiState.value.isLoading || _uiState.value.isLoadingMore) return
        viewModelScope.launch {
            if (currentOffset == 0) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            } else {
                _uiState.update { it.copy(isLoadingMore = true, errorMessage = null) }
            }

            repository.getPokemonList(limit = pageSize, offset = currentOffset)
                .onSuccess { newList ->
                    val updatedList = if (currentOffset == 0) newList else _uiState.value.pokemonList + newList
                    allPokemonsCache = updatedList
                    currentOffset += pageSize
                    _uiState.update {
                        it.copy(
                            pokemonList = updatedList,
                            isLoading = false,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = error.localizedMessage ?: "Error al cargar Pokémon"
                        )
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterPokemons(query, _uiState.value.selectedType)
    }

    fun onTypeSelected(type: String?) {
        _uiState.update { it.copy(selectedType = type) }
        if (type == null) {
            _uiState.update { it.copy(pokemonList = allPokemonsCache) }
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                repository.getPokemonsByType(type)
                    .onSuccess { list ->
                        _uiState.update { it.copy(pokemonList = list, isLoading = false) }
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error.localizedMessage, isLoading = false) }
                    }
            }
        }
    }

    private fun filterPokemons(query: String, type: String?) {
        var list = if (type == null) allPokemonsCache else _uiState.value.pokemonList
        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query.trim(), ignoreCase = true) }
        }
        _uiState.update { it.copy(pokemonList = list) }
    }

    fun fetchPokemonDetail(nameOrId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDetailLoading = true, selectedPokemonDetail = null) }
            repository.getPokemonDetail(nameOrId)
                .onSuccess { detail ->
                    _uiState.update { it.copy(selectedPokemonDetail = detail, isDetailLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.localizedMessage, isDetailLoading = false) }
                }
        }
    }
}
