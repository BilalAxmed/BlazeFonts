package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Blaze Logo Composable: Renders a smooth squircle container with the
 * signature lowercase 'b' (vertical stem + tangent circular bowl).
 * Colors dynamically match the active theme or custom parameters.
 */
@Composable
fun BlazeLogoView(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalAppColors.current.logoBgColor,
    bColor: Color = LocalAppColors.current.logoBColor
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cornerRadius = CornerRadius(w * 0.28f, h * 0.28f)

        // Draw squircle background
        drawRoundRect(
            color = backgroundColor,
            cornerRadius = cornerRadius
        )

        val strokePx = w * 0.075f

        // Draw 'b' circular bowl centered at (0.55w, 0.56h) with radius 0.22w
        drawCircle(
            color = bColor,
            radius = w * 0.22f,
            center = Offset(w * 0.55f, h * 0.56f),
            style = Stroke(width = strokePx)
        )

        // Draw 'b' vertical stem from (0.33w, 0.22h) to (0.33w, 0.78h)
        drawLine(
            color = bColor,
            start = Offset(w * 0.33f, h * 0.22f),
            end = Offset(w * 0.33f, h * 0.78f),
            strokeWidth = strokePx,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Copies text to clipboard and triggers subtle haptic feedback and toast.
 */
fun copyTextToClipboard(context: Context, text: String, label: String = "BlazeFont") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard?.setPrimaryClip(clip)

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? android.os.VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(25)
            }
        }
    } catch (_: Exception) {}

    Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
}

/**
 * Shares text via Android intent.
 */
fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Styled Text"))
}

/**
 * Direct Tweet to Twitter/X intent.
 */
fun tweetText(context: Context, text: String) {
    try {
        val tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(text)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
        context.startActivity(intent)
    } catch (e: Exception) {
        shareText(context, text)
    }
}

/**
 * High-polish styled card representing a transformed font with smooth animations.
 */
@Composable
fun FontResultCard(
    fontName: String,
    category: String,
    transformedText: String,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onCardClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val colors = LocalAppColors.current
    var copiedRecently by remember { mutableStateOf(false) }

    LaunchedEffect(copiedRecently) {
        if (copiedRecently) {
            kotlinx.coroutines.delay(1600)
            copiedRecently = false
        }
    }

    val favoriteScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "fav_scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (copiedRecently) colors.primary.copy(alpha = 0.5f) else colors.cardBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable {
                copyTextToClipboard(context, transformedText)
                copiedRecently = true
                onCardClick()
            },
        color = colors.surface,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = if (colors.isDark) 0.dp else 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Category label, Style name & Favorite action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(colors.primary)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = fontName.uppercase(),
                        color = colors.textMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        color = colors.chipBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.chipBorder)
                    ) {
                        Text(
                            text = category,
                            color = colors.textBody,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                        )
                    }
                }

                // Favorite Toggle
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(34.dp)
                        .scale(favoriteScale)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) colors.primary else colors.textMuted.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(11.dp))

            // Main Transformed Text Display
            Text(
                text = transformedText,
                color = colors.textPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row (Tweet, Share, Copy)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tweet Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                        .clickable { tweetText(context, transformedText) },
                    color = colors.chipBg,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = if (colors.isDark) 0.dp else 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("𝕏", color = colors.textBody, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Post", color = colors.textBody, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Share Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                        .clickable { shareText(context, transformedText) },
                    color = colors.chipBg,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = if (colors.isDark) 0.dp else 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = colors.textBody,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Share", color = colors.textBody, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Copy Button with Animated Transition
                AnimatedContent(
                    targetState = copiedRecently,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "copy_anim"
                ) { isCopied ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                if (isCopied) colors.primary else colors.chipBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                copyTextToClipboard(context, transformedText)
                                copiedRecently = true
                            },
                        color = if (isCopied) colors.primary else colors.container,
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = if (colors.isDark) 0.dp else 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = if (isCopied) Color.White else colors.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isCopied) "Copied!" else "Copy",
                                color = if (isCopied) Color.White else colors.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
