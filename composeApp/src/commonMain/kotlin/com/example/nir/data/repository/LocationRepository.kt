package com.example.nir.data.repository

import com.example.nir.data.models.PhotonResponse
import com.example.nir.data.models.SearchLocation
import com.example.nir.network.NetworkClient
import com.example.nir.getPlatform
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface LocationRepository {
    suspend fun searchLocations(query: String): Result<List<SearchLocation>>
}

@Serializable
data class NominatimResult(
    val name: String,
    val country: String? = null,
    val state: String? = null,
    val lat: String,
    val lon: String,
    @SerialName("display_name") val displayName: String
)

class LocationRepositoryImpl : LocationRepository {
    
    companion object {
        private const val PHOTON_BASE_URL = "https://photon.komoot.io"
        private const val NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org"
        private const val SEARCH_LIMIT = 10
    }
    
    private val isIOS = getPlatform().name.contains("iOS", ignoreCase = true)
    
    override suspend fun searchLocations(query: String): Result<List<SearchLocation>> {
        return try {
            if (query.isBlank()) {
                return Result.success(emptyList())
            }
            
            if (isIOS) {
                searchWithNominatim(query)
            } else {
                searchWithPhoton(query)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun searchWithPhoton(query: String): Result<List<SearchLocation>> {
        val response = NetworkClient.httpClient.get("$PHOTON_BASE_URL/api/") {
            parameter("q", query)
            parameter("limit", SEARCH_LIMIT)
            parameter("lang", "en")
        }
        
        if (response.status == HttpStatusCode.OK) {
            val photonResponse: PhotonResponse = response.body()
            val locations = photonResponse.features.map { SearchLocation.from(it) }
            return Result.success(locations)
        } else {
            return Result.failure(Exception("Search failed with status: ${response.status}"))
        }
    }
    
    private suspend fun searchWithNominatim(query: String): Result<List<SearchLocation>> {
        val response = NetworkClient.httpClient.get("$NOMINATIM_BASE_URL/search") {
            parameter("q", query)
            parameter("format", "json")
            parameter("limit", SEARCH_LIMIT)
            parameter("addressdetails", "1")
            parameter("accept-language", "en")
        }
        
        if (response.status == HttpStatusCode.OK) {
            val nominatimResults: List<NominatimResult> = response.body()
            val locations = nominatimResults.map { result ->
                SearchLocation(
                    name = result.name,
                    displayName = result.displayName,
                    latitude = result.lat.toDoubleOrNull() ?: 0.0,
                    longitude = result.lon.toDoubleOrNull() ?: 0.0
                )
            }
            return Result.success(locations)
        } else {
            return Result.failure(Exception("Search failed with status: ${response.status}"))
        }
    }
}