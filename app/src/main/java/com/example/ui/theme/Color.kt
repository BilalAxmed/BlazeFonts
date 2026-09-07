package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppThemePalette(
    val title: String,
    val subtitle: String,
    val isDark: Boolean,
    val primaryColor: Color,
    val previewBg: Color
) {
    WHITE_BLUE(
        title = "White & Blue",
        subtitle = "Clean modern royal blue",
        isDark = false,
        primaryColor = Color(0xFF1E88E5),
        previewBg = Color(0xFFF4F8FC)
    ),
    WHITE_BLACK(
        title = "White & Black",
        subtitle = "Pure white with black logo & text",
        isDark = false,
        primaryColor = Color(0xFF111111),
        previewBg = Color(0xFFFFFFFF)
    ),
    WHITE_ORANGE(
        title = "White & Orange",
        subtitle = "Vibrant blaze orange",
        isDark = false,
        primaryColor = Color(0xFFFF5722),
        previewBg = Color(0xFFFFF7F2)
    ),
    WHITE_GREEN(
        title = "White & Green",
        subtitle = "Fresh emerald & mint",
        isDark = false,
        primaryColor = Color(0xFF198754),
        previewBg = Color(0xFFF2FBF6)
    ),
    BLACK_ORANGE(
        title = "Black & Orange",
        subtitle = "Cyber dark neon blaze",
        isDark = true,
        primaryColor = Color(0xFFFF6D00),
        previewBg = Color(0xFF121113)
    ),
    BLACK_BLUE(
        title = "Black & Blue",
        subtitle = "Midnight electric cyan",
        isDark = true,
        primaryColor = Color(0xFF00B4D8),
        previewBg = Color(0xFF0B0E14)
    ),
    WARM_EDITORIAL(
        title = "Warm Editorial",
        subtitle = "Classic polish & mocha",
        isDark = false,
        primaryColor = Color(0xFF6B5C5A),
        previewBg = Color(0xFFFDF8F6)
    )
}

data class AppColors(
    val bg: Color,
    val surface: Color,
    val container: Color,
    val chipBg: Color,
    val chipBorder: Color,
    val cardBorder: Color,
    val borderSubtle: Color,
    val primary: Color,
    val primaryDark: Color,
    val secondary: Color,
    val accent: Color,
    val textPrimary: Color,
    val textBody: Color,
    val textMuted: Color,
    val isDark: Boolean,
    val logoBgColor: Color = primary,
    val logoBColor: Color = Color.White
)

