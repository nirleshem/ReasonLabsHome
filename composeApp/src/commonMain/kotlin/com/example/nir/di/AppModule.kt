package com.example.nir.di

import com.example.nir.data.local.RecentSearchesStorage
import com.example.nir.data.local.RecentSearchesStorageImpl
import com.example.nir.data.local.SettingsFactory
import com.example.nir.data.repository.LocationRepository
import com.example.nir.data.repository.LocationRepositoryImpl
import com.example.nir.data.repository.WeatherRepository
import com.example.nir.data.repository.WeatherRepositoryImpl
import com.example.nir.domain.WeatherUseCase
import com.example.nir.presentation.WeatherViewModel

object AppModule {
    
    // Replace with your actual OpenWeatherMap API key
    private const val WEATHER_API_KEY = "fa85487df39c9e313cc05969985a9137"
    
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepositoryImpl(WEATHER_API_KEY)
    }
    
    fun provideLocationRepository(): LocationRepository {
        return LocationRepositoryImpl()
    }
    
    fun provideRecentSearchesStorage(): RecentSearchesStorage {
        return RecentSearchesStorageImpl(SettingsFactory.create())
    }
    
    fun provideWeatherUseCase(): WeatherUseCase {
        return WeatherUseCase(
            locationRepository = provideLocationRepository(),
            weatherRepository = provideWeatherRepository(),
            recentSearchesStorage = provideRecentSearchesStorage()
        )
    }
    
    fun provideWeatherViewModel(): WeatherViewModel {
        return WeatherViewModel(provideWeatherUseCase())
    }
}