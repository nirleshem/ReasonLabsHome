package com.example.nir.data.models

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Serializable
data class RecentSearch(
    val cityName: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val searchTimestamp: Long = Clock.System.now().epochSeconds
) {
    companion object {
        fun from(location: SearchLocation): RecentSearch {
            return RecentSearch(
                cityName = location.name,
                displayName = location.displayName,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }
    
    fun toSearchLocation(): SearchLocation {
        return SearchLocation(
            name = cityName,
            displayName = displayName,
            latitude = latitude,
            longitude = longitude
        )
    }
}