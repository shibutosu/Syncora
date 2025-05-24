package com.example.office.presentation.search.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.office.presentation.search.model.OfficeSearchResult

@Composable
fun SearchResultItem(result: OfficeSearchResult) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (result.category) {
                    "Tasks" -> "✓"
                    "Workspace" -> "◉"
                    "Mail" -> "✉"
                    "Calendar" -> "📅"
                    "Documents" -> "📄"
                    else -> "🔍"
                },
                fontSize = 18.sp,
                color = colors.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = result.title,
                color = colors.onBackground,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${result.type} • ${result.category}",
                color = colors.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ArrowOutward,
            contentDescription = null,
            tint = colors.outline,
            modifier = Modifier.size(20.dp)
        )
    }
}