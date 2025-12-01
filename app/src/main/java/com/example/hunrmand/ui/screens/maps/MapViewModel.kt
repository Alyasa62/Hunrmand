package com.example.hunrmand.ui.maps // Updated package to match your structure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.repository.LocationRepository // Added import based on your AppModule
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Simple data class to represent a point on the map
data class MapPin(
    val id: String,
    val name: String,
    val position: LatLng,
    val description: String
)

// Updated to accept LocationRepository in the constructor
// This fixes the "Too many arguments" error in AppModule
class MapViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _mapPins = MutableStateFlow<List<MapPin>>(emptyList())
    val mapPins: StateFlow<List<MapPin>> = _mapPins.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMapData()
    }

    private fun loadMapData() {
        viewModelScope.launch {
            _isLoading.value = true
            // Example usage of repository if needed
            // val locations = locationRepository.getLocations()

            val mockData = listOf(
                MapPin("1", "Mianwali", LatLng(32.5839, 71.5370), "Mianwali, Punjab, Pakistan"),
                MapPin("2", "Islamabad", LatLng(33.6844, 73.0479), "Capital of Pakistan"),
                MapPin("3", "Lahore", LatLng(31.5497, 74.3436), "Capital of Punjab")
            )
            _mapPins.value = mockData
            _isLoading.value = false
        }
    }

    fun onPinSelected(pin: MapPin) {
        println("Selected pin: ${pin.name}")
    }
}