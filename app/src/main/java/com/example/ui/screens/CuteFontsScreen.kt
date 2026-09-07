package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.CuteFonts
import com.example.ui.components.FontResultCard
import com.example.ui.theme.*

@Composable
fun CuteFontsScreen(
    inputText: String,
    onInputTextChange: (String) -> Unit
) {
    val colors = LocalAppColors.current
    val displayInput = if (inputText.isBlank()) "aesthetic vibes" else inputText
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember {
        listOf("All", "Hearts", "Ribbons", "Sparkles", "Kaomoji", "Flowers", "Nature", "Aesthetic")
    }

    val allCute = remember { CuteFonts.CUTE_STYLES }

    val filteredCute = remember(selectedCategory) {
        if (selectedCategory == "All") allCute
        else allCute.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Hero Input Container
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
                            text = "KAWAII & CUTE FONTS",
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.chipBg
                    ) {
                        Text(
                            text = "${inputText.length}/10,000",
                            color = colors.textBody,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = inputText,
                    onValueChange = {
                        if (it.length <= 10000) onInputTextChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Type text to make cute (e.g. sweetheart, angel)...",
                            color = colors.textMuted,
                            fontSize = 15.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary,
                        cursorColor = colors.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(10.dp))
                                .clickable { onInputTextChange("Princess Glow") },
                            color = colors.chipBg
                        ) {
                            Text(
                                text = "Princess",
                                color = colors.textBody,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(10.dp))
                                .clickable { onInputTextChange("angelic dream") },
                            color = colors.chipBg
                        ) {
                            Text(
                                text = "Angelic",
                                color = colors.textBody,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    if (inputText.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.chipBg,
                            modifier = Modifier.clickable { onInputTextChange("") }
                        ) {
                            Text(
                                text = "Clear",
                                color = colors.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
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
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (isSelected) colors.primary else colors.chipBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedCategory = category },
                    color = if (isSelected) colors.primary else colors.chipBg,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else colors.textBody,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Results Count Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredCute.size} CUTE STYLES READY",
                color = colors.textMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            )
            Text(
                text = "Tap card to copy",
                color = colors.textMuted,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Cute Styles List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredCute, key = { it.id }) { item ->
                val transformed = item.generator(displayInput)
                FontResultCard(
                    fontName = item.name,
                    category = item.category,
                    transformedText = transformed
                )
            }
        }
    }
}
