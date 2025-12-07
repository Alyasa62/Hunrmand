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
import com.example.hunrmand.ui.screens.auth.LoginScreen
import com.example.hunrmand.ui.screens.auth.RegistrationScreen
import com.example.hunrmand.ui.screens.job.PostJobScreen
import com.example.hunrmand.ui.screens.job.JobFeedScreen
import com.example.hunrmand.ui.screens.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.hunrmand.domain.model.UserRole

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    // Determine start destination based on session presence
    // If currentUser is initially null (loading), we might show a Splash. 
    // But assuming AuthViewModel loads initially fast from DataStore.
    // Ideally we'd have a specific "isLoading" state. 
    // For this task, we'll route dynamically.
    
    val startDestination = if (currentUser != null) Routes.HOME else Routes.LOGIN
    
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
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { 
                // Check Role and Show Appropriate Screen
                if (currentUser?.role == UserRole.WORKER) {
                    com.example.hunrmand.ui.screens.worker.WorkerHomeScreen(navController = navController)
                } else {
                    HomeScreen(navController = navController) 
                }
            }
            composable(Routes.BOOKING) { BookingScreen(navController = navController) }
            composable(Routes.NEW_JOB) { NewJobScreen() }
            composable(Routes.NOTIFICATION) { NotificationScreen() }
            composable(Routes.PROFILE) { ProfileScreen(navController = navController) }
            composable(Routes.SEARCH) { SearchScreen(navController = navController) }

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
            composable(Routes.LOCATION_PICKER) {
                com.example.hunrmand.ui.screens.location.LocationPickerScreen(navController = navController)
            }
            
            // Auth Routes
            composable(Routes.LOGIN) { 
                LoginScreen(
                    onLoginSuccess = { 
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    }
                ) 
            }
            composable(Routes.REGISTER) { 
                RegistrationScreen(
                    onRegistrationSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN)
                    }
                ) 
            }
            
            // Job Routes
            composable(Routes.NEW_JOB) { backStackEntry -> 
                // Retrieve result from LocationPicker
                val pickedLat = backStackEntry.savedStateHandle.get<Double>("picked_location_lat")
                val pickedLng = backStackEntry.savedStateHandle.get<Double>("picked_location_lng")
                val pickedAddress = backStackEntry.savedStateHandle.get<String>("picked_location_address")
                
                PostJobScreen(
                    onJobPosted = {
                        navController.popBackStack()
                    },
                    onPickLocation = {
                         navController.navigate(Routes.LOCATION_PICKER)
                    },
                    pickedLat = pickedLat,
                    pickedLng = pickedLng,
                    pickedAddress = pickedAddress
                )
            }
            composable(Routes.JOB_FEED) {
                JobFeedScreen()
            }
            
            composable(
                route = "job_detail/{jobId}",
                arguments = listOf(androidx.navigation.navArgument("jobId") { type = NavType.StringType })
            ) { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                com.example.hunrmand.ui.screens.job.JobDetailScreen(navController = navController, jobId = jobId)
            }
        }
    }
}