package com.example.hunrmand.ui.screens.job

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
    viewModel: PostJobViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel() // To get creator ID
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    
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
    ) {
        Text("Post a New Job", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = budget,
            onValueChange = { budget = it },
            label = { Text("Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                currentUser?.id?.let { creatorId ->
                    viewModel.postJob(title, description, budget.toDoubleOrNull() ?: 0.0, creatorId) 
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && description.isNotBlank() && budget.isNotBlank() && currentUser != null
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
