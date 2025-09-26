package com.example.nir.data.repository

import com.example.nir.data.models.WeatherData
import com.example.nir.data.models.WeatherResponse
import com.example.nir.network.NetworkClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

interface WeatherRepository {
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): Result<WeatherData>
}

class WeatherRepositoryImpl(
    private val apiKey: String
) : WeatherRepository {
    
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
    }
    
    override suspend fun getWeatherByCoordinates(lat: Double, lon: Double): Result<WeatherData> {
        return try {
            val response = NetworkClient.httpClient.get("$BASE_URL/weather") {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", apiKey)
                parameter("units", "metric")
                parameter("lang", "en")
            }
            
            if (response.status == HttpStatusCode.OK) {
                val weatherResponse: WeatherResponse = response.body()
                val weatherData = WeatherData.from(weatherResponse)
                Result.success(weatherData)
            } else {
                Result.failure(Exception("Weather request failed with status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}