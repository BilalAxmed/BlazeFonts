package com.example.keyboard

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.FontEngine
import com.example.ui.theme.*

/**
 * ProcessTextActivity responds to android.intent.action.PROCESS_TEXT
 * allowing Samsung Keyboard and Android users to transform highlighted text
 * directly from any app (Twitter, Instagram, WhatsApp, Notes).
 */
class ProcessTextActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rawText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString() ?: ""
        } else {
            ""
        }
        val isReadOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        QuickFontSelectorSheet(
                            initialText = rawText,
                            isReadOnly = isReadOnly,
                            onSelectStyle = { transformed ->
                                if (!isReadOnly && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    val returnIntent = Intent().apply {
                                        putExtra(Intent.EXTRA_PROCESS_TEXT, transformed)
                                    }
                                    setResult(Activity.RESULT_OK, returnIntent)
                                } else {
                                    copyToClipboard(transformed)
                                    Toast.makeText(this@ProcessTextActivity, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }
                                finish()
                            },
                            onDismiss = { finish() }
                        )
                    }
                }
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("BlazeFont", text)
        clipboard?.setPrimaryClip(clip)
    }

    @Composable
    private fun QuickFontSelectorSheet(
        initialText: String,
        isReadOnly: Boolean,
        onSelectStyle: (String) -> Unit,
        onDismiss: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .border(1.dp, PolishBorderSubtle, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
            colors = CardDefaults.cardColors(containerColor = PolishBg),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(PolishPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "B",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "BlazeFont Style",
                                color = PolishTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                if (isReadOnly) "Tap style to copy" else "Tap style to replace selection",
                                color = PolishTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = PolishTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input Preview Container
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, PolishBorderSubtle, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = PolishContainer),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (initialText.isEmpty()) "Sample Text" else initialText,
                        modifier = Modifier.padding(12.dp),
                        color = PolishTextPrimary,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Styles List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(FontEngine.ALL_STYLES) { style ->
                        val sampleToUse = if (initialText.isEmpty()) "BlazeFont" else initialText
                        val transformed = style.transformer(sampleToUse)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, PolishCardBorder, RoundedCornerShape(14.dp))
                                .clickable { onSelectStyle(transformed) },
                            colors = CardDefaults.cardColors(containerColor = PolishSurface),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = style.name.uppercase(),
                                        color = PolishTextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.1.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = transformed,
                                        color = PolishTextPrimary,
                                        fontSize = 17.sp
                                    )
                                }
                                Icon(
                                    imageVector = if (isReadOnly) Icons.Default.ContentCopy else Icons.Default.Done,
                                    contentDescription = "Select",
                                    tint = PolishPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
