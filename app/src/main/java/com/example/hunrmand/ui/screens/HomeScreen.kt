package com.example.hunrmand.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hunrmand.data.repository.WorkerRepositoryImpl
import com.example.hunrmand.navigation.Routes
import com.example.hunrmand.ui.components.CategoryItem

// Simple data class for Ads
data class AdBannerData(
    val title: String,
    val discount: String,
    val buttonText: String,
    val backgroundColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userName: String? = null,
    userCity: String? = null
) {
    val context = LocalContext.current
    val repository = WorkerRepositoryImpl() // Initialize repository
    val categories = repository.getCategories() // Fetch categories

    val nameToDisplay = userName ?: "User"
    val cityToDisplay = userCity ?: "Default City"

    val adsList = listOf(
        AdBannerData("Book AC Repair", "70% Off", "Book Now", Color(0xFFFFE0B2)),
        AdBannerData("Home Cleaning", "20% Off", "Clean Now", Color(0xFFBBDEFB)),
        AdBannerData("Plumbing Svc", "Fast", "Call Now", Color(0xFFC8E6C9))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Hello, $nameToDisplay", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, "Loc", tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                    Text(cityToDisplay, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                }
            }
            Surface(shape = CircleShape, color = Color(0xFFFF9800).copy(0.8f), modifier = Modifier.size(48.dp)) {
                IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menu", tint = Color.Black) }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Search ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).clickable { navController.navigate(Routes.SEARCH) },
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 6.dp
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, "Search", tint = Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Search electrician, plumber...", color = Color.Gray, modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Ads ---
        val pagerState = rememberPagerState(pageCount = { adsList.size })
        HorizontalPager(state = pagerState, pageSpacing = 16.dp) { page ->
            AdBannerItem(adsList[page])
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Categories ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("See All", color = Color.Gray, modifier = Modifier.clickable { })
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 3 items per row
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category = category) { categoryId ->
                    // Navigate to WorkerList with the ID
                    navController.navigate(Routes.getWorkerListRoute(categoryId))
                }
            }
        }
    }
}

@Composable
fun AdBannerItem(ad: AdBannerData) {
    Card(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ad.backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text(ad.title, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(ad.discount, fontWeight = FontWeight.ExtraBold, color = Color(0xFF00695C))
        }
    }
}