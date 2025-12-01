package com.example.hunrmand.domain.repository


import android.location.Location

interface LocationRepository {
    // Turn coordinates into a City Name (e.g., "Islamabad")
    suspend fun getCityFromCoordinates(lat: Double, lng: Double): String?

    // Turn a City Name into coordinates (for the Search bar)
    suspend fun getCoordinatesFromCity(cityName: String): Pair<Double, Double>?
}