package com.example.nir.data.local

import com.example.nir.data.models.RecentSearch
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface RecentSearchesStorage {
    fun getRecentSearches(): List<RecentSearch>
    fun addRecentSearch(search: RecentSearch)
    fun clearRecentSearches()
}

class RecentSearchesStorageImpl(
    private val settings: Settings
) : RecentSearchesStorage {
    
    companion object {
        private const val RECENT_SEARCHES_KEY = "recent_searches"
        private const val MAX_RECENT_SEARCHES = 5
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    override fun getRecentSearches(): List<RecentSearch> {
        return try {
            val jsonString = settings.getString(RECENT_SEARCHES_KEY, "[]")
            json.decodeFromString<List<RecentSearch>>(jsonString)
                .sortedByDescending { it.searchTimestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override fun addRecentSearch(search: RecentSearch) {
        try {
            val currentSearches = getRecentSearches().toMutableList()
            
            // Remove existing search for the same city
            currentSearches.removeAll { it.cityName.equals(search.cityName, ignoreCase = true) }
            
            // Add new search at the beginning
            currentSearches.add(0, search)
            
            // Keep only the last MAX_RECENT_SEARCHES
            val updatedSearches = currentSearches.take(MAX_RECENT_SEARCHES)
            
            val jsonString = json.encodeToString(updatedSearches)
            settings[RECENT_SEARCHES_KEY] = jsonString
        } catch (e: Exception) {
            // Handle serialization errors silently
        }
    }
    
    override fun clearRecentSearches() {
        settings.remove(RECENT_SEARCHES_KEY)
    }
}