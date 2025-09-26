package com.example.nir.domain

import com.example.nir.data.local.RecentSearchesStorage
import com.example.nir.data.models.RecentSearch
import com.example.nir.data.models.SearchLocation
import com.example.nir.data.models.WeatherData
import com.example.nir.data.repository.LocationRepository
import com.example.nir.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherUseCase(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val recentSearchesStorage: RecentSearchesStorage
) {
    
    suspend fun searchLocations(query: String): Result<List<SearchLocation>> {
        return locationRepository.searchLocations(query)
    }
    
    suspend fun getWeatherForLocation(location: SearchLocation): Result<WeatherData> {
        val weatherResult = weatherRepository.getWeatherByCoordinates(
            lat = location.latitude,
            lon = location.longitude
        )
        
        // Save to recent searches if weather fetch is successful
        if (weatherResult.isSuccess) {
            val recentSearch = RecentSearch.from(location)
            recentSearchesStorage.addRecentSearch(recentSearch)
        }
        
        return weatherResult
    }
    
    fun getRecentSearches(): List<RecentSearch> {
        return recentSearchesStorage.getRecentSearches()
    }
    
    suspend fun getWeatherForRecentSearch(recentSearch: RecentSearch): Result<WeatherData> {
        return weatherRepository.getWeatherByCoordinates(
            lat = recentSearch.latitude,
            lon = recentSearch.longitude
        )
    }
    
    fun clearRecentSearches() {
        recentSearchesStorage.clearRecentSearches()
    }
}