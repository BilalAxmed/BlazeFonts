package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.FontEngine
import com.example.ui.components.FontResultCard
import com.example.ui.components.copyTextToClipboard
import com.example.ui.theme.*

@Composable
fun FontGeneratorScreen(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onNavigateToTweetComposer: (String) -> Unit
) {
    val context = LocalContext.current
    val colors = LocalAppColors.current

    var selectedCategory by remember { mutableStateOf("All") }
    var favoriteStyles by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    val maxCharLimit = 10000
    val charCount = inputText.length
    val wordCount = if (inputText.isBlank()) 0 else inputText.trim().split("\\s+".toRegex()).size

    val categories = remember {
        listOf("All", "Favorites", "Sans", "Serif", "Fancy", "Decorated", "Special", "Twitter/X Best")
    }

    val displayInput = if (inputText.isBlank()) "Blaze your tweet with style" else inputText

    val allStyles = remember { FontEngine.ALL_STYLES }

    val filteredStyles = remember(selectedCategory, favoriteStyles, searchQuery) {
        allStyles.filter { style ->
            val matchesCategory = when (selectedCategory) {
                "All" -> true
                "Favorites" -> favoriteStyles.contains(style.id)
                "Sans" -> style.category == "Sans"
                "Serif" -> style.category == "Serif"
                "Fancy" -> style.category == "Fancy"
                "Decorated" -> style.category == "Decorated"
                "Special" -> style.category == "Special" || style.category == "Enclosed"
                "Twitter/X Best" -> style.id in listOf(
                    "sans_bold", "sans_italic", "serif_bold_italic", "double_struck",
                    "monospace", "small_caps", "sans_bold_italic", "quote_style_curly",
                    "serif_italic", "serif_bold"
                )
                else -> true
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                style.name.contains(searchQuery, ignoreCase = true) ||
                        style.category.contains(searchQuery, ignoreCase = true)
            }
            matchesCategory && matchesSearch
        }
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
                // Header in Card
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
                            text = "LIVE FONT ENGINE",
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (charCount > 9500) Color.Red.copy(alpha = 0.15f) else colors.chipBg,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (charCount > 9500) Color.Red.copy(alpha = 0.35f) else colors.chipBorder
                            )
                        ) {
                            Text(
                                text = if (wordCount > 0) "$wordCount words • $charCount/$maxCharLimit" else "10K words limit",
                                color = if (charCount > 9500) Color.Red else colors.textBody,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showSearch = !showSearch },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                if (showSearch) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search fonts",
                                tint = colors.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        if (inputText.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = colors.chipBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, colors.chipBorder),
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

                // Search Bar with animation
                AnimatedVisibility(
                    visible = showSearch,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(14.dp))
                                .background(colors.surface),
                            placeholder = { Text("Filter fonts (e.g. bold, script, cursive)...", color = colors.textMuted, fontSize = 13.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colors.surface,
                                unfocusedContainerColor = colors.surface,
                                focusedTextColor = colors.textPrimary,
                                unfocusedTextColor = colors.textPrimary,
                                cursorColor = colors.primary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear search", tint = colors.textMuted, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = inputText,
                    onValueChange = {
                        if (it.length <= maxCharLimit) onInputTextChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Type or paste text (supports up to 10,000 words & chars)...",
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
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Quick Action Buttons in Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Paste button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    val item = cm?.primaryClip?.getItemAt(0)
                                    item?.text?.toString()?.let {
                                        val combined = (inputText + " " + it).take(maxCharLimit)
                                        onInputTextChange(combined)
                                    }
                                },
                            color = colors.chipBg,
                            shadowElevation = if (colors.isDark) 0.dp else 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ContentPaste, contentDescription = null, tint = colors.textBody, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Paste", color = colors.textBody, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Sample text quick button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    onInputTextChange("Write tweets in style ✨")
                                },
                            color = colors.chipBg,
                            shadowElevation = if (colors.isDark) 0.dp else 1.dp
                        ) {
                            Text(
                                text = "Sample",
                                color = colors.textBody,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp)
                            )
                        }

                        // Thread Composer quick button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    onNavigateToTweetComposer(displayInput)
                                },
                            color = colors.chipBg,
                            shadowElevation = if (colors.isDark) 0.dp else 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("𝕏", color = colors.textBody, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Thread", color = colors.textBody, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // Copy Top Bold Result Quick Action
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                val topStyled = FontEngine.transform(displayInput, "sans_bold")
                                copyTextToClipboard(context, topStyled)
                            },
                        color = colors.primary,
                        shadowElevation = if (colors.isDark) 0.dp else 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Copy Bold", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Categories Filter Carousel
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
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else colors.textBody,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (category == "Favorites" && favoriteStyles.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) Color.White.copy(alpha = 0.3f) else colors.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${favoriteStyles.size}",
                                    color = if (isSelected) Color.White else colors.primary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
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
                text = "${filteredStyles.size} STYLES READY",
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

        // Font List or Empty State
        if (filteredStyles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (selectedCategory == "Favorites") Icons.Default.FavoriteBorder else Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = colors.textMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (selectedCategory == "Favorites") "No favorites saved yet" else "No matching font styles",
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedCategory == "Favorites") "Tap the heart on any font to pin it here" else "Try clearing your search query",
                        color = colors.textMuted,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredStyles, key = { it.id }) { style ->
                    val transformed = style.transformer(displayInput)
                    val isFav = favoriteStyles.contains(style.id)

                    FontResultCard(
                        fontName = style.name,
                        category = style.category,
                        transformedText = transformed,
                        isFavorite = isFav,
                        onToggleFavorite = {
                            favoriteStyles = if (isFav) favoriteStyles - style.id else favoriteStyles + style.id
                        },
                        onCardClick = {}
                    )
                }
            }
        }
    }
}
