package com.example.hunrmand.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.CleaningServices
import com.example.hunrmand.domain.model.Category
import com.example.hunrmand.domain.model.Worker
import com.example.hunrmand.domain.repository.WorkerRepository

class WorkerRepositoryImpl : WorkerRepository {

    // Hardcoded Categories
    private val categories = listOf(
        Category("elec", "Electrician", Icons.Default.ElectricalServices),
        Category("plumb", "Plumber", Icons.Default.Build),
        Category("paint", "Painter", Icons.Default.Brush),
        Category("clean", "Cleaner", Icons.Default.CleaningServices)
    )

    // Hardcoded Workers
    private val workers = listOf(
        Worker("1", "Adnan", "elec", 4.8, "Islamabad", "$15/hr"),
        Worker("2", "Bilal", "elec", 4.5, "Rawalpindi", "$12/hr"),
        Worker("3", "Charlie", "plumb", 4.9, "Lahore", "$20/hr"),
        Worker("4", "David", "paint", 4.2, "Karachi", "$18/hr"),
        Worker("5", "Eve", "clean", 4.7, "Islamabad", "$10/hr")
    )

    override fun getCategories(): List<Category> {
        return categories
    }

    override fun getWorkersByCategory(categoryId: String): List<Worker> {
        return workers.filter { it.categoryId == categoryId }
    }

    override fun getWorkerById(workerId: String): Worker? {
        return workers.find { it.id == workerId }
    }
}