package com.example.nir.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PhotonResponse(
    val features: List<LocationFeature>
)

@Serializable
data class LocationFeature(
    val properties: LocationProperties,
    val geometry: Geometry
)

@Serializable
data class LocationProperties(
    val name: String,
    val country: String? = null,
    val state: String? = null,
    val city: String? = null
) {
    fun getDisplayName(): String {
        return buildString {
            append(name)
            city?.let { if (it != name) append(", $it") }
            state?.let { append(", $it") }
            country?.let { append(", $it") }
        }
    }
}

@Serializable
data class Geometry(
    val coordinates: List<Double>
) {
    val longitude: Double get() = coordinates[0]
    val latitude: Double get() = coordinates[1]
}

data class SearchLocation(
    val name: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun from(feature: LocationFeature): SearchLocation {
            return SearchLocation(
                name = feature.properties.name,
                displayName = feature.properties.getDisplayName(),
                latitude = feature.geometry.latitude,
                longitude = feature.geometry.longitude
            )
        }
    }
}