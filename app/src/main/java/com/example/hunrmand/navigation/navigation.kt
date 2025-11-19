package com.example.hunrmand.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Stars
import com.example.hunrmand.ui.components.NavItem

object Routes {
    const val HOME = "home"
    const val BOOKING = "booking"
    const val NEW_JOB = "new_job"
    const val PROFILE = "profile"
    const val REWARDS = "rewards"
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
        route = Routes.REWARDS,
        label = "Rewards",
        selectedIcon = Icons.Filled.Stars,
        unselectedIcon = Icons.Outlined.Stars,
        contentDescription = "Rewards Screen"
    ),
    NavItem(
        route = Routes.PROFILE,
        label = "Profile",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        contentDescription = "Profile Screen"
    )
)