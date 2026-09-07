package com.example.engine

/**
 * NumberFonts provides various artistic representations for numerical digits 0-9
 * including mathematical, superscripts, subscripts, circled, roman, and aesthetic formats.
 */
object NumberFonts {

    data class NumberStyle(
        val id: String,
        val name: String,
        val sample: String,
        val map: List<String>,
        val prefix: String = "",
        val suffix: String = ""
    ) {
        fun convert(input: String): String {
            val sb = StringBuilder()
            sb.append(prefix)
            for (ch in input) {
                if (ch in '0'..'9') {
                    val idx = ch - '0'
                    if (idx < map.size) {
                        sb.append(map[idx])
                    } else {
                        sb.append(ch)
                    }
                } else {
                    sb.append(ch)
                }
            }
            sb.append(suffix)
            return sb.toString()
        }
    }

    val NUMBER_STYLES: List<NumberStyle> = listOf(
        NumberStyle(
            id = "circled_white",
            name = "Circled Numbers",
            sample = "⓪ ① ② ③ ④ ⑤ ⑥ ⑦ ⑧ ⑨",
            map = listOf("⓪", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨")
        ),
        NumberStyle(
            id = "circled_black",
            name = "Black Circled Numbers",
            sample = "⓿ ❶ ❷ ❸ ❹ ❺ ❻ ❼ ❽ ❾",
            map = listOf("⓿", "❶", "❷", "❸", "❹", "❺", "❻", "❼", "❽", "❾")
        ),
        NumberStyle(
            id = "double_struck_num",
            name = "Double-Struck / Outlined",
            sample = "𝟘 𝟙 𝟚 𝟛 𝟜 𝟝 𝟞 𝟟 𝟠 𝟡",
            map = listOf("𝟘", "𝟙", "𝟚", "𝟛", "𝟜", "𝟝", "𝟞", "𝟟", "𝟠", "𝟡")
        ),
        NumberStyle(
            id = "sans_bold_num",
            name = "Sans Bold Numbers",
            sample = "𝟬 𝟭 𝟮 𝟯 𝟰 𝟱 𝟲 𝟳 𝟴 𝟵",
            map = listOf("𝟬", "𝟭", "𝟮", "𝟯", "𝟰", "𝟱", "𝟲", "𝟳", "𝟴", "𝟵")
        ),
        NumberStyle(
            id = "serif_bold_num",
            name = "Serif Bold Numbers",
            sample = "𝟎 𝟏 𝟐 𝟑 𝟒 𝟓 𝟔 𝟕 𝟖 𝟗",
            map = listOf("𝟎", "𝟏", "𝟐", "𝟑", "𝟒", "𝟓", "𝟔", "𝟕", "𝟖", "𝟗")
        ),
        NumberStyle(
            id = "monospace_num",
            name = "Monospace Numbers",
            sample = "𝟶 𝟷 𝟸 𝟹 𝟺 𝟻 𝟼 𝟽 𝟾 𝟿",
            map = listOf("𝟶", "𝟷", "𝟸", "𝟹", "𝟺", "𝟻", "𝟼", "𝟽", "𝟾", "𝟿")
        ),
        NumberStyle(
            id = "superscript_num",
            name = "Superscript Numbers",
            sample = "⁰ ¹ ² ³ ⁴ ⁵ ⁶ ⁷ ⁸ ⁹",
            map = listOf("⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹")
        ),
        NumberStyle(
            id = "subscript_num",
            name = "Subscript Numbers",
            sample = "₀ ₁ ₂ ₃ ₄ ₅ ₆ ₇ ₈ ₉",
            map = listOf("₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉")
        ),
        NumberStyle(
            id = "fullwidth_num",
            name = "Fullwidth Numbers",
            sample = "０ １ ２ ３ ４ ５ ６ ７ ８ ９",
            map = listOf("０", "１", "２", "３", "４", "５", "６", "７", "８", "９")
        ),
        NumberStyle(
            id = "parenthesized_num",
            name = "Parenthesized Numbers",
            sample = "⑴ ⑵ ⑶ ⑷ ⑸ ⑹ ⑺ ⑻ ⑼",
            map = listOf("(0)", "⑴", "⑵", "⑶", "⑷", "⑸", "⑹", "⑺", "⑻", "⑼")
        ),
        NumberStyle(
            id = "dot_num",
            name = "Dot Numbered",
            sample = "⒈ ⒉ ⒊ ⒋ ⒌ ⒍ ⒎ ⒏ ⒐",
            map = listOf("0.", "⒈", "⒉", "⒊", "⒋", "⒌", "⒍", "⒎", "⒏", "⒐")
        ),
        NumberStyle(
            id = "roman_numerals",
            name = "Roman Numerals",
            sample = "Ⅰ Ⅱ Ⅲ Ⅳ Ⅴ Ⅵ Ⅶ Ⅷ Ⅸ Ⅹ",
            map = listOf("O", "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ", "Ⅸ")
        )
    )

    fun convertNumber(numberStr: String, styleId: String): String {
        val style = NUMBER_STYLES.find { it.id == styleId } ?: NUMBER_STYLES[0]
        return style.convert(numberStr)
    }
}
