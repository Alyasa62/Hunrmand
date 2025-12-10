package com.example.hunrmand.data.repository

import android.content.Context
import android.location.Geocoder
import com.example.hunrmand.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationRepositoryImpl(private val context: Context) : LocationRepository {

    override suspend fun getCityFromCoordinates(lat: Double, lng: Double): String? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                // Get 1 result
                @Suppress("DEPRECATION") // For simplicity in older Android versions
                val addresses = geocoder.getFromLocation(lat, lng, 1)

                if (!addresses.isNullOrEmpty()) {
                    // Try to get locality (City), fallback to subAdminArea or adminArea
                    addresses[0].locality ?: addresses[0].subAdminArea ?: addresses[0].adminArea
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun getCoordinatesFromCity(cityName: String): Pair<Double, Double>? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(cityName, 1)

                if (!addresses.isNullOrEmpty()) {
                    Pair(addresses[0].latitude, addresses[0].longitude)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}