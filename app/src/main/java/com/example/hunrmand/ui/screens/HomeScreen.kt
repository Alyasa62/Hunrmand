package com.example.hunrmand.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.example.hunrmand.data.repository.WorkerRepositoryImpl
import com.example.hunrmand.navigation.Routes
import com.example.hunrmand.ui.components.CategoryItem
import com.example.hunrmand.ui.components.WorkerHomeItem
import kotlin.math.absoluteValue

data class AdBannerData(
    val title: String,
    val discount: String,
    val buttonText: String
    // We will use dynamic theme colors for backgrounds now
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
    val topWorkers = repository.getTopRatedWorkers()

    val nameToDisplay = userName ?: "User"
    val cityToDisplay = userCity ?: "City, ABC"

    val adsList = listOf(
        AdBannerData("Book AC Repair", "70% Off", "Book Now"),
        AdBannerData("Home Cleaning", "20% Off", "Clean Now"),
        AdBannerData("Plumbing Svc", "Fast", "Call Now")
    )

    // Animation State
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(animationSpec = tween(500)) { 50 } + fadeIn(animationSpec = tween(500))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, $nameToDisplay",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Loc",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = cityToDisplay,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. Search ---
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(animationSpec = tween(500, delayMillis = 100)) { 50 } + fadeIn(animationSpec = tween(500, delayMillis = 100))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { navController.navigate(Routes.SEARCH) },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Subtle grey
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search electrician, plumber...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. Ads Pager ---
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(animationSpec = tween(500, delayMillis = 200)) { 50 } + fadeIn(animationSpec = tween(500, delayMillis = 200))
        ) {
            Column {
                val pagerState = rememberPagerState(pageCount = { adsList.size })
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    pageSpacing = 16.dp
                ) { page ->
                    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    val scale = lerp(0.92f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))

                    Box(modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale.coerceAtLeast(0.5f)
                    }) {
                        AdBannerItem(adsList[page], page)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(adsList.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(if (pagerState.currentPage == iteration) 10.dp else 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. Categories  ---
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(animationSpec = tween(500, delayMillis = 300)) { 50 } + fadeIn(animationSpec = tween(500, delayMillis = 300))
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { /* See All */ }) {
                        Text("See All", color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(240.dp),
                    userScrollEnabled = false
                ) {
                    items(categories) { category ->
                        CategoryItem(category = category) { categoryId ->
                            navController.navigate(Routes.getWorkerListRoute(categoryId))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. Top Rated  ---
        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(animationSpec = tween(500, delayMillis = 400)) { 50 } + fadeIn(animationSpec = tween(500, delayMillis = 400))
        ) {
            Column {
                Text(
                    text = "Top Rated",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
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
    }
}

@Composable
fun AdBannerItem(ad: AdBannerData, index: Int) {

    val containerColor = when(index % 3) {
        0 -> MaterialTheme.colorScheme.primaryContainer
        1 -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val contentColor = when(index % 3) {
        0 -> MaterialTheme.colorScheme.onPrimaryContainer
        1 -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = ad.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = contentColor
                )
                Text(
                    text = ad.discount,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentColor,
                        contentColor = containerColor
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(ad.buttonText, style = MaterialTheme.typography.labelLarge)
                }
            }
            // Placeholder for illustrative image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.2f))
            )
        }
    }
}