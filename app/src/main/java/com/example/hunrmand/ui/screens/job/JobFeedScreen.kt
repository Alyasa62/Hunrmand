package com.example.hunrmand.ui.screens.job

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.hunrmand.ui.components.JobItem

@Composable
fun JobFeedScreen(
    viewModel: PostJobViewModel = koinViewModel()
) {
    val jobs by viewModel.allJobs.collectAsState(initial = emptyList())

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Job Feed", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(jobs) { job ->
            JobItem(job = job)
        }
        
        if (jobs.isEmpty()) {
            item {
                Text("No jobs available yet.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
