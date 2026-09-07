package com.example.engine

/**
 * CuteFonts provides cute, aesthetic, kaomoji and romantic styled text formats.
 */
object CuteFonts {

    data class CuteStyle(
        val id: String,
        val name: String,
        val category: String,
        val sample: String,
        val generator: (String) -> String
    )

    val CUTE_STYLES: List<CuteStyle> = listOf(
        CuteStyle(
            id = "heart_love",
            name = "Heart In Love",
            category = "Hearts",
            sample = "♡ 𝒯𝑒𝓍𝓉 ♡",
            generator = { "♡ " + FontEngine.transform(it, "script_cursive") + " ♡" }
        ),
        CuteStyle(
            id = "ribbon_bow",
            name = "Ribbon Bow",
            category = "Ribbons",
            sample = "🎀 𝒯𝑒𝓍𝓉 🎀",
            generator = { "🎀 " + FontEngine.transform(it, "script_bold") + " 🎀" }
        ),
        CuteStyle(
            id = "aesthetic_sparkles",
            name = "Aesthetic Sparkles",
            category = "Sparkles",
            sample = "⋆｡°✩ 𝒯𝑒𝓍𝓉 ✩°｡⋆",
            generator = { "⋆｡°✩ " + FontEngine.transform(it, "script_cursive") + " ✩°｡⋆" }
        ),
        CuteStyle(
            id = "cute_bear",
            name = "Cute Bear Kaomoji",
            category = "Kaomoji",
            sample = "ʕ•ᴥ•ʔ 𝒯𝑒𝓍𝓉 ʕ•ᴥ•ʔ",
            generator = { "ʕ•ᴥ•ʔ " + FontEngine.transform(it, "small_caps") + " ʕ•ᴥ•ʔ" }
        ),
        CuteStyle(
            id = "happy_flower",
            name = "Blossom Flower",
            category = "Flowers",
            sample = "(✿◠‿◠) 𝒯𝑒𝓍𝓉 🌸",
            generator = { "(✿◠‿◠) " + FontEngine.transform(it, "script_bold") + " 🌸" }
        ),
        CuteStyle(
            id = "angelic_wings",
            name = "Angelic Heart Wings",
            category = "Hearts",
            sample = "𓆩♡𓆪 𝒯𝑒𝓍𝓉 𓆩♡𓆪",
            generator = { "𓆩♡𓆪 " + FontEngine.transform(it, "script_cursive") + " 𓆩♡𓆪" }
        ),
        CuteStyle(
            id = "butterfly_dream",
            name = "Butterfly Dream",
            category = "Nature",
            sample = "🦋 𝒯𝑒𝓍𝓉 🦋",
            generator = { "🦋 " + FontEngine.transform(it, "sans_italic") + " 🦋" }
        ),
        CuteStyle(
            id = "pastel_stars",
            name = "Pastel Night Stars",
            category = "Sparkles",
            sample = "☁️ ੈ✩‧₊˚ 𝒯𝑒𝓍𝓉 ˚₊‧✩ੈ ☁️",
            generator = { "☁️ ੈ✩‧₊˚ " + FontEngine.transform(it, "sans_bold_italic") + " ˚₊‧✩ੈ ☁️" }
        ),
        CuteStyle(
            id = "cute_kitty",
            name = "Cute Kitty",
            category = "Kaomoji",
            sample = "(=^･ω･^=) 𝒯𝑒𝓍𝓉",
            generator = { "(=^･ω･^=) " + FontEngine.transform(it, "small_caps") }
        ),
        CuteStyle(
            id = "sweet_strawberry",
            name = "Sweet Berry",
            category = "Aesthetic",
            sample = "🍓 𝒯𝑒𝓍𝓉 🍓",
            generator = { "🍓 " + FontEngine.transform(it, "script_bold") + " 🍓" }
        ),
        CuteStyle(
            id = "soft_clouds",
            name = "Soft Cloud Aesthetic",
            category = "Aesthetic",
            sample = "☁️ 𝒯𝑒𝓍𝓉 ☁️",
            generator = { "☁️ " + FontEngine.transform(it, "double_struck") + " ☁️" }
        ),
        CuteStyle(
            id = "sparkle_kiss",
            name = "Sweet Kiss Kaomoji",
            category = "Kaomoji",
            sample = "(づ￣ ³￣)づ 𝒯𝑒𝓍𝓉",
            generator = { "(づ￣ ³￣)づ " + FontEngine.transform(it, "script_cursive") }
        )
    )

    fun generate(text: String, styleId: String): String {
        val style = CUTE_STYLES.find { it.id == styleId } ?: CUTE_STYLES[0]
        return style.generator(text)
    }
}
