package com.example.nir.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nir.data.models.RecentSearch
import com.example.nir.data.models.SearchLocation
import com.example.nir.data.models.WeatherData
import com.example.nir.domain.WeatherUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val searchQuery: String = "",
    val locationSuggestions: List<SearchLocation> = emptyList(),
    val selectedLocation: SearchLocation? = null,
    val weatherData: WeatherData? = null,
    val recentSearches: List<RecentSearch> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val error: String? = null,
    val showSuggestions: Boolean = false
)

class WeatherViewModel(
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    private var searchJob: Job? = null
    
    init {
        loadRecentSearches()
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            showSuggestions = query.isNotBlank(),
            error = null
        )
        
        if (query.isNotBlank()) {
            debounceSearch(query)
        } else {
            clearSuggestions()
        }
    }
    
    private fun debounceSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce delay
            searchLocations(query)
        }
    }
    
    private suspend fun searchLocations(query: String) {
        _uiState.value = _uiState.value.copy(isSearching = true)
        
        weatherUseCase.searchLocations(query)
            .onSuccess { locations ->
                _uiState.value = _uiState.value.copy(
                    locationSuggestions = locations,
                    isSearching = false,
                    error = null
                )
            }
            .onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    locationSuggestions = emptyList(),
                    isSearching = false,
                    error = "Failed to search locations: ${exception.message}"
                )
            }
    }
    
    fun selectLocation(location: SearchLocation) {
        _uiState.value = _uiState.value.copy(
            selectedLocation = location,
            searchQuery = location.displayName,
            showSuggestions = false,
            locationSuggestions = emptyList(),
            error = null
        )
    }
    
    fun searchWeather() {
        val location = _uiState.value.selectedLocation ?: return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            weatherUseCase.getWeatherForLocation(location)
                .onSuccess { weather ->
                    _uiState.value = _uiState.value.copy(
                        weatherData = weather,
                        isLoading = false,
                        error = null
                    )
                    loadRecentSearches() // Refresh recent searches
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        weatherData = null,
                        isLoading = false,
                        error = "Failed to fetch weather: ${exception.message}"
                    )
                }
        }
    }
    
    fun selectRecentSearch(recentSearch: RecentSearch) {
        val location = recentSearch.toSearchLocation()
        selectLocation(location)
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            weatherUseCase.getWeatherForRecentSearch(recentSearch)
                .onSuccess { weather ->
                    _uiState.value = _uiState.value.copy(
                        weatherData = weather,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        weatherData = null,
                        isLoading = false,
                        error = "Failed to fetch weather: ${exception.message}"
                    )
                }
        }
    }
    
    fun clearSuggestions() {
        _uiState.value = _uiState.value.copy(
            locationSuggestions = emptyList(),
            showSuggestions = false,
            isSearching = false
        )
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun loadRecentSearches() {
        val recentSearches = weatherUseCase.getRecentSearches()
        _uiState.value = _uiState.value.copy(recentSearches = recentSearches)
    }
    
    fun clearRecentSearches() {
        weatherUseCase.clearRecentSearches()
        loadRecentSearches()
    }
}