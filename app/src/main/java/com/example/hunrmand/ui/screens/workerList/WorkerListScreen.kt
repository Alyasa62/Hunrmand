package com.example.hunrmand.ui.screens.workerList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hunrmand.data.repository.WorkerRepositoryImpl
import com.example.hunrmand.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerListScreen(
    navController: NavController,
    categoryId: String
) {
    val repository = WorkerRepositoryImpl()
    val workers = repository.getWorkersByCategory(categoryId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Workers") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(workers) { worker ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Routes.getWorkerDetailRoute(worker.id))
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Placeholder for Image
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = Color.LightGray
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(worker.name.take(1))
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(worker.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(worker.city, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                                Text(" ${worker.rating}", style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(worker.hourlyRate, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}