package com.example.office

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState

@Suppress("DEPRECATION")
class ChatsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        setContent {
            val context = LocalContext.current
            val themeState = remember { ThemeState(context) }
            AppTheme(themeState) {
                ChatsScreen()
            }
        }
    }
}

@Composable
fun ChatsScreen() {
    var selectedTab by remember { mutableStateOf("Message") }
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomNavigationBar(selectedItem = NavigationItem.Chats)
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(colors.surfaceVariant)
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    EnhancedTabButton("Message", selectedTab) { selectedTab = "Message" }
                    EnhancedTabButton("Mail", selectedTab) { selectedTab = "Mail" }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    "Message" -> ChatList()
                    "Mail" -> MailList()
                }
            }
        }
    }
}

@Composable
fun EnhancedTabButton(title: String, selectedTab: String, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val isSelected = selectedTab == title

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(if (isSelected) colors.primary else Color.Transparent)
                .padding(vertical = 8.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                color = if (isSelected) colors.onPrimary else colors.onSurfaceVariant,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ChatList() {
    val chatItems = listOf(
        ChatItem("Alice Smith", "Hey, how's the project going?", "10:30 AM"),
        ChatItem("Bob Johnson", "Don't forget about the meeting tomorrow.", "09:15 AM"),
        ChatItem("Evan Alex", "Can you review the document I sent?", "Yesterday"),
        ChatItem("Team Layerio", "Project update: Phase 2 complete!", "Yesterday"),
        ChatItem("John Doe", "Great job on the presentation!", "2 days ago")
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(chatItems) { chat ->
            ChatItemView(chat)
            Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        }
    }
}

@Composable
fun ChatItemView(chat: ChatItem) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable {
                val intent = Intent(context, MessagerActivity::class.java)
                intent.putExtra("userName", chat.name)
                context.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chat.name.first().toString(),
                fontSize = 20.sp,
                color = colors.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = chat.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
            Text(
                text = chat.message,
                fontSize = 14.sp,
                color = colors.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = chat.time,
            fontSize = 12.sp,
            color = colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun MailList() {
    val mailItems = listOf(
        MailItem("Project Update", "Layerio Team", "Phase 2 has been completed successfully.", "2 days ago"),
        MailItem("Meeting Reminder", "HR Department", "Don't forget the all-hands meeting tomorrow.", "1 day ago"),
        MailItem("Invoice", "Finance Team", "March invoice is ready for review.", "Today"),
        MailItem("Design Review", "UI/UX Team", "Please review the latest wireframes.", "Yesterday")
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(mailItems) { mail ->
            MailItemView(mail)
            Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        }
    }
}

@Composable
fun MailItemView(mail: MailItem) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { }
    ) {
        Text(
            text = mail.subject,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )
        Text(
            text = "${mail.sender} • ${mail.time}",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant
        )
        Text(
            text = mail.preview,
            fontSize = 14.sp,
            color = colors.onBackground,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

data class ChatItem(val name: String, val message: String, val time: String)
data class MailItem(val subject: String, val sender: String, val preview: String, val time: String)
