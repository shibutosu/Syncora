package com.example.office.presentation.profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ActivityGraph() {
    val values = listOf(3, 5, 1, 6, 4, 7, 2)
    val maxValue = values.maxOrNull() ?: 1
    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val spacing = size.width / (values.size - 1)
        val maxHeight = size.height

        for (i in 0 until values.size - 1) {
            val startX = i * spacing
            val startY = maxHeight - (values[i] / maxValue.toFloat() * maxHeight)
            val endX = (i + 1) * spacing
            val endY = maxHeight - (values[i + 1] / maxValue.toFloat() * maxHeight)

            drawLine(
                color = lineColor,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.cornerPathEffect(50f)
            )
        }
    }
}