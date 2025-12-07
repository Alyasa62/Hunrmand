package com.example.hunrmand.ui.screens.job

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    navController: NavController,
    jobId: String,
    viewModel: JobDetailViewModel = koinViewModel()
) {
    val job by viewModel.job.collectAsState()
    val bidState by viewModel.bidState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    LaunchedEffect(jobId) {
        viewModel.loadJob(jobId)
    }

    LaunchedEffect(bidState) {
        if (bidState?.isSuccess == true) {
            showBottomSheet = false
            viewModel.resetBidState()
            // optionally show snackbar
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
           PlaceBidSheet(
               onPlaceBid = { amount, time ->
                   viewModel.placeBid(jobId, amount, time)
               }
           )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            job?.let { j ->
                Text(j.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Budget: PKR ${j.budget}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(j.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Location", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(j.location, style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Place Bid")
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun PlaceBidSheet(onPlaceBid: (Double, String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Place Your Bid", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Bid Amount (PKR)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Estimated Time (e.g. 2 days)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { 
                amount.toDoubleOrNull()?.let { validAmount ->
                    onPlaceBid(validAmount, time)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = amount.isNotBlank() && time.isNotBlank()
        ) {
            Text("Submit Bid")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
