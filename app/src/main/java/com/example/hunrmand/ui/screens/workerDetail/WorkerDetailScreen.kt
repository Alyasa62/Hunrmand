package com.example.hunrmand.ui.screens.workerDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hunrmand.domain.model.UserRole
import com.example.hunrmand.domain.repository.WorkerRepository
import com.example.hunrmand.ui.screens.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDetailScreen(
    navController: NavController,
    workerId: String,
    authViewModel: AuthViewModel = koinViewModel()
) {
    val repository: WorkerRepository = koinInject()
    val scope = rememberCoroutineScope()
    var worker by remember { mutableStateOf(repository.getWorkerById(workerId)) }
    
    val currentUser by authViewModel.currentUser.collectAsState()

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
            Column {
                if (currentUser?.role == UserRole.ADMIN) {
                    Button(
                        onClick = { 
                            if (worker!!.rating < 2.0) {
                                scope.launch {
                                    repository.deleteWorker(workerId)
                                    navController.popBackStack()
                                }
                            } else {
                                // Show logic not allowed
                                // In a real app, use Snackbar
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        enabled = (worker?.rating ?: 0.0) < 2.0
                    ) {
                        Text("Remove Worker (Admin Only)")
                    }
                }
                
                Button(
                    onClick = { /* Handle booking logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp)
                ) {
                    Text("Book Now - ${worker?.hourlyRate}")
                }
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
                Text(worker?.name?.take(1) ?: "?", style = MaterialTheme.typography.headlineLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(worker?.name ?: "", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(worker?.profession ?: "", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Professional worker with rating ${worker?.rating}. Hourly Rate: ${worker?.hourlyRate}")
                }
            }
        }
    }
}