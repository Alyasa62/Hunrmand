package com.example.hunrmand.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hunrmand.navigation.Routes
import com.example.hunrmand.navigation.bottomNavItems
import com.example.hunrmand.ui.components.AppBottomBar
import com.example.hunrmand.ui.screens.booking.BookingScreen
import com.example.hunrmand.ui.screens.HomeScreen
import com.example.hunrmand.ui.screens.notification.NotificationScreen
import com.example.hunrmand.ui.screens.ProfileScreen
import com.example.hunrmand.ui.screens.SearchScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(navController = navController)
            }
            composable(Routes.BOOKING) {
                BookingScreen()
            }
            composable(Routes.NEW_JOB) {
                NewJobScreen()
            }
            // --- Added the missing Notification Route ---
            composable(Routes.NOTIFICATION) {
                NotificationScreen()
            }
            composable(Routes.PROFILE) {
                ProfileScreen()
            }
            composable(Routes.SEARCH) {
                SearchScreen()
            }
        }
    }
}