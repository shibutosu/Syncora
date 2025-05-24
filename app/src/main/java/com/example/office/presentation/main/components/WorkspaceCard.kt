package com.example.office.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkspaceCard(modifier: Modifier = Modifier) {
    val sharedWorkspaces = listOf("✅ Layerio Daily", "🌎 Our project", "💼 Layeriotion")
    val privateWorkspaces = listOf("Human Resource", "Employee Salary")

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEADABA))
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            Text(
                text = "Shared Workspace",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFBDAB8C)
            )

            sharedWorkspaces.forEach {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Private Workspace",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFBDAB8C)
            )

            privateWorkspaces.forEach {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = "Open",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
