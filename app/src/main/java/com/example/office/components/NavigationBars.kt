package com.example.office

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NavigationItem {
    Home, Search, Chats, Calendar, Profile
}

@Composable
fun BottomNavigationBar(
    selectedItem: NavigationItem
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colors.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = if (selectedItem == NavigationItem.Home) colors.primary else colors.onSurface.copy(alpha = 0.6f)
                )
            },
            selected = selectedItem == NavigationItem.Home,
            onClick = {
                if (selectedItem != NavigationItem.Home) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = if (selectedItem == NavigationItem.Search) colors.primary else colors.onSurface.copy(alpha = 0.6f)
                )
            },
            selected = selectedItem == NavigationItem.Search,
            onClick = {
                if (selectedItem != NavigationItem.Search) {
                    context.startActivity(Intent(context, SearchApiActivity::class.java))
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = "Chats",
                    tint = if (selectedItem == NavigationItem.Chats) colors.primary else colors.onSurface.copy(alpha = 0.6f)
                )
            },
            selected = selectedItem == NavigationItem.Chats,
            onClick = {
                if (selectedItem != NavigationItem.Chats) {
                    context.startActivity(Intent(context, ChatsActivity::class.java))
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "Calendar",
                    tint = if (selectedItem == NavigationItem.Calendar) colors.primary else colors.onSurface.copy(alpha = 0.6f)
                )
            },
            selected = selectedItem == NavigationItem.Calendar,
            onClick = {
                if (selectedItem != NavigationItem.Calendar) {
                    context.startActivity(Intent(context, TodayTaskActivity::class.java))
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = if (selectedItem == NavigationItem.Profile) colors.primary else colors.onSurface.copy(alpha = 0.6f)
                )
            },
            selected = selectedItem == NavigationItem.Profile,
            onClick = {
                if (selectedItem != NavigationItem.Profile) {
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun HeaderNavigationBar(ActivityName: String, Menu: Boolean) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { (context as? Activity)?.finish() },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.onSurface
            )
        }

        Text(
            text = ActivityName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        if (Menu) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Menu",
                    tint = colors.onSurface
                )
            }
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }
    }

    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = colors.outline
    )
}
