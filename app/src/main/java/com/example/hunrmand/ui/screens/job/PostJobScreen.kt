package com.example.hunrmand.ui.screens.job

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hunrmand.ui.screens.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostJobScreen(
    onJobPosted: () -> Unit,
    onPickLocation: () -> Unit,
    pickedAddress: String?,
    pickedLat: Double?,
    pickedLng: Double?,
    viewModel: PostJobViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel() // To get creator ID
) {
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val budget by viewModel.budget.collectAsState()
    
    val postState by viewModel.postJobState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(postState) {
        if (postState?.isSuccess == true) {
            onJobPosted()
            viewModel.resetPostJobState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Post a New Job", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { viewModel.title.value = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = { viewModel.description.value = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = budget,
            onValueChange = { viewModel.budget.value = it },
            label = { Text("Budget (PKR)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Location Picker
        if (pickedAddress != null) {
            Text("Location: $pickedAddress", style = MaterialTheme.typography.bodyMedium)
        } else {
             Text("Location: Not Selected", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
        }
        
        Button(
            onClick = onPickLocation,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Pick Location on Map")
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                currentUser?.id?.let { creatorId ->
                    viewModel.postJob(creatorId, pickedLat, pickedLng, pickedAddress) 
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && description.isNotBlank() && budget.isNotBlank() && currentUser != null && pickedLat != null
        ) {
            Text("Post Job")
        }
        
        if (postState?.isFailure == true) {
            Text(
                text = "Error: ${postState?.exceptionOrNull()?.message}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
