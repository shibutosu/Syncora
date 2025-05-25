package com.example.office.presentation.search.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun SearchHeader(
    searchQuery: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onFilterClick: () -> Unit,
    onClearQuery: () -> Unit,
    showHistory: Boolean,
    searchHistory: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    onFocus: () -> Unit,
    onDismissHistory: () -> Unit,
    onFilterIconPosition: (IntOffset) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(colors.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = colors.onSecondaryContainer)

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                textStyle = TextStyle(color = colors.onSecondaryContainer, fontSize = 16.sp),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        if (it.isFocused) onFocus()
                    },
                decorationBox = { innerTextField ->
                    if (searchQuery.text.isEmpty()) {
                        Text("Search...", color = colors.onSecondaryContainer.copy(alpha = 0.5f))
                    }
                    innerTextField()
                }
            )

            if (searchQuery.text.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = colors.onSecondaryContainer,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onClearQuery()
                        }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = colors.onSecondaryContainer,
                modifier = Modifier
                    .size(20.dp)
                    .onGloballyPositioned {
                        val offset = it.localToWindow(Offset.Zero)
                        onFilterIconPosition(IntOffset(offset.x.roundToInt(), offset.y.roundToInt()))
                    }
                    .clickable { onFilterClick() }
            )
        }
    }
}