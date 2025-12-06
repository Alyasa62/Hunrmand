package com.example.hunrmand.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hunrmand.domain.model.Job

@Composable
fun JobItem(
    job: Job,
    onApplyClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(job.title, style = MaterialTheme.typography.titleLarge)
            Text("Budget: $${job.budget}", style = MaterialTheme.typography.bodyMedium)
            // Show location if available, assuming Job has location
            if (job.location.isNotBlank()) {
                Text("Location: ${job.location}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(job.description, style = MaterialTheme.typography.bodySmall)
            
            // Optional Apply Button - visible if needed
            // For now, BookingScreen might not need action, but reusing it is fine.
        }
    }
}
