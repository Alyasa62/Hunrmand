package com.example.hunrmand.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import com.example.hunrmand.ui.components.NavItem

object Routes {
    const val HOME = "home"
    const val BOOKING = "booking"
    const val NEW_JOB = "new_job"
    const val PROFILE = "profile"
    const val NOTIFICATION = "notification"
    const val SEARCH = "search"
    
    // Auth
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Jobs
    const val JOB_FEED = "job_feed"

    // Maps
    const val MAP_SELECTION = "map_selection"

    // Dynamic Routes
    const val WORKER_LIST = "worker_list/{categoryId}"
    const val WORKER_DETAIL = "worker_detail/{workerId}"

    // Helper functions to build the route string
    fun getWorkerListRoute(categoryId: String) = "worker_list/$categoryId"
    fun getWorkerDetailRoute(workerId: String) = "worker_detail/$workerId"
}

val bottomNavItems = listOf(
    NavItem(
        route = Routes.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        contentDescription = "Home Screen"
    ),
    NavItem(
        route = Routes.BOOKING,
        label = "Bookings",
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Outlined.DateRange,
        contentDescription = "Booking Screen"
    ),
    NavItem(
        route = Routes.NEW_JOB,
        label = "Post",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Filled.Add,
        contentDescription = "New Job",
        isMainAction = true
    ),
    NavItem(
        route = Routes.NOTIFICATION,
        label = "Notification",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        contentDescription = "Notifications Screen"
    ),
    NavItem(
        route = Routes.PROFILE,
        label = "Profile",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        contentDescription = "Profile Screen"
    )
)