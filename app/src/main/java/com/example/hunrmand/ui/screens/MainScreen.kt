package com.example.hunrmand.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hunrmand.navigation.Routes
import com.example.hunrmand.navigation.bottomNavItems
import com.example.hunrmand.ui.components.AppBottomBar
import com.example.hunrmand.ui.screens.booking.BookingScreen
import com.example.hunrmand.ui.screens.home.HomeScreen
import com.example.hunrmand.ui.screens.maps.MapScreen
import com.example.hunrmand.ui.screens.notification.NotificationScreen
import com.example.hunrmand.ui.screens.workerList.WorkerListScreen
import com.example.hunrmand.ui.screens.workerDetail.WorkerDetailScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Only show bottom bar on main tabs, not deep detail screens
            val showBottomBar = bottomNavItems.any { it.route == currentRoute }

            if (showBottomBar) {
                AppBottomBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onItemSelected = { item ->
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen(navController = navController) }
            composable(Routes.BOOKING) { BookingScreen() }
            composable(Routes.NEW_JOB) { NewJobScreen() }
            composable(Routes.NOTIFICATION) { NotificationScreen() }
            composable(Routes.PROFILE) { ProfileScreen() }
            composable(Routes.SEARCH) { SearchScreen() }

            // Dynamic Route: List of Workers for a Category
            composable(
                route = Routes.WORKER_LIST,
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                WorkerListScreen(navController = navController, categoryId = categoryId)
            }

            // Dynamic Route: Detail of a specific Worker
            composable(
                route = Routes.WORKER_DETAIL,
                arguments = listOf(navArgument("workerId") { type = NavType.StringType })
            ) { backStackEntry ->
                val workerId = backStackEntry.arguments?.getString("workerId") ?: ""
                WorkerDetailScreen(navController = navController, workerId = workerId)
            }
            composable(Routes.MAP_SELECTION) {
                MapScreen(navController = navController)
            }
        }
    }
}