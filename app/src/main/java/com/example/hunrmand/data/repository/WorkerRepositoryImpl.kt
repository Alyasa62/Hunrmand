package com.example.hunrmand.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Grass
import com.example.hunrmand.domain.model.Category
import com.example.hunrmand.domain.model.Worker
import com.example.hunrmand.domain.repository.WorkerRepository

class WorkerRepositoryImpl : WorkerRepository {

    private val categories = listOf(
        Category("elec", "Electrician", Icons.Default.ElectricalServices),
        Category("plumb", "Plumber", Icons.Default.Build),
        Category("paint", "Painter", Icons.Default.Brush),
        Category("clean", "Cleaner", Icons.Default.CleaningServices),
        Category("garden", "Gardener", Icons.Default.Grass)
    )

    // Master list of all workers (Acting as your "Database" for now)
    private val workers = listOf(
        Worker("1", "Amar", "plumb", 4.8, "Expert Plumber", "$20/hr", isTopPick = true),
        Worker("2", "Hala", "clean", 4.9, "Expert Cleaner", "$15/hr", isTopPick = true),
        Worker("3", "Farida", "garden", 5.0, "Expert Gardener", "$25/hr", isTopPick = true),
        Worker("4", "David", "elec", 4.2, "Electrician", "$18/hr"), // Low rating
        Worker("5", "John", "paint", 3.5, "Painter", "$15/hr"),      // Low rating
        Worker("6", "Sarah", "elec", 4.9, "Pro Electrician", "$30/hr"), // High rating!
        Worker("7", "Bilal", "plumb", 4.7, "Plumber", "$18/hr")
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


    // "Select * From Workers Where rating >= 4.5 Order By rating DESC"
    override fun getTopRatedWorkers(): List<Worker> {
        return workers
            .filter { it.rating >= 4.7 }
            .sortedByDescending { it.rating }
    }
}