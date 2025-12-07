package com.example.hunrmand.ui.screens.location

import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.hunrmand.navigation.Routes
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
    navController: NavController,
    viewModel: LocationPickerViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val initialLatLng by viewModel.initialLatLng.collectAsState()
    
    // Default: Mianwali (fallback)
    val startPos = LatLng(32.5839, 71.5370)
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startPos, 15f)
    }
    
    LaunchedEffect(initialLatLng) {
        initialLatLng?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }
    
    var address by remember { mutableStateOf("Fetching address...") }
    var currentLatLng by remember { mutableStateOf(startPos) }
    var isMoving by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            isMoving = false
            val position = cameraPositionState.position.target
            currentLatLng = position
            
            // Reverse Geocoding
            scope.launch {
                address = try {
                    withContext(Dispatchers.IO) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                             // New async API could be used, but keeping sync for simplicity in this snippet or use listener
                             // For simplicity and compatibility:
                             @Suppress("DEPRECATION")
                             val addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1)
                             addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
                         } else {
                             @Suppress("DEPRECATION")
                             val addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1)
                             addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
                         }
                    }
                } catch (e: IOException) {
                    "Unable to fetch address"
                }
            }
        } else {
            isMoving = true
            address = "Locating..."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pick Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        navController.previousBackStackEntry?.savedStateHandle?.set("picked_location_lat", currentLatLng.latitude)
                        navController.previousBackStackEntry?.savedStateHandle?.set("picked_location_lng", currentLatLng.longitude)
                        navController.previousBackStackEntry?.savedStateHandle?.set("picked_location_address", address)
                        navController.popBackStack()
                    }) {
                        Text("Select", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            )
            
            // Center Marker
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "Center Marker",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
                    .offset(y = (-24).dp) // Offset to make the bottom of the icon point to center
                    .zIndex(1f),
                tint = Color.Red
            )
            
            // Address Box
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Selected Location", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(address, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.previousBackStackEntry?.savedStateHandle?.set("picked_location_lat", currentLatLng.latitude)
                            navController.previousBackStackEntry?.savedStateHandle?.set("picked_location_lng", currentLatLng.longitude)
                            navController.previousBackStackEntry?.savedStateHandle?.set("picked_location_address", address)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm Location")
                    }
                }
            }
        }
    }
}
