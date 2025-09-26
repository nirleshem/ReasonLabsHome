package com.example.nir.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WeatherResponse(
    val weather: List<WeatherCondition>,
    val main: MainWeather,
    val wind: Wind,
    val name: String
)

@Serializable
data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class MainWeather(
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    @SerialName("temp_min")
    val tempMin: Double,
    @SerialName("temp_max")
    val tempMax: Double
)

@Serializable
data class Wind(
    val speed: Double,
    val deg: Int? = null
)

data class WeatherData(
    val cityName: String,
    val temperature: Int,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String
) {
    companion object {
        fun from(response: WeatherResponse): WeatherData {
            val condition = response.weather.firstOrNull() ?: WeatherCondition(0, "", "Unknown", "01d")
            return WeatherData(
                cityName = response.name,
                temperature = response.main.temp.toInt(),
                description = condition.description.replaceFirstChar { it.uppercase() },
                humidity = response.main.humidity,
                windSpeed = response.wind.speed,
                icon = condition.icon
            )
        }
    }
    
    fun getTemperatureText(): String = "${temperature}°C"
    fun getHumidityText(): String = "$humidity%"
    fun getWindSpeedText(): String = "${windSpeed} m/s"
}

enum class WeatherIcon(val code: String, val emoji: String) {
    CLEAR_DAY("01d", "☀️"),
    CLEAR_NIGHT("01n", "🌙"),
    FEW_CLOUDS_DAY("02d", "🌤️"),
    FEW_CLOUDS_NIGHT("02n", "☁️"),
    SCATTERED_CLOUDS("03d", "☁️"),
    SCATTERED_CLOUDS_NIGHT("03n", "☁️"),
    BROKEN_CLOUDS("04d", "☁️"),
    BROKEN_CLOUDS_NIGHT("04n", "☁️"),
    SHOWER_RAIN_DAY("09d", "🌦️"),
    SHOWER_RAIN_NIGHT("09n", "🌦️"),
    RAIN_DAY("10d", "🌧️"),
    RAIN_NIGHT("10n", "🌧️"),
    THUNDERSTORM("11d", "⛈️"),
    THUNDERSTORM_NIGHT("11n", "⛈️"),
    SNOW("13d", "❄️"),
    SNOW_NIGHT("13n", "❄️"),
    MIST("50d", "🌫️"),
    MIST_NIGHT("50n", "🌫️");
    
    companion object {
        fun fromCode(code: String): WeatherIcon {
            return entries.find { it.code == code } ?: CLEAR_DAY
        }
    }
}