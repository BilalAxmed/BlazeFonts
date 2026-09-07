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
import com.example.engine.NumberFonts
import com.example.ui.components.FontResultCard
import com.example.ui.theme.*

@Composable
fun NumberFontsScreen(
    inputNumber: String,
    onInputNumberChange: (String) -> Unit
) {
    val colors = LocalAppColors.current
    val displayInput = if (inputNumber.isBlank()) "2026 - 12345" else inputNumber
    val numberStyles = remember { NumberFonts.NUMBER_STYLES }
    val numberPresets = remember { listOf("2026", "100K+", "0123456789", "+1 (555) 019", "99.99%", "$1,000,000") }

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
                            text = "NUMBER & DIGIT STYLIST",
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
                            text = "${inputNumber.length}/10,000",
                            color = colors.textBody,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = inputNumber,
                    onValueChange = {
                        if (it.length <= 10000) onInputNumberChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Type digits or numbers (e.g. 2026, 12345)...",
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
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Presets Carousel
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(numberPresets) { preset ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(10.dp))
                                .clickable { onInputNumberChange(preset) },
                            color = colors.chipBg
                        ) {
                            Text(
                                text = preset,
                                color = colors.textBody,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Results Count Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${numberStyles.size} NUMBER FONTS READY",
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

        // Number Styles List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(numberStyles, key = { it.id }) { style ->
                val transformed = style.convert(displayInput)
                FontResultCard(
                    fontName = style.name,
                    category = "Numbers",
                    transformedText = transformed
                )
            }
        }
    }
}
