package com.example.office.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.office.presentation.main.model.Task

@Composable
fun PriorityTaskCard() {
    val tasks = remember {
        mutableStateListOf(
            Task("Doing exercise 45 min", true),
            Task("Doing meditation"),
            Task("Read a self improvement book"),
            Task("Buy coffee in the shop")
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFC8A3D6))
            .padding(16.dp)
            .fillMaxHeight()
    ) {
        Column {
            Text(
                text = "My Priority Task",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF836190),
            )

            tasks.forEach { task ->
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isChecked = remember { mutableStateOf(task.done) }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isChecked.value) Color(0xFF553B59) else Color.Transparent)
                            .border(1.5.dp, Color.DarkGray, RoundedCornerShape(4.dp))
                            .clickable {
                                isChecked.value = !isChecked.value
                                task.done = isChecked.value
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked.value) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Checked",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        color = if (task.done) Color.DarkGray else Color.Black,
                        style = if (task.done) TextStyle(textDecoration = TextDecoration.LineThrough)
                        else TextStyle(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 40.dp)
                    )
                }
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
