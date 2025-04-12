package com.example.office

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.office.ui.theme.AppTheme
import com.example.office.ui.theme.ThemeState

@Suppress("DEPRECATION")
class ProfileActivity : ComponentActivity() {
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
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomNavigationBar(selectedItem = NavigationItem.Profile)
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Аватар
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(colors.primary)
                        .clickable {
                            context.startActivity(Intent(context, SettingsActivity::class.java))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AU",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Информация о пользователе
                Text(
                    text = "Anton Utomo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )
                Text(
                    text = "UI/UX Designer",
                    fontSize = 18.sp,
                    color = colors.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileInfoItem("Email", "anton.utomo@office.com")
                ProfileInfoItem("Phone", "+1 234 567 8901")
                ProfileInfoItem("Department", "Product Design")

                Spacer(modifier = Modifier.height(32.dp))

                ProfileStatistics()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileStatistics() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = "Activity Stats",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Weekly Activity",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = colors.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActivityGraph()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileStatItem("Completed Tasks", "32")
            ProfileStatItem("Ongoing Tasks", "5")
            ProfileStatItem("Completed Projects", "12")
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    val colors = MaterialTheme.colorScheme

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = colors.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ActivityGraph() {
    val values = listOf(3, 5, 1, 6, 4, 7, 2)
    val maxValue = values.maxOrNull() ?: 1
    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)) {

        val spacing = size.width / (values.size - 1)
        val maxHeight = size.height

        for (i in 0 until values.size - 1) {
            val startX = i * spacing
            val startY = maxHeight - (values[i] / maxValue.toFloat() * maxHeight)
            val endX = (i + 1) * spacing
            val endY = maxHeight - (values[i + 1] / maxValue.toFloat() * maxHeight)

            drawLine(
                color = lineColor,
                start = androidx.compose.ui.geometry.Offset(startX, startY),
                end = androidx.compose.ui.geometry.Offset(endX, endY),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.cornerPathEffect(50f)
            )
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 18.sp,
            color = colors.onSurface
        )
    }
}
