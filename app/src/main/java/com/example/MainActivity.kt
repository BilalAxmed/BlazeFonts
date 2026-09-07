package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var activePalette by remember { mutableStateOf(AppThemePalette.WHITE_BLUE) }

            MyApplicationTheme(palette = activePalette) {
                BlazeFontApp(
                    currentPalette = activePalette,
                    onSelectPalette = { activePalette = it }
                )
            }
        }
    }
}

enum class BlazeNavTab(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val badge: String? = null
) {
    FONTS("Styles", Icons.Default.TextFields),
    DECORATOR("Decorator", Icons.Default.AutoAwesome, "HOT"),
    NUMBERS("Numbers", Icons.Default.Pin),
    CUTE("Cute", Icons.Default.Favorite, "CUTE"),
    EMOJIS("Emojis", Icons.Default.SentimentSatisfiedAlt),
    TWEET("Tweet 𝕏", Icons.AutoMirrored.Filled.Send, "10K")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlazeFontApp(
    currentPalette: AppThemePalette = AppThemePalette.WHITE_BLUE,
    onSelectPalette: (AppThemePalette) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(BlazeNavTab.FONTS) }
    var globalText by remember { mutableStateOf("") }
    var tweetDraft by remember { mutableStateOf("") }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showDevInfoDialog by remember { mutableStateOf(false) }
    var showKeyboardSetupDialog by remember { mutableStateOf(false) }

    val colors = LocalAppColors.current
    val developerName = stringResource(id = R.string.developer_name)

    // Palette Theme Selection Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(colors.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Select Theme Palette", color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("Personalize your visual experience", color = colors.textMuted, fontSize = 11.sp)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppThemePalette.values().forEach { palette ->
                        val isSelected = palette == currentPalette
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    2.dp,
                                    if (isSelected) palette.primaryColor else colors.cardBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    onSelectPalette(palette)
                                    showThemeDialog = false
                                },
                            color = palette.previewBg
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val paletteColors = getAppColors(palette)
                                    BlazeLogoView(
                                        modifier = Modifier.size(28.dp),
                                        backgroundColor = paletteColors.logoBgColor,
                                        bColor = paletteColors.logoBColor
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = palette.title,
                                            color = if (palette.isDark) Color.White else Color(0xFF1D1B1A),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = palette.subtitle,
                                            color = if (palette.isDark) Color(0xFFA19188) else Color(0xFF64748B),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = palette.primaryColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Done", color = colors.primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = colors.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Developer & App Info Dialog
    if (showDevInfoDialog) {
        AlertDialog(
            onDismissRequest = { showDevInfoDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BlazeLogoView(
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("BlazeFont Suite", color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Version 2.0 • Pro Edition", color = colors.textMuted, fontSize = 11.sp)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Developer Badge Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(colors.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Lead Architect & Developer", color = colors.textMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text(developerName, color = colors.primary, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }

                    Text(
                        "• 10,000 Character Transformations: Live Unicode styling, Cute bio text, Number fonts, and Ornaments for long threads and bios.",
                        color = colors.textBody,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                    Text(
                        "• Dynamic Color Themes: White & Blue, White & Black, White & Orange, White & Green, and Dark themes with instantaneous switching.",
                        color = colors.textBody,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                    Text(
                        "• Tweet & Thread Composer: Compose long-form content up to 10K words and characters with automatic 280-char tweet splitting and direct posting to 𝕏.",
                        color = colors.textBody,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                    Text(
                        "• Keyboard Integration: Android & Samsung Keyboard IME service with full font switching and emoji tray.",
                        color = colors.textBody,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Keyboard IME Setup Entry
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, colors.chipBorder, RoundedCornerShape(14.dp))
                            .clickable {
                                showDevInfoDialog = false
                                showKeyboardSetupDialog = true
                            },
                        color = colors.chipBg,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Keyboard,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Blaze Keyboard IME Setup",
                                    color = colors.textPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Enable system-wide Unicode fonts typing",
                                    color = colors.textMuted,
                                    fontSize = 10.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = colors.textMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDevInfoDialog = false }) {
                    Text("Close", color = colors.primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = colors.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.bg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.bg)
                    .statusBarsPadding()
            ) {
                // Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showDevInfoDialog = true }
                    ) {
                        // Brand Logo
                        BlazeLogoView(
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BlazeFont",
                                    color = colors.textPrimary,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-0.4).sp
                                )
                                Spacer(modifier = Modifier.width(7.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = colors.primary.copy(alpha = 0.12f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        colors.primary.copy(alpha = 0.3f)
                                    )
                                ) {
                                    Text(
                                        text = "10K words",
                                        color = colors.primary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.3.sp,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Dev: $developerName",
                                color = colors.textMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Theme Switcher Button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                                .clickable { showThemeDialog = true },
                            color = colors.container,
                            shadowElevation = if (colors.isDark) 0.dp else 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(currentPalette.primaryColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Theme",
                                    color = colors.textPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Developer Info Button
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, colors.chipBorder, RoundedCornerShape(12.dp))
                                .clickable { showDevInfoDialog = true },
                            color = colors.container,
                            shadowElevation = if (colors.isDark) 0.dp else 1.dp
                        ) {
                            Box(
                                modifier = Modifier.padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Developer Info",
                                    tint = colors.primary,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }
                }

                // Horizontal Nav Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(BlazeNavTab.values()) { tab ->
                        val isSelected = tab == selectedTab
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) colors.primary else colors.chipBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedTab = tab },
                            color = if (isSelected) colors.primary else colors.chipBg,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) Color.White else colors.textBody,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tab.title,
                                    color = if (isSelected) Color.White else colors.textBody,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                tab.badge?.let { badge ->
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (isSelected) Color.White.copy(alpha = 0.25f)
                                                else colors.primary.copy(alpha = 0.15f)
                                            )
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = badge,
                                            color = if (isSelected) Color.White else colors.primary,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .border(1.dp, colors.borderSubtle, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = colors.container,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val primaryTabs = BlazeNavTab.values()
                    primaryTabs.forEach { tab ->
                        val isSelected = tab == selectedTab
                        val pillScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.05f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.6f),
                            label = "nav_pill_scale"
                        )

                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                                .scale(pillScale),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(46.dp)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) colors.primary else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) Color.White else colors.textMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tab.title.split(" ")[0],
                                color = if (isSelected) colors.primary else colors.textMuted,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                BlazeNavTab.FONTS -> FontGeneratorScreen(
                    inputText = globalText,
                    onInputTextChange = {
                        if (it.length <= 10000) globalText = it
                    },
                    onNavigateToTweetComposer = { draft ->
                        tweetDraft = draft
                        selectedTab = BlazeNavTab.TWEET
                    }
                )
                BlazeNavTab.DECORATOR -> TextDecoratorScreen(
                    inputText = globalText,
                    onInputTextChange = {
                        if (it.length <= 10000) globalText = it
                    }
                )
                BlazeNavTab.NUMBERS -> NumberFontsScreen(
                    inputNumber = globalText,
                    onInputNumberChange = {
                        if (it.length <= 10000) globalText = it
                    }
                )
                BlazeNavTab.CUTE -> CuteFontsScreen(
                    inputText = globalText,
                    onInputTextChange = {
                        if (it.length <= 10000) globalText = it
                    }
                )
                BlazeNavTab.EMOJIS -> EmojiScreen()
                BlazeNavTab.TWEET -> TweetComposerScreen(
                    initialText = if (tweetDraft.isNotEmpty()) tweetDraft else globalText,
                    onBack = { selectedTab = BlazeNavTab.FONTS }
                )
            }
        }

        if (showKeyboardSetupDialog) {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { showKeyboardSetupDialog = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = colors.bg
                ) {
                    KeyboardSetupScreen(onBack = { showKeyboardSetupDialog = false })
                }
            }
        }
    }
}
