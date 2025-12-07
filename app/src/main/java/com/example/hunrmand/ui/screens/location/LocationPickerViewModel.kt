package com.example.hunrmand.ui.screens.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.domain.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LocationPickerViewModel(
    private val sessionManager: SessionManager,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _initialLatLng = MutableStateFlow<LatLng?>(null)
    val initialLatLng: StateFlow<LatLng?> = _initialLatLng.asStateFlow()

    init {
        initializeLocation()
    }

    private fun initializeLocation() {
        viewModelScope.launch {
            val savedCity = sessionManager.city.firstOrNull()
            if (!savedCity.isNullOrBlank()) {
                val coords = locationRepository.getCoordinatesFromCity(savedCity)
                if (coords != null) {
                    _initialLatLng.value = LatLng(coords.first, coords.second)
                } else {
                     // Fallback if city coords not found
                     _initialLatLng.value = LatLng(32.5839, 71.5370) // Mianwali
                }
            } else {
                 // Default Mianwali if no city saved
                 _initialLatLng.value = LatLng(32.5839, 71.5370) 
            }
        }
    }
}
