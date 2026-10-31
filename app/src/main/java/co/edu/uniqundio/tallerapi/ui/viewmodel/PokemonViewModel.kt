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
    val filteredList: List<PokemonResult> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedType: String? = null,
    val availableTypes: List<String> = emptyList(),
    val isConnected: Boolean = true,
    val selectedPokemonDetail: PokemonDetail? = null,
    val isDetailLoading: Boolean = false,
    val hasMore: Boolean = true
)

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokemonRepository()
    private val connectivityObserver = NetworkConnectivityObserver(application)

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20
    private var allPokemonsCache: List<PokemonResult> = emptyList()
    private var typePokemonsCache: List<PokemonResult>? = null

    init {
        observeNetwork()
        loadTypes()
        loadPokemonList()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connectivityObserver.observe.collect { connected ->
                _uiState.update { it.copy(isConnected = connected) }
                if (connected && _uiState.value.errorMessage != null && _uiState.value.pokemonList.isEmpty()) {
                    loadPokemonList()
                }
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
        if (_uiState.value.isLoading || _uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        viewModelScope.launch {
            if (currentOffset == 0) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            } else {
                _uiState.update { it.copy(isLoadingMore = true, errorMessage = null) }
            }

            repository.getPokemonList(limit = pageSize, offset = currentOffset)
                .onSuccess { newList ->
                    if (newList.isEmpty()) {
                        _uiState.update { it.copy(hasMore = false, isLoading = false, isLoadingMore = false) }
                        return@onSuccess
                    }
                    val updatedList = if (currentOffset == 0) newList else allPokemonsCache + newList
                    allPokemonsCache = updatedList
                    currentOffset += pageSize

                    val currentBase = if (_uiState.value.selectedType != null) typePokemonsCache ?: updatedList else updatedList
                    val filtered = applyNonDestructiveFilter(currentBase, _uiState.value.searchQuery)

                    _uiState.update {
                        it.copy(
                            pokemonList = updatedList,
                            filteredList = filtered,
                            isLoading = false,
                            isLoadingMore = false,
                            hasMore = newList.size >= pageSize
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
        val base = if (_uiState.value.selectedType != null) typePokemonsCache ?: allPokemonsCache else allPokemonsCache
        val filtered = applyNonDestructiveFilter(base, query)
        _uiState.update { it.copy(filteredList = filtered) }
    }

    fun onTypeSelected(type: String?) {
        _uiState.update { it.copy(selectedType = type, errorMessage = null) }
        if (type == null) {
            typePokemonsCache = null
            val filtered = applyNonDestructiveFilter(allPokemonsCache, _uiState.value.searchQuery)
            _uiState.update { it.copy(filteredList = filtered) }
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                repository.getPokemonsByType(type)
                    .onSuccess { list ->
                        typePokemonsCache = list
                        val filtered = applyNonDestructiveFilter(list, _uiState.value.searchQuery)
                        _uiState.update { it.copy(filteredList = filtered, isLoading = false) }
                    }
                    .onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error.localizedMessage, isLoading = false) }
                    }
            }
        }
    }

    private fun applyNonDestructiveFilter(source: List<PokemonResult>, query: String): List<PokemonResult> {
        if (query.isBlank()) return source
        return source.filter { it.name.contains(query.trim(), ignoreCase = true) }
    }

    fun retry() {
        _uiState.update { it.copy(errorMessage = null) }
        if (allPokemonsCache.isEmpty()) {
            currentOffset = 0
            loadPokemonList()
        } else if (_uiState.value.selectedType != null) {
            onTypeSelected(_uiState.value.selectedType)
        }
    }

    fun fetchPokemonDetail(nameOrId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDetailLoading = true, selectedPokemonDetail = null, errorMessage = null) }
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
