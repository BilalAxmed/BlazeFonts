package com.example.engine

/**
 * TextDecorator provides artistic borders, frames, brackets, and ornaments
 * for bios, tweets, usernames, and posts.
 */
object TextDecorator {

    data class DecoratorPattern(
        val id: String,
        val name: String,
        val category: String,
        val prefix: String,
        val suffix: String,
        val fontTransformer: (String) -> String = { it }
    ) {
        fun decorate(text: String): String {
            val processed = fontTransformer(text)
            return "$prefix$processed$suffix"
        }
    }

    val DECORATORS: List<DecoratorPattern> = listOf(
        // Royal & Vintage
        DecoratorPattern(
            id = "royal_wings",
            name = "Royal Wings",
            category = "Royal",
            prefix = "꧁༺ ",
            suffix = " ༻꧂",
            fontTransformer = { FontEngine.transform(it, "script_bold") }
        ),
        DecoratorPattern(
            id = "crown_royal",
            name = "Royal Crown",
            category = "Royal",
            prefix = "♛ ",
            suffix = " ♛",
            fontTransformer = { FontEngine.transform(it, "gothic_bold") }
        ),
        DecoratorPattern(
            id = "royal_cross",
            name = "Gothic Crosses",
            category = "Royal",
            prefix = "†•.¸.•·",
            suffix = "·•.¸.•†",
            fontTransformer = { FontEngine.transform(it, "gothic_fraktur") }
        ),
        DecoratorPattern(
            id = "sparkle_stars",
            name = "Celestial Sparkle",
            category = "Aesthetic",
            prefix = "✧･ﾟ: *✧ ",
            suffix = " ✧*:･ﾟ✧",
            fontTransformer = { FontEngine.transform(it, "sans_italic") }
        ),
        DecoratorPattern(
            id = "shooting_star",
            name = "Shooting Star",
            category = "Aesthetic",
            prefix = "彡★ ",
            suffix = " ★彡",
            fontTransformer = { FontEngine.transform(it, "sans_bold") }
        ),
        DecoratorPattern(
            id = "glitter_dots",
            name = "Star Line",
            category = "Aesthetic",
            prefix = "✦•········•✦ ",
            suffix = " ✦•········•✦",
            fontTransformer = { FontEngine.transform(it, "small_caps") }
        ),
        DecoratorPattern(
            id = "angel_wings",
            name = "Angel Hearts",
            category = "Cute",
            prefix = "𓆩♡𓆪 ",
            suffix = " 𓆩♡𓆪",
            fontTransformer = { FontEngine.transform(it, "script_cursive") }
        ),
        DecoratorPattern(
            id = "butterfly_bow",
            name = "Cute Ribbon & Bow",
            category = "Cute",
            prefix = "🎀 𝒲𝒾𝓉𝒽 𝐿𝑜𝓋𝑒 🎀 ",
            suffix = " ♡",
            fontTransformer = { FontEngine.transform(it, "script_cursive") }
        ),
        DecoratorPattern(
            id = "soft_hearts",
            name = "Heart Petals",
            category = "Cute",
            prefix = "ღ(¯`◕‿◕´¯) ♫ ♪ ",
            suffix = " ♪ ♫ (¯`◕‿◕´¯)ღ"
        ),
        DecoratorPattern(
            id = "cyber_blocks",
            name = "Cyber Blocks",
            category = "Gaming",
            prefix = "░▒▓█ ",
            suffix = " █▓▒░",
            fontTransformer = { FontEngine.transform(it, "sans_bold") }
        ),
        DecoratorPattern(
            id = "sound_equalizer",
            name = "Audio Beats",
            category = "Gaming",
            prefix = "ıllıllı ",
            suffix = " ıllıllı",
            fontTransformer = { FontEngine.transform(it, "double_struck") }
        ),
        DecoratorPattern(
            id = "heavy_swords",
            name = "Warrior Swords",
            category = "Gaming",
            prefix = "⚔️ ═╬ ",
            suffix = " ╬═ ⚔️",
            fontTransformer = { FontEngine.transform(it, "gothic_bold") }
        ),
        DecoratorPattern(
            id = "aesthetic_box",
            name = "Aesthetic Box",
            category = "Borders",
            prefix = "╔═════ஓ๑♡๑ஓ═════╗\n  ",
            suffix = "\n╚═════ஓ๑♡๑ஓ═════╝",
            fontTransformer = { FontEngine.transform(it, "sans_bold_italic") }
        ),
        DecoratorPattern(
            id = "sparkle_frame",
            name = "Star Frame",
            category = "Borders",
            prefix = "⋆ ˚｡⋆୨୧˚ ",
            suffix = " ˚୨୧⋆｡˚ ⋆",
            fontTransformer = { FontEngine.transform(it, "script_bold") }
        ),
        DecoratorPattern(
            id = "brackets_japanese",
            name = "Japanese Brackets",
            category = "Borders",
            prefix = "【 ",
            suffix = " 】",
            fontTransformer = { FontEngine.transform(it, "sans_bold") }
        ),
        DecoratorPattern(
            id = "vintage_scroll",
            name = "Vintage Scroll",
            category = "Vintage",
            prefix = "📜 ⊱ ",
            suffix = " ⊰ 📜",
            fontTransformer = { FontEngine.transform(it, "serif_bold_italic") }
        )
    )

    fun decorate(text: String, patternId: String): String {
        val pattern = DECORATORS.find { it.id == patternId } ?: DECORATORS[0]
        return pattern.decorate(text)
    }
}
