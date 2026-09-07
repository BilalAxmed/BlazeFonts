package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
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
import com.example.engine.EmojiData
import com.example.ui.components.copyTextToClipboard
import com.example.ui.theme.*

@Composable
fun EmojiScreen() {
    val context = LocalContext.current
    val colors = LocalAppColors.current

    var selectedCategory by remember { mutableStateOf(EmojiData.CATEGORIES.first().name) }
    var selectedEmojiText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val currentCategory = remember(selectedCategory) {
        EmojiData.CATEGORIES.find { it.name == selectedCategory } ?: EmojiData.CATEGORIES.first()
    }

    val displayEmojis = remember(selectedCategory, searchQuery) {
        if (searchQuery.isBlank()) {
            currentCategory.emojis
        } else {
            EmojiData.CATEGORIES.flatMap { it.emojis }
                .distinct()
                .filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Hero Emoji Tray & Multi-Select Builder
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, colors.borderSubtle, RoundedCornerShape(22.dp)),
            color = colors.container,
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colors.primary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EMOJI COMBOS & PALETTE",
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    if (selectedEmojiText.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.chipBg
                        ) {
                            Text(
                                text = "${selectedEmojiText.length} items",
                                color = colors.textBody,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tray output box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp)),
                    color = colors.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedEmojiText.isEmpty()) "Tap emojis below to build combo..." else selectedEmojiText,
                            color = if (selectedEmojiText.isEmpty()) colors.textMuted else colors.textPrimary,
                            fontSize = if (selectedEmojiText.isEmpty()) 13.sp else 20.sp,
                            modifier = Modifier.weight(1f)
                        )

                        if (selectedEmojiText.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = colors.chipBg,
                                    modifier = Modifier.clickable { selectedEmojiText = "" }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = colors.textMuted,
                                        modifier = Modifier.padding(4.dp).size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { copyTextToClipboard(context, selectedEmojiText) },
                                    color = colors.primary
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Copy", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Categories Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(EmojiData.CATEGORIES) { category ->
                val isSelected = category.name == selectedCategory
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (isSelected) colors.primary else colors.chipBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            selectedCategory = category.name
                            searchQuery = ""
                        },
                    color = if (isSelected) colors.primary else colors.chipBg,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = category.name,
                        color = if (isSelected) Color.White else colors.textBody,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of Emojis
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 48.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(displayEmojis) { emoji ->
                Surface(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(12.dp))
                        .clickable {
                            selectedEmojiText += emoji
                        },
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}
