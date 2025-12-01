package com.example.hunrmand.ui.screens.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.hunrmand.ui.maps.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    navController: NavController, // Added NavController to handle back navigation
    viewModel: MapViewModel = koinViewModel()
) {
    val mapPins by viewModel.mapPins.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Default to Mianwali, Pakistan
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(32.5839, 71.5370), 7f)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = false
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false
                )
            ) {
                mapPins.forEach { pin ->
                    Marker(
                        state = MarkerState(position = pin.position),
                        title = pin.name,
                        snippet = "Click to select this location",
                        onClick = {
                            // 1. Set the result in the previous back stack entry
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_city", pin.name)

                            // 2. Pop back to Home Screen
                            navController.popBackStack()

                            true // Consumes the event
                        }
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}