fun getAppColors(palette: AppThemePalette): AppColors {
    return when (palette) {
        AppThemePalette.WHITE_BLACK -> AppColors(
            bg = Color(0xFFFFFFFF),
            surface = Color(0xFFF9FAFB),
            container = Color(0xFFF3F4F6),
            chipBg = Color(0xFFF3F4F6),
            chipBorder = Color(0xFFE5E7EB),
            cardBorder = Color(0xFFE5E7EB),
            borderSubtle = Color(0xFFD1D5DB),
            primary = Color(0xFF111111),
            primaryDark = Color(0xFF000000),
            secondary = Color(0xFF374151),
            accent = Color(0xFF1F2937),
            textPrimary = Color(0xFF000000),
            textBody = Color(0xFF1F2937),
            textMuted = Color(0xFF6B7280),
            isDark = false,
            logoBgColor = Color(0xFF000000),
            logoBColor = Color(0xFFFFFFFF)
        )
        AppThemePalette.WHITE_BLUE -> AppColors(
            bg = Color(0xFFF4F8FC),
            surface = Color(0xFFFFFFFF),
            container = Color(0xFFE8F1FA),
            chipBg = Color(0xFFDFEDFA),
            chipBorder = Color(0xFFC7DEFA),
            cardBorder = Color(0xFFE1ECF7),
            borderSubtle = Color(0xFFD0E3F7),
            primary = Color(0xFF1E88E5),
            primaryDark = Color(0xFF1565C0),
            secondary = Color(0xFF4A90E2),
            accent = Color(0xFF64B5F6),
            textPrimary = Color(0xFF0F172A),
            textBody = Color(0xFF334155),
            textMuted = Color(0xFF64748B),
            isDark = false,
            logoBgColor = Color(0xFF1E88E5),
            logoBColor = Color(0xFFFFFFFF)
        )
        AppThemePalette.WHITE_ORANGE -> AppColors(
            bg = Color(0xFFFFF8F4),
            surface = Color(0xFFFFFFFF),
            container = Color(0xFFFFF0E6),
            chipBg = Color(0xFFFFE5D4),
            chipBorder = Color(0xFFFED2B8),
            cardBorder = Color(0xFFFFECE0),
            borderSubtle = Color(0xFFFDD5BE),
            primary = Color(0xFFFF5722),
            primaryDark = Color(0xFFE64A19),
            secondary = Color(0xFFFF7043),
            accent = Color(0xFFFF8A65),
            textPrimary = Color(0xFF21130D),
            textBody = Color(0xFF4E382E),
            textMuted = Color(0xFF8D6E63),
            isDark = false,
            logoBgColor = Color(0xFFFF5722),
            logoBColor = Color(0xFFFFFFFF)
        )
        AppThemePalette.WHITE_GREEN -> AppColors(
            bg = Color(0xFFF2FBF6),
            surface = Color(0xFFFFFFFF),
            container = Color(0xFFE4F7EC),
            chipBg = Color(0xFFD6F3E2),
            chipBorder = Color(0xFFBCE7CE),
            cardBorder = Color(0xFFE0F5EA),
            borderSubtle = Color(0xFFBFE5D1),
            primary = Color(0xFF198754),
            primaryDark = Color(0xFF146C43),
            secondary = Color(0xFF20C997),
            accent = Color(0xFF48BB78),
            textPrimary = Color(0xFF0D2818),
            textBody = Color(0xFF2D4F38),
            textMuted = Color(0xFF5A7E68),
            isDark = false,
            logoBgColor = Color(0xFF198754),
            logoBColor = Color(0xFFFFFFFF)
        )
        AppThemePalette.BLACK_ORANGE -> AppColors(
            bg = Color(0xFF111013),
            surface = Color(0xFF1A181E),
            container = Color(0xFF242129),
            chipBg = Color(0xFF2D2933),
            chipBorder = Color(0xFF3D3845),
            cardBorder = Color(0xFF2E2A36),
            borderSubtle = Color(0xFF3B3544),
            primary = Color(0xFFFF6D00),
            primaryDark = Color(0xFFE65100),
            secondary = Color(0xFFFF9100),
            accent = Color(0xFFFFAB40),
            textPrimary = Color(0xFFFFF3EC),
            textBody = Color(0xFFD6C8C0),
            textMuted = Color(0xFFA19188),
            isDark = true,
            logoBgColor = Color(0xFFFF6D00),
            logoBColor = Color(0xFFFFFFFF)
        )
        AppThemePalette.BLACK_BLUE -> AppColors(
            bg = Color(0xFF0B0E14),
            surface = Color(0xFF131A24),
            container = Color(0xFF1A2332),
            chipBg = Color(0xFF222E42),
            chipBorder = Color(0xFF2E3D56),
            cardBorder = Color(0xFF1F2B3E),
            borderSubtle = Color(0xFF2A3A52),
            primary = Color(0xFF00B4D8),
            primaryDark = Color(0xFF0096C7),
            secondary = Color(0xFF48CAE4),
            accent = Color(0xFF90E0EF),
            textPrimary = Color(0xFFF0F6FC),
            textBody = Color(0xFFC9D1D9),
            textMuted = Color(0xFF8B949E),
            isDark = true,
            logoBgColor = Color(0xFF00B4D8),
            logoBColor = Color(0xFFFFFFFF)
        )
        AppThemePalette.WARM_EDITORIAL -> AppColors(
            bg = Color(0xFFFDF8F6),
            surface = Color(0xFFFFFFFF),
            container = Color(0xFFF7ECEA),
            chipBg = Color(0xFFEBDEDC),
            chipBorder = Color(0xFFD9CCC9),
            cardBorder = Color(0xFFF0E6E4),
            borderSubtle = Color(0xFFE6D8D5),
            primary = Color(0xFF6B5C5A),
            primaryDark = Color(0xFF524644),
            secondary = Color(0xFF9A8D8B),
            accent = Color(0xFF8C7A77),
            textPrimary = Color(0xFF1D1B1A),
            textBody = Color(0xFF4A4544),
            textMuted = Color(0xFF9A8D8B),
            isDark = false,
            logoBgColor = Color(0xFF6B5C5A),
            logoBColor = Color(0xFFFFFFFF)
        )
    }
}

val LocalAppColors = staticCompositionLocalOf { getAppColors(AppThemePalette.WHITE_BLUE) }

// Compatibility aliases for legacy references
val PolishBg: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.bg
val PolishSurface: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.surface
val PolishContainer: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.container
val PolishChipBg: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.chipBg
val PolishChipBorder: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.chipBorder
val PolishCardBorder: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.cardBorder
val PolishBorderSubtle: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.borderSubtle
val PolishPrimary: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.primary
val PolishPrimaryDark: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.primaryDark
val PolishSecondary: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.secondary
val PolishAccent: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.accent
val PolishTextPrimary: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.textPrimary
val PolishTextBody: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.textBody
val PolishTextMuted: Color @Composable @ReadOnlyComposable get() = LocalAppColors.current.textMuted
val PolishWarmGold: Color get() = Color(0xFFB08968)
val PolishRoseGold: Color get() = Color(0xFFC48B81)
