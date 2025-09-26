# Weather App Setup Instructions

## OpenWeatherMap API Key Setup

To use this weather app, you need to get an API key from OpenWeatherMap and configure it in the app.

### Step 1: Get Your API Key
1. Go to [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account
3. Go to your account dashboard
4. Copy your API key

### Step 2: Configure the API Key
Open the file `composeApp/src/commonMain/kotlin/com/example/nir/di/AppModule.kt` and replace:

```kotlin
private const val WEATHER_API_KEY = "YOUR_OPENWEATHERMAP_API_KEY_HERE"
```

With your actual API key:

```kotlin
private const val WEATHER_API_KEY = "your_actual_api_key_here"
```

### Step 3: Build and Run
After adding your API key, you can build and run the app on both Android and iOS platforms.

## Features
- 🔍 Live location search with Photon API
- 🌤️ Current weather display with temperature, humidity, and wind speed
- 📱 Weather icons that change based on conditions
- 📋 Recent searches (last 5 searches) displayed as capsule-shaped chips
- 🎨 Clean, modern UI using Compose Multiplatform
- 🔄 Cross-platform support (Android & iOS)

## Architecture
- **Shared Code**: Business logic, models, repositories, and state management
- **Platform-specific**: UI rendering (though using Compose Multiplatform for both)
- **Networking**: Ktor for HTTP requests
- **Serialization**: Kotlinx Serialization for JSON parsing
- **State Management**: StateFlow with ViewModel pattern
- **Local Storage**: Multiplatform Settings for recent searches

## APIs Used
- **Photon API**: For location search suggestions
- **OpenWeatherMap API**: For weather data