package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.engine.FontEngine
import com.example.ui.components.copyTextToClipboard
import com.example.ui.components.shareText
import com.example.ui.components.tweetText
import com.example.ui.theme.*

@Composable
fun TweetComposerScreen(
    initialText: String = "",
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val colors = LocalAppColors.current

    var tweetBody by remember { mutableStateOf(initialText) }
    val maxLimit = 10000
    val singleTweetLimit = 280
    val charCount = tweetBody.length
    val wordCount = if (tweetBody.isBlank()) 0 else tweetBody.trim().split("\\s+".toRegex()).size
    val threadParts = if (charCount <= singleTweetLimit) 1 else (charCount / 260) + 1

    val quickStyles = listOf(
        "sans_bold" to "𝗕𝗼𝗹𝗱",
        "sans_italic" to "𝘐𝘵𝘢𝘭𝘪𝘤",
        "serif_bold_italic" to "𝑩𝒐𝒍𝒅 𝑰𝒕𝒂𝒍𝒊𝒄",
        "double_struck" to "𝔻𝕠𝕦𝕓𝕝𝕖",
        "monospace" to "𝙼𝚘𝚗𝚘",
        "small_caps" to "ꜱᴍᴀʟʟ",
        "quote_style_curly" to "“Quote”",
        "bubble_circled" to "Ⓑⓤⓑⓑⓛⓔ",
        "wide_fullwidth" to "Ｗｉｄｅ"
    )

    val tweetTemplates = listOf(
        "🚀 Big announcement: " to "Announcement",
        "🔥 Hot take: " to "Hot Take",
        "🧵 Thread on why " to "Thread",
        "✨ Pro tip: " to "Pro Tip",
        "💡 Insight of the day: " to "Insight",
        "📈 Milestone unlocked: " to "Milestone"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Hero Composer Container
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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "𝕏",
                            color = colors.textPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "THREAD & POST COMPOSER",
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                    }

                    // Counter Pill
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, colors.chipBorder, RoundedCornerShape(10.dp)),
                        color = colors.chipBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$charCount / 10,000",
                                color = colors.textBody,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (threadParts > 1) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "($threadParts tweets)",
                                    color = colors.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Text Field
                TextField(
                    value = tweetBody,
                    onValueChange = {
                        if (it.length <= maxLimit) tweetBody = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp),
                    placeholder = {
                        Text(
                            "Compose stylized tweet, thread, bio or post (up to 10,000 chars)...",
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
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Format Toolbar
                Text(
                    text = "APPLY FONT TO ALL",
                    color = colors.textMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(quickStyles) { (styleId, label) ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(8.dp))
                                .clickable {
                                    if (tweetBody.isNotBlank()) {
                                        tweetBody = FontEngine.transform(tweetBody, styleId)
                                    }
                                },
                            color = colors.chipBg
                        ) {
                            Text(
                                text = label,
                                color = colors.textPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Template starters
                Text(
                    text = "HOOK STARTERS",
                    color = colors.textMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(tweetTemplates) { (prefix, label) ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(8.dp))
                                .clickable {
                                    tweetBody = prefix + tweetBody
                                },
                            color = colors.chipBg
                        ) {
                            Text(
                                text = label,
                                color = colors.textBody,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions Bottom Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (tweetBody.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = colors.chipBg,
                                modifier = Modifier.clickable { tweetBody = "" }
                            ) {
                                Text(
                                    text = "Clear",
                                    color = colors.primary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(10.dp))
                                .clickable { shareText(context, tweetBody) },
                            color = colors.chipBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = colors.textBody, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", color = colors.textBody, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // Main Post / Copy
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                                .clickable { copyTextToClipboard(context, tweetBody) },
                            color = colors.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = colors.primary, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy", color = colors.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { tweetText(context, tweetBody) },
                            color = colors.primary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("𝕏", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Post to 𝕏", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Thread Split Preview if long
        if (threadParts > 1) {
            Text(
                text = "AUTOMATIC THREAD BREAKDOWN ($threadParts TWEETS)",
                color = colors.textMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            val chunks = tweetBody.chunked(270)
            chunks.forEachIndexed { index, chunk ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                    color = colors.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tweet ${index + 1}/$threadParts",
                                color = colors.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${chunk.length}/280",
                                color = colors.textMuted,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = chunk,
                            color = colors.textPrimary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { tweetText(context, "$chunk (${index + 1}/$threadParts)") },
                                color = colors.chipBg
                            ) {
                                Text(
                                    text = "Post Tweet ${index + 1} 𝕏",
                                    color = colors.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
