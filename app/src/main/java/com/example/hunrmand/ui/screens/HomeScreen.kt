package com.example.hunrmand.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hunrmand.data.repository.WorkerRepositoryImpl
import com.example.hunrmand.navigation.Routes
import com.example.hunrmand.ui.components.CategoryItem
import com.example.hunrmand.ui.components.WorkerHomeItem

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
    val repository = WorkerRepositoryImpl()
    val categories = repository.getCategories()

    // --- DYNAMIC SELECTION ---
    // Now we ask the repository for top rated workers dynamically.
    // In the future, this function call will trigger a DB query.
    val topWorkers = repository.getTopRatedWorkers()

    val nameToDisplay = userName ?: "Adnan"
    val cityToDisplay = userCity ?: "City, ABC"

    val adsList = listOf(
        AdBannerData("Book AC Repair", "70% Off", "Book Now", Color(0xFFFFE0B2)),
        AdBannerData("Home Cleaning", "20% Off", "Clean Now", Color(0xFFBBDEFB)),
        AdBannerData("Plumbing Svc", "Fast", "Call Now", Color(0xFFC8E6C9))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .verticalScroll(rememberScrollState())
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Search ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).clickable { navController.navigate(Routes.SEARCH) },
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, "Search", tint = Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Search electrician, plumber...", color = Color.Gray, modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Ads Pager ---
        val pagerState = rememberPagerState(pageCount = { adsList.size })
        HorizontalPager(state = pagerState, pageSpacing = 16.dp) { page ->
            AdBannerItem(adsList[page])
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Pager Indicators ---
        Row(
            Modifier.height(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(adsList.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(modifier = Modifier.padding(4.dp).clip(CircleShape).background(color).size(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Categories ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("See All", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(240.dp)

        ) {
            items(categories) { category ->
                CategoryItem(category = category) { categoryId ->
                    navController.navigate(Routes.getWorkerListRoute(categoryId))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Top Rated Workers ---
        Text("Top Rated", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(topWorkers) { worker ->
                WorkerHomeItem(worker = worker) { workerId ->
                    navController.navigate(Routes.getWorkerDetailRoute(workerId))
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
            Text(ad.title, fontWeight = FontWeight.Bold, color = Color.Black, style = MaterialTheme.typography.titleMedium)
            Text(ad.discount, fontWeight = FontWeight.ExtraBold, color = Color(0xFF00695C), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(ad.buttonText, color = Color.Black, fontSize = 12.sp)
            }
        }
    }
}