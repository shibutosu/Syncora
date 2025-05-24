package com.example.office.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.office.presentation.common.components.BottomNavigationBar
import com.example.office.presentation.common.components.NavigationItem
import com.example.office.presentation.profile.components.*
import com.example.office.presentation.profile.model.User

@Composable
fun ProfileScreen(
    user: User?,
    onSettingsClicked: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(selectedItem = NavigationItem.Profile)
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(onClick = onSettingsClicked),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user?.username?.take(2)?.uppercase() ?: "AU",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user?.fullName ?: "Имя Фамилия",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = user?.position ?: "Должность",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileInfoItem("Email", user?.email ?: "")
                ProfileInfoItem("Phone", user?.phoneNumber ?: "")
                ProfileInfoItem("Department", user?.department ?: "")
                ProfileInfoItem("Employee Code", user?.employeeCode ?: "")

                Spacer(modifier = Modifier.height(20.dp))

                ProfileStatistics()

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Выйти из профиля", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}