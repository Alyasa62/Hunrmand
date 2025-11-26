package com.example.hunrmand.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hunrmand.domain.model.Category

@Composable
fun CategoryItem(
    category: Category,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF3E0)) // Light orange background
            .clickable { onClick(category.id) }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = category.name,
            modifier = Modifier.size(40.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}