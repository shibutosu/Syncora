package com.example.office

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MessagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.getStringExtra("userName") ?: "Unknown"
        setContent {
            ChatDetailScreen(userName)
        }
    }
}

@Composable
fun ChatDetailScreen(userName: String) {
    Scaffold(
        containerColor = Color(0xFF161616),
        topBar = { ChatDetailHeader(userName) }
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MessageList()
                MessageInput()
            }
        }
    }
}

@Composable
fun ChatDetailHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = { /* Назад */ },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF282828))
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.first().toString(),
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = userName,
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { /* Опции */ },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF282828))
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "Options",
                tint = Color.White
            )
        }
    }
}

@Composable
fun MessageList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text("Привет! Как дела?", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Все отлично! Работаю над проектом.", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Звучит круто! Удачи!", color = Color.White)
    }
}

@Composable
fun MessageInput() {
    var message by remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .background(Color(0xFF282828), CircleShape)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = { /* Отправить сообщение */ }) {
            Text("Отправить")
        }
    }
}
