package com.example.hunrmand.ui.screens.workerDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hunrmand.data.repository.WorkerRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDetailScreen(
    navController: NavController,
    workerId: String
) {
    val repository = WorkerRepositoryImpl()
    val worker = repository.getWorkerById(workerId)

    if (worker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Worker not found")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { /* Handle booking logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text("Book Now - ${worker.hourlyRate}")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Big Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(worker.name.take(1), style = MaterialTheme.typography.headlineLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(worker.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(worker.city, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Professional worker with rating ${worker.rating}. Experienced in residential and commercial projects.")
                }
            }
        }
    }
}