package com.example.keyboard

import android.content.ClipboardManager
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.engine.EmojiData
import com.example.engine.FontEngine
import com.example.ui.theme.*

/**
 * BlazeFontImeService allows users to select BlazeFont from the Samsung Keyboard
 * or Android Input Method Switcher, and type fancy fonts directly in any app.
 */
class BlazeFontImeService : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private var currentStyleId by mutableStateOf("sans_bold")
    private var isCaps by mutableStateOf(false)
    private var isSymbolsMode by mutableStateOf(false)
    private var isEmojiMode by mutableStateOf(false)

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@BlazeFontImeService)
            setViewTreeSavedStateRegistryOwner(this@BlazeFontImeService)
            setContent {
                MyApplicationTheme {
                    KeyboardMainView()
                }
            }
        }
        return composeView
    }

    private fun vibrate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? android.os.VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(15)
                }
            }
        } catch (_: Exception) {}
    }

    private fun sendKey(rawChar: String) {
        vibrate()
        val ic = currentInputConnection ?: return
        val transformed = FontEngine.transform(rawChar, currentStyleId)
        ic.commitText(transformed, 1)
    }

    private fun sendRawText(text: String) {
        vibrate()
        val ic = currentInputConnection ?: return
        ic.commitText(text, 1)
    }

    private fun handleDelete() {
        vibrate()
        val ic = currentInputConnection ?: return
        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
    }

    private fun handleEnter() {
        vibrate()
        val ic = currentInputConnection ?: return
        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
        ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
    }

    private fun handlePasteAndConvert() {
        vibrate()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = clipboard?.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text?.toString() ?: ""
            if (text.isNotEmpty()) {
                val converted = FontEngine.transform(text, currentStyleId)
                currentInputConnection?.commitText(converted, 1)
            }
        }
    }

    private fun openKeyboardPicker() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showInputMethodPicker()
    }

    @Composable
    private fun KeyboardMainView() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PolishBg)
                .border(1.dp, PolishBorderSubtle)
                .padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            // Top Toolbar with Active Font Pill & Quick Switchers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Box
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PolishPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("B", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Font Selector Horizontal List
                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(FontEngine.ALL_STYLES) { style ->
                        val isSelected = style.id == currentStyleId
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) PolishPrimary else PolishChipBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    vibrate()
                                    currentStyleId = style.id
                                },
                            color = if (isSelected) PolishPrimary else PolishChipBg,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = style.previewSample.take(10),
                                color = if (isSelected) Color.White else PolishTextBody,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Switch to Samsung Keyboard Button
                IconButton(
                    onClick = { openKeyboardPicker() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Switch Keyboard",
                        tint = PolishPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (isEmojiMode) {
                // Emoji Panel in Keyboard
                EmojiKeyboardPanel()
            } else if (isSymbolsMode) {
                // Symbols Layout
                SymbolsKeyboardPanel()
            } else {
                // Normal QWERTY Layout
                QwertyKeyboardPanel()
            }
        }
    }

    @Composable
    private fun QwertyKeyboardPanel() {
        val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
        val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
        val row3 = listOf("z", "x", "c", "v", "b", "n", "m")

        Column(modifier = Modifier.fillMaxWidth()) {
            // Row 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row1.forEach { char ->
                    val displayChar = if (isCaps) char.uppercase() else char
                    KeyButton(
                        text = FontEngine.transform(displayChar, currentStyleId),
                        rawText = displayChar,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row2.forEach { char ->
                    val displayChar = if (isCaps) char.uppercase() else char
                    KeyButton(
                        text = FontEngine.transform(displayChar, currentStyleId),
                        rawText = displayChar,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shift Key
                Surface(
                    modifier = Modifier
                        .weight(1.4f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            vibrate()
                            isCaps = !isCaps
                        },
                    color = if (isCaps) PolishPrimary else PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.North,
                            contentDescription = "Shift",
                            tint = if (isCaps) Color.White else PolishTextBody,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                row3.forEach { char ->
                    val displayChar = if (isCaps) char.uppercase() else char
                    KeyButton(
                        text = FontEngine.transform(displayChar, currentStyleId),
                        rawText = displayChar,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Backspace Key
                Surface(
                    modifier = Modifier
                        .weight(1.4f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable { handleDelete() },
                    color = PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = PolishTextBody,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Bottom Control Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 123 / Symbol Switch
                Surface(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            vibrate()
                            isSymbolsMode = true
                        },
                    color = PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("?123", color = PolishTextBody, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Emoji Button
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            vibrate()
                            isEmojiMode = true
                        },
                    color = PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("😀", fontSize = 16.sp)
                    }
                }

                // Paste & Convert
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable { handlePasteAndConvert() },
                    color = PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Paste & Convert",
                            tint = PolishPrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                // Space Bar
                Surface(
                    modifier = Modifier
                        .weight(3.5f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishCardBorder, RoundedCornerShape(8.dp))
                        .clickable { sendRawText(" ") },
                    color = PolishSurface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("BlazeFont Space", color = PolishTextMuted, fontSize = 12.sp)
                    }
                }

                // Dot Key
                KeyButton(
                    text = ".",
                    rawText = ".",
                    modifier = Modifier.weight(0.9f)
                )

                // Enter Key
                Surface(
                    modifier = Modifier
                        .weight(1.4f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { handleEnter() },
                    color = PolishPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enter",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SymbolsKeyboardPanel() {
        val symRow1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        val symRow2 = listOf("@", "#", "$", "_", "&", "-", "+", "(", ")", "/")
        val symRow3 = listOf("*", "\"", "'", ":", ";", "!", "?", "%", "=")

        Column(modifier = Modifier.fillMaxWidth()) {
            // Row 1: Numbers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                symRow1.forEach { char ->
                    KeyButton(
                        text = FontEngine.transform(char, currentStyleId),
                        rawText = char,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                symRow2.forEach { char ->
                    KeyButton(text = char, rawText = char, modifier = Modifier.weight(1f))
                }
            }

            // Row 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                symRow3.forEach { char ->
                    KeyButton(text = char, rawText = char, modifier = Modifier.weight(1f))
                }
                // Backspace
                Surface(
                    modifier = Modifier
                        .weight(1.4f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable { handleDelete() },
                    color = PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = PolishTextBody,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Bottom Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ABC Switch
                Surface(
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            vibrate()
                            isSymbolsMode = false
                        },
                    color = PolishPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("ABC", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Space Bar
                Surface(
                    modifier = Modifier
                        .weight(4.5f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishCardBorder, RoundedCornerShape(8.dp))
                        .clickable { sendRawText(" ") },
                    color = PolishSurface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Space", color = PolishTextMuted, fontSize = 12.sp)
                    }
                }

                // Enter
                Surface(
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .padding(horizontal = 2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { handleEnter() },
                    color = PolishPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enter",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun EmojiKeyboardPanel() {
        var selectedCategoryIndex by remember { mutableStateOf(0) }
        val currentCategory = EmojiData.CATEGORIES[selectedCategoryIndex]

        Column(modifier = Modifier.fillMaxWidth().height(220.dp)) {
            // Category Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(EmojiData.CATEGORIES.indices.toList()) { idx ->
                    val cat = EmojiData.CATEGORIES[idx]
                    val isCatSelected = idx == selectedCategoryIndex
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                if (isCatSelected) PolishPrimary else PolishChipBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                vibrate()
                                selectedCategoryIndex = idx
                            },
                        color = if (isCatSelected) PolishPrimary else PolishChipBg,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(cat.icon, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }

            // Emoji Grid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                val scrollState = rememberScrollState()
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    currentCategory.emojis.chunked(7).forEach { rowEmojis ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowEmojis.forEach { emoji ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { sendRawText(emoji) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Back to ABC Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            vibrate()
                            isEmojiMode = false
                        },
                    color = PolishPrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text("ABC", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishChipBorder, RoundedCornerShape(8.dp))
                        .clickable { handleDelete() },
                    color = PolishChipBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = PolishTextBody,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun KeyButton(
        text: String,
        rawText: String,
        modifier: Modifier = Modifier
    ) {
        Surface(
            modifier = modifier
                .height(44.dp)
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, PolishCardBorder, RoundedCornerShape(8.dp))
                .clickable { sendKey(rawText) },
            color = PolishSurface,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 0.5.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = text,
                    color = PolishTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
