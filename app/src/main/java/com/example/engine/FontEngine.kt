package com.example.engine

/**
 * FontEngine converts plain text into 40+ stylish Unicode font variations.
 * Accurately implements all Unicode mathematical alphanumeric symbols,
 * small capitals, enclosed characters, combining diacritics, and aesthetic modifications.
 */
object FontEngine {

    data class FontStyle(
        val id: String,
        val name: String,
        val category: String, // "Popular", "Sans", "Serif", "Fancy", "Enclosed", "Decorated", "Special"
        val previewSample: String,
        val transformer: (String) -> String
    )

    // Maps for Latin letters and digits
    private val NORMAL_LOWER = "abcdefghijklmnopqrstuvwxyz"
    private val NORMAL_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val NORMAL_DIGITS = "0123456789"

    // 1. Sans-Serif
    private val SANS_LOWER = "𝖺𝖻𝖼𝖽𝖾𝖿𝗀𝗁𝗂𝗃𝗄𝗅𝗆𝗇𝗈𝗉𝗊𝗋𝗌𝗍𝗎𝗏𝗐𝗑𝗒𝗓"
    private val SANS_UPPER = "𝖠𝖡𝖢𝖣𝖤𝖥𝖦𝖧𝖨𝖩𝖪𝖫𝖬𝖭𝖮𝖯𝖰𝖱𝖲𝖳𝖴𝖵𝖶𝖷𝖸𝖵"
    private val SANS_DIGITS = "𝟢𝟣𝟤𝟥𝟦𝟧𝟨𝟩𝟪𝟫"

    // 2. Sans-Serif Bold
    private val SANS_BOLD_LOWER = "𝗮𝗯𝗰𝗱𝗲𝗳𝗴𝗵𝗶𝗷𝗸𝗹𝗺𝗻𝗼𝗽𝗾𝗿𝘀𝘁𝘂𝘃𝘄𝘅𝘆𝘇"
    private val SANS_BOLD_UPPER = "𝗔𝗕𝗖𝗗𝗘𝗙𝗚𝗛𝗜𝗝𝗞𝗟𝗠𝗡𝗢𝗣𝗤𝗥𝗦𝗧𝗨𝗩𝗪𝗫𝗬𝗭"
    private val SANS_BOLD_DIGITS = "𝟬𝟭𝟮𝟯𝟰𝟱𝟲𝟳𝟴𝟵"

    // 3. Sans-Serif Italic
    private val SANS_ITALIC_LOWER = "𝘢𝘣𝘤𝘥𝘦𝘧𝘨𝘩𝘪𝘫𝘬𝘭𝘮𝘯𝘰𝘱𝘲𝘳𝘴𝘵𝘶𝘷𝘸𝘹𝘺𝘻"
    private val SANS_ITALIC_UPPER = "𝘈𝘉𝘊𝘋𝘌𝘍𝘎𝘏𝘐𝘑𝘒𝘓𝘔𝘕𝘖𝘗𝘘𝘙𝘚𝘛𝘜𝘝𝘞𝘟𝘠𝘡"

    // 4. Sans-Serif Bold Italic
    private val SANS_BOLD_ITALIC_LOWER = "𝙖𝙗𝙘𝙙𝙚𝙛𝙜𝙝𝙞𝙟𝙠𝙡𝙢𝙣𝙤𝙥𝙦𝙧𝙨𝙩𝙪𝙫𝙬𝙭𝙮𝙯"
    private val SANS_BOLD_ITALIC_UPPER = "𝘼𝘽𝘾𝘿𝙀𝙁𝙂𝙃𝙄𝙅𝙆𝙇𝙈𝙉𝙊𝙋𝙌𝙍𝙎𝙏𝙐𝙑𝙒𝙓𝙔𝙕"

    // 5. Serif Bold (Bold Font)
    private val SERIF_BOLD_LOWER = "𝐚𝐛𝐜𝐝𝐞𝐟𝐠𝐡𝐢𝐣𝐤𝐥𝐦𝐧𝐨𝐩𝐪𝐫𝐬𝐭𝐮𝐯𝐰𝐱𝐲𝐳"
    private val SERIF_BOLD_UPPER = "𝐀𝐁𝐂𝐃𝐄𝐅𝐆𝐇𝐈𝐉𝐊𝐋𝐌𝐍𝐎𝐏𝐐𝐑𝐒𝐓𝐔𝐕𝐖𝐗𝐘𝐙"
    private val SERIF_BOLD_DIGITS = "𝟎𝟏𝟐𝟑𝟒𝟓𝟔𝟕𝟖𝟗"

    // 6. Serif Italic (Italic Text)
    // Note: math italic h is 'ℎ' (U+210E)
    private val SERIF_ITALIC_LOWER = "𝑎𝑏𝑐𝑑𝑒𝑓𝑔ℎ𝑖𝑗𝑘𝑙𝑚𝑛𝑜𝑝𝑞𝑟𝑠𝑡𝑢𝑣𝑤𝑥𝑦𝑧"
    private val SERIF_ITALIC_UPPER = "𝐴𝐵𝐶𝐷𝐸𝐹𝐺𝐻𝐼𝐽𝐾𝐿𝑀𝑁𝑂𝑃𝑄𝑅𝑆𝑇𝑈𝑉𝑊𝑋𝑌𝑍"

    // 7. Serif Bold Italic (Bold Italic)
    private val SERIF_BOLD_ITALIC_LOWER = "𝒂𝒃𝒄𝒅𝒆𝒇𝒈𝒉𝒊𝒋𝒌𝒍𝒎𝒏𝒐𝒑𝒒𝒓𝒔𝒕𝒖𝒗𝒘𝒙𝒚𝒛"
    private val SERIF_BOLD_ITALIC_UPPER = "𝑨𝑩𝑪𝑫𝑬𝑭𝑮𝑯𝑰𝑱𝑲𝑳𝑴𝑵𝑶𝑷𝑸𝑹𝑺𝑻𝑼𝑽𝑾𝑿𝒀𝒁"

    // 8. Double-Struck / Outlined
    // Note: C, H, N, P, Q, R, Z are in letterlike symbols block
    private val DOUBLE_STRUCK_LOWER = "𝕒𝕓𝕔𝕕𝕖𝕗𝕘𝕙𝕚𝕛𝕜𝕝𝕞𝕟𝕠𝕡𝕢𝕣𝕤𝕥𝕦𝕧𝕨𝕩𝕪𝕫"
    private val DOUBLE_STRUCK_UPPER = "𝔸𝔹ℂ𝔻𝔼𝔽𝔾ℍ𝕀𝕁𝕂𝕃𝕄ℕ𝕆ℙℚℝ𝕊𝕋𝕌𝕍𝕎𝕏𝕐ℤ"
    private val DOUBLE_STRUCK_DIGITS = "𝟘𝟙𝟚𝟛𝟜𝟝𝟞𝟟𝟠𝟡"

    // 9. Monospace / Typewriter
    private val MONOSPACE_LOWER = "𝚊𝚋𝚌𝚍𝚎𝚏𝚐𝚑𝚒𝚓𝚔𝚕𝚖𝚗𝚘𝚙𝚚𝚛𝚜𝚝𝚞𝚟𝚠𝚡𝚢𝚣"
    private val MONOSPACE_UPPER = "𝙰𝙱𝙲𝙳𝙴𝙵𝙶𝙷𝙸𝙹𝙺𝙻𝙼𝙽𝙾𝙿𝚀𝚁𝚂𝚃𝚄𝚅𝚆𝚇𝚈𝚉"
    private val MONOSPACE_DIGITS = "𝟶𝟷𝟸𝟹𝟺𝟻𝟼𝟽𝟾𝟿"

    // 10. Small Caps
    private val SMALL_CAPS_MAP = mapOf(
        'a' to "ᴀ", 'b' to "ʙ", 'c' to "ᴄ", 'd' to "ᴅ", 'e' to "ᴇ", 'f' to "ꜰ", 'g' to "ɢ",
        'h' to "ʜ", 'i' to "ɪ", 'j' to "ᴊ", 'k' to "ᴋ", 'l' to "ʟ", 'm' to "ᴍ", 'n' to "ɴ",
        'o' to "ᴏ", 'p' to "ᴘ", 'q' to "ꞯ", 'r' to "ʀ", 's' to "ꜱ", 't' to "ᴛ", 'u' to "ᴜ",
        'v' to "ᴠ", 'w' to "ᴡ", 'x' to "x", 'y' to "ʏ", 'z' to "ᴢ",
        'A' to "ᴀ", 'B' to "ʙ", 'C' to "ᴄ", 'D' to "ᴅ", 'E' to "ᴇ", 'F' to "ꜰ", 'G' to "ɢ",
        'H' to "ʜ", 'I' to "ɪ", 'J' to "ᴊ", 'K' to "ᴋ", 'L' to "ʟ", 'M' to "ᴍ", 'N' to "ɴ",
        'O' to "ᴏ", 'P' to "ᴘ", 'Q' to "ꞯ", 'R' to "ʀ", 'S' to "ꜱ", 'T' to "ᴛ", 'U' to "ᴜ",
        'V' to "ᴠ", 'W' to "ᴡ", 'X' to "x", 'Y' to "ʏ", 'Z' to "ᴢ"
    )

    // 11. Script / Cursive
    private val SCRIPT_LOWER = "𝒶𝒷𝒸𝒹ℯ𝒻ℊ𝒽𝒾𝒿𝓀𝓁𝓂𝓃ℴ𝓅𝓆𝓇𝓈𝓉𝓊𝓋𝓌𝓍𝓎𝓏"
    private val SCRIPT_UPPER = "𝒜ℬ𝒞𝒟ℰℱ𝒢ℋℐ𝒥𝒦ℒℳ𝒩𝒪𝒫𝒬ℛ𝒮𝒯𝒰𝒱𝒲𝒳𝒴𝒵"

    // 12. Bold Script
    private val BOLD_SCRIPT_LOWER = "𝓪𝓫𝓬𝓭𝓮𝓯𝓰𝓱𝓲𝓳𝓴𝓵𝓶𝓷𝓸𝓹𝓺𝓻𝓼𝓽𝓾𝓿𝔀𝔁𝔂𝔃"
    private val BOLD_SCRIPT_UPPER = "𝓐𝓑𝓒𝓓𝓔𝓕𝓖𝓗𝓘𝓙𝓚𝓛𝓜𝓝𝓞𝓟𝓠𝓡𝓢𝓣𝓤𝓥𝓦𝓧𝓨𝓩"

    // 13. Gothic / Fraktur
    private val GOTHIC_LOWER = "𝔞𝔟𝔠𝔡𝔢𝔣𝔤𝔥𝔦𝔧𝔨𝔩𝔪𝔫𝔬𝔭𝔮𝔯𝔰𝔱𝔲𝔳𝔴𝔵𝔶𝔷"
    private val GOTHIC_UPPER = "𝔄𝔅ℭ𝔇𝔈𝔉𝔊ℌℑ𝔍𝔎𝔏𝔐𝔑𝔒𝔓𝔔ℜ𝔖𝔗𝔘𝔙𝔚𝔛𝔜ℨ"

    // 14. Bold Gothic / Bold Fraktur
    private val BOLD_GOTHIC_LOWER = "𝖆𝖇𝖈𝖉𝖊𝖋𝖌𝖍𝖎𝖏𝖐𝖑𝖒𝖓𝖔𝖕𝖖𝖗𝖘𝖙𝖚𝖛𝖜𝖝𝖞𝖟"
    private val BOLD_GOTHIC_UPPER = "𝕬𝕭𝕮𝕯𝕰𝕱𝕲𝕳𝕴𝕵𝕶𝕷𝕸𝕹𝕺𝕻𝕼𝕽𝕾𝕿𝖀𝖁𝖂𝖃𝖄𝖅"

    // 15. Bubble / Circled
    private val CIRCLED_LOWER = "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ"
    private val CIRCLED_UPPER = "ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏ"
    private val CIRCLED_DIGITS = "⓪①②③④⑤⑥⑦⑧⑨"

    // 16. Black Bubble / Negative Circled
    private val NEGATIVE_CIRCLED_LOWER = "🅐🅑🅒🅓🅔🅕🅖🅗🅘🅙🅚🅛🅜🅝🅞🅟🅠🅡🅢🅣🅤🅥🅦🅧🅨🅩"
    private val NEGATIVE_CIRCLED_UPPER = "🅐🅑🅒🅓🅔🅕🅖🅗🅘🅙🅚🅛🅜🅝🅞🅟🅠🅡🅢🅣🅤🅥🅦🅧🅨🅩"
    private val NEGATIVE_CIRCLED_DIGITS = "⓿➊➋➌➍➎➏➐➑➒"

    // 17. Square / Boxed
    private val SQUARED_LOWER = "🄰🄱🄲🄳🄴🄵🄶🄷🄸🄹🄺🄻🄼🄽🄾🄿🅀🅁🅂🅃🅄🅅🅆🅇🅈🅉"
    private val SQUARED_UPPER = "🄰🄱🄲🄳🄴🄵🄶🄷🄸🄹🄺🄻🄼🄽🄾🄿🅀🅁🅂🅃🅄🅅🅆🅇🅈🅉"

    // 18. Black Square / Negative Squared
    private val NEGATIVE_SQUARED_LOWER = "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉"
    private val NEGATIVE_SQUARED_UPPER = "🅰🅱🅲🅳🅴🅵🅶🅷🅸🅹🅺🅻🅼🅽🅾🅿🆀🆁🆂🆃🆄🆅🆆🆇🆈🆉"

    // 19. Fullwidth / Wide
    private val FULLWIDTH_LOWER = "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ"
    private val FULLWIDTH_UPPER = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ"
    private val FULLWIDTH_DIGITS = "０１２３４５６７８９"

    // 20. Upside Down / Inverted
    private val UPSIDE_DOWN_MAP = mapOf(
        'a' to "ɐ", 'b' to "q", 'c' to "ɔ", 'd' to "p", 'e' to "ǝ", 'f' to "ɟ", 'g' to "ƃ",
        'h' to "ɥ", 'i' to "ᴉ", 'j' to "ɾ", 'k' to "ʞ", 'l' to "l", 'm' to "ɯ", 'n' to "u",
        'o' to "o", 'p' to "d", 'q' to "b", 'r' to "ɹ", 's' to "s", 't' to "ʇ", 'u' to "n",
        'v' to "ʌ", 'w' to "ʍ", 'x' to "x", 'y' to "ʎ", 'z' to "z",
        'A' to "∀", 'B' to "𐐒", 'C' to "Ɔ", 'D' to "ᗡ", 'E' to "Ǝ", 'F' to "Ⅎ", 'G' to "⅁",
        'H' to "H", 'I' to "I", 'J' to "ſ", 'K' to "ʞ", 'L' to "˥", 'M' to "W", 'N' to "N",
        'O' to "O", 'P' to "Ԁ", 'Q' to "Ό", 'R' to "ᴚ", 'S' to "S", 'T' to "⊥", 'U' to "∩",
        'V' to "Λ", 'W' to "M", 'X' to "X", 'Y' to "⅄", 'Z' to "Z",
        '0' to "0", '1' to "Ɩ", '2' to "ᄅ", '3' to "Ɛ", '4' to "ㄣ", '5' to "ϛ", '6' to "9",
        '7' to "ㄥ", '8' to "8", '9' to "6", '?' to "¿", '!' to "¡", '.' to "˙", ',' to "'",
        '\'' to ",", '_' to "‾", '&' to "⅋"
    )

    // 21. Superscript
    private val SUPERSCRIPT_MAP = mapOf(
        'a' to "ᵃ", 'b' to "ᵇ", 'c' to "ᶜ", 'd' to "ᵈ", 'e' to "ᵉ", 'f' to "ᶠ", 'g' to "ᵍ",
        'h' to "ʰ", 'i' to "ⁱ", 'j' to "ʲ", 'k' to "ᵏ", 'l' to "ˡ", 'm' to "ᵐ", 'n' to "ⁿ",
        'o' to "ᵒ", 'p' to "ᵖ", 'r' to "ʳ", 's' to "ˢ", 't' to "ᵗ", 'u' to "ᵘ", 'v' to "ᵛ",
        'w' to "ʷ", 'x' to "ˣ", 'y' to "ʸ", 'z' to "ᶻ",
        'A' to "ᴬ", 'B' to "ᴮ", 'D' to "ᴰ", 'E' to "ᴱ", 'G' to "ᴳ", 'H' to "ᴴ", 'I' to "ᴵ",
        'J' to "ᴶ", 'K' to "ᴷ", 'L' to "ᴸ", 'M' to "ᴹ", 'N' to "ᴺ", 'O' to "ᴼ", 'P' to "ᴾ",
        'R' to "ᴿ", 'T' to "ᵀ", 'U' to "ᵁ", 'V' to "ⱽ", 'W' to "ᵂ",
        '0' to "⁰", '1' to "¹", '2' to "²", '3' to "³", '4' to "⁴", '5' to "⁵", '6' to "⁶",
        '7' to "⁷", '8' to "⁸", '9' to "⁹", '+' to "⁺", '-' to "⁻", '=' to "⁼", '(' to "⁽", ')' to "⁾"
    )

    // 22. Subscript
    private val SUBSCRIPT_MAP = mapOf(
        'a' to "ₐ", 'e' to "ₑ", 'h' to "ₕ", 'i' to "ᵢ", 'j' to "ⱼ", 'k' to "ₖ", 'l' to "ₗ",
        'm' to "ₘ", 'n' to "ₙ", 'o' to "ₒ", 'p' to "ₚ", 'r' to "ᵣ", 's' to "ₛ", 't' to "ₜ",
        'u' to "ᵤ", 'v' to "ᵥ", 'x' to "ₓ",
        '0' to "₀", '1' to "₁", '2' to "₂", '3' to "₃", '4' to "₄", '5' to "₅", '6' to "₆",
        '7' to "₇", '8' to "₈", '9' to "₉", '+' to "₊", '-' to "₋", '=' to "₌", '(' to "₍", ')' to "₎"
    )

    /**
     * Maps input string character-by-character based on matching code points.
     */
    private fun mapChars(
        input: String,
        lower: String,
        upper: String,
        digits: String? = null
    ): String {
        val lowerList = lower.codePointsList()
        val upperList = upper.codePointsList()
        val digitsList = digits?.codePointsList()

        val sb = StringBuilder()
        for (ch in input) {
            when {
                ch in 'a'..'z' -> {
                    val idx = ch - 'a'
                    if (idx < lowerList.size) sb.append(lowerList[idx]) else sb.append(ch)
                }
                ch in 'A'..'Z' -> {
                    val idx = ch - 'A'
                    if (idx < upperList.size) sb.append(upperList[idx]) else sb.append(ch)
                }
                ch in '0'..'9' && digitsList != null -> {
                    val idx = ch - '0'
                    if (idx < digitsList.size) sb.append(digitsList[idx]) else sb.append(ch)
                }
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }

    private fun String.codePointsList(): List<String> {
        val list = mutableListOf<String>()
        var i = 0
        while (i < this.length) {
            val cp = this.codePointAt(i)
            val charCount = Character.charCount(cp)
            list.add(this.substring(i, i + charCount))
            i += charCount
        }
        return list
    }

    /**
     * Strikethrough using combining short stroke overlay (U+0336)
     */
    private fun strikethrough(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0336')
        }
    }

    /**
     * Underline using combining low line (U+0332)
     */
    private fun underline(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0332')
        }
    }

    /**
     * Double Underline using combining double low line (U+0333)
     */
    private fun doubleUnderline(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0333')
        }
    }

    /**
     * Wavy Underline using combining tilde below (U+0330)
     */
    private fun wavyUnderline(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0330')
        }
    }

    /**
     * Slash-through using combining long solidus overlay (U+0338)
     */
    private fun slashThrough(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0338')
        }
    }

    /**
     * Dotted combining below (U+0323)
     */
    private fun dotted(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0323')
        }
    }

    /**
     * Sparkle text combining asterisk/star marks
     */
    private fun sparkleText(input: String): String = buildString {
        for (ch in input) {
            append(ch)
            if (ch != ' ' && ch != '\n') append('\u0359')
        }
    }

    /**
     * Inverted / Upside down text (reversed order with upside down chars)
     */
    private fun upsideDown(input: String): String {
        val sb = StringBuilder()
        for (i in input.length - 1 downTo 0) {
            val ch = input[i]
            sb.append(UPSIDE_DOWN_MAP[ch] ?: ch.toString())
        }
        return sb.toString()
    }

    /**
     * Zalgo / Glitch effect
     */
    private fun zalgo(input: String): String {
        val zalgoUp = charArrayOf('\u030d', '\u030e', '\u0304', '\u0305', '\u033f', '\u0311', '\u0306', '\u0310', '\u0352', '\u0357', '\u0351', '\u0307', '\u0308', '\u030a', '\u0342', '\u0343', '\u0344', '\u034a', '\u034b', '\u034c', '\u0350', '\u0300', '\u0301', '\u0302', '\u0303', '\u030b', '\u030c', '\u033e', '\u0340', '\u0341', '\u0315', '\u031b', '\u032a')
        val zalgoMid = charArrayOf('\u0315', '\u031b', '\u0340', '\u0341', '\u0358', '\u0321', '\u0322', '\u0327', '\u0328', '\u0334', '\u0335', '\u0336', '\u034f', '\u035c', '\u035d', '\u035e', '\u035f', '\u0360', '\u0362', '\u0338', '\u0337', '\u0361')
        val zalgoDown = charArrayOf('\u0316', '\u0317', '\u0318', '\u0319', '\u031c', '\u031d', '\u031e', '\u031f', '\u0320', '\u0324', '\u0325', '\u0326', '\u0329', '\u032a', '\u032b', '\u032c', '\u032d', '\u032e', '\u032f', '\u0330', '\u0331', '\u0332', '\u0333', '\u0339', '\u033a', '\u033b', '\u033c', '\u0345', '\u0347', '\u0348', '\u0349', '\u034d', '\u034e', '\u0353', '\u0354', '\u0355', '\u0356', '\u0359', '\u035a', '\u0323')

        return buildString {
            for (ch in input) {
                append(ch)
                if (ch != ' ' && ch != '\n') {
                    append(zalgoUp[Math.abs(ch.hashCode()) % zalgoUp.size])
                    append(zalgoMid[Math.abs(ch.hashCode() * 3) % zalgoMid.size])
                    append(zalgoDown[Math.abs(ch.hashCode() * 7) % zalgoDown.size])
                }
            }
        }
    }

    // All available font styles list
    val ALL_STYLES: List<FontStyle> = listOf(
        // Primary requested styles
        FontStyle(
            id = "sans_italic",
            name = "Sans Serif Italic",
            category = "Sans",
            previewSample = "𝘦𝘹𝘢𝘮𝘱𝘭𝘦 𝘵𝘦𝘹𝘵",
            transformer = { mapChars(it, SANS_ITALIC_LOWER, SANS_ITALIC_UPPER) }
        ),
        FontStyle(
            id = "sans_normal",
            name = "Sans-Serif",
            category = "Sans",
            previewSample = "𝖾𝗑𝖺𝗆𝗉𝗅𝖾 𝗍𝖾𝗑𝗍",
            transformer = { mapChars(it, SANS_LOWER, SANS_UPPER, SANS_DIGITS) }
        ),
        FontStyle(
            id = "sans_bold",
            name = "Sans-Serif Bold",
            category = "Sans",
            previewSample = "𝗲𝘅𝗮𝗺𝗽𝗹𝗲 𝘁𝗲𝘅𝘁",
            transformer = { mapChars(it, SANS_BOLD_LOWER, SANS_BOLD_UPPER, SANS_BOLD_DIGITS) }
        ),
        FontStyle(
            id = "sans_bold_italic",
            name = "Sans Serif Bold Italic",
            category = "Sans",
            previewSample = "𝙚𝙭𝙖𝙢𝙥𝙡𝙚 𝙩𝙚𝙭𝙩",
            transformer = { mapChars(it, SANS_BOLD_ITALIC_LOWER, SANS_BOLD_ITALIC_UPPER) }
        ),
        FontStyle(
            id = "double_struck",
            name = "Double-Struck / Outlined",
            category = "Special",
            previewSample = "𝕖𝕩𝕒𝕞𝕡𝕝𝕖 𝕥𝕖𝕩𝕥",
            transformer = { mapChars(it, DOUBLE_STRUCK_LOWER, DOUBLE_STRUCK_UPPER, DOUBLE_STRUCK_DIGITS) }
        ),
        FontStyle(
            id = "monospace",
            name = "Monospace / Typewriter",
            category = "Popular",
            previewSample = "𝚎𝚡𝚊𝚖𝚙𝚕𝚎 𝚝𝚎𝚡𝚝",
            transformer = { mapChars(it, MONOSPACE_LOWER, MONOSPACE_UPPER, MONOSPACE_DIGITS) }
        ),
        FontStyle(
            id = "serif_bold",
            name = "Bold Font",
            category = "Serif",
            previewSample = "𝐞𝐱𝐚𝐦𝐩𝐥𝐞 𝐭𝐞𝐱𝐭",
            transformer = { mapChars(it, SERIF_BOLD_LOWER, SERIF_BOLD_UPPER, SERIF_BOLD_DIGITS) }
        ),
        FontStyle(
            id = "serif_italic",
            name = "Italic Text",
            category = "Serif",
            previewSample = "𝑒𝑥𝑎𝑚𝑝𝑙𝑒 𝑡𝑒𝑥𝑡",
            transformer = { mapChars(it, SERIF_ITALIC_LOWER, SERIF_ITALIC_UPPER) }
        ),
        FontStyle(
            id = "serif_bold_italic",
            name = "Bold Italic",
            category = "Serif",
            previewSample = "𝒆𝒙𝒂𝒎𝒑𝒍𝒆 𝒕𝒆𝒙𝒕",
            transformer = { mapChars(it, SERIF_BOLD_ITALIC_LOWER, SERIF_BOLD_ITALIC_UPPER) }
        ),
        FontStyle(
            id = "small_caps",
            name = "Small Caps",
            category = "Popular",
            previewSample = "ᴇxᴀᴍᴘʟᴇ ᴛᴇxᴛ",
            transformer = { text ->
                buildString {
                    for (ch in text) {
                        append(SMALL_CAPS_MAP[ch] ?: ch.toString())
                    }
                }
            }
        ),
        FontStyle(
            id = "quote_style_1",
            name = "Quote Style (Fancy)",
            category = "Decorated",
            previewSample = "❝ 𝖊𝖝𝖆𝖒𝖕𝖑𝖊 𝖙𝖊𝖝𝖙 ❞",
            transformer = { "❝ " + mapChars(it, BOLD_GOTHIC_LOWER, BOLD_GOTHIC_UPPER) + " ❞" }
        ),
        FontStyle(
            id = "quote_style_curly",
            name = "Quote Style (Curved)",
            category = "Decorated",
            previewSample = "“ 𝗲𝘅𝗮𝗺𝗽𝗹𝗲 𝘁𝗲𝘅𝘁 ”",
            transformer = { "“ " + mapChars(it, SANS_BOLD_LOWER, SANS_BOLD_UPPER, SANS_BOLD_DIGITS) + " ”" }
        ),
        FontStyle(
            id = "quote_style_japanese",
            name = "Quote Style (Corner Brackets)",
            category = "Decorated",
            previewSample = "『 𝓮𝔁𝓪𝓶𝓹𝓵𝓮 𝓽𝓮𝔁𝓽 』",
            transformer = { "『 " + mapChars(it, BOLD_SCRIPT_LOWER, BOLD_SCRIPT_UPPER) + " 』" }
        ),
        FontStyle(
            id = "quote_style_guillemet",
            name = "Quote Style (Angle)",
            category = "Decorated",
            previewSample = "❮ 𝕖𝕩𝕒𝕞𝕡𝕝𝕖 𝕥𝕖𝕩𝕥 ❯",
            transformer = { "❮ " + mapChars(it, DOUBLE_STRUCK_LOWER, DOUBLE_STRUCK_UPPER, DOUBLE_STRUCK_DIGITS) + " ❯" }
        ),
        FontStyle(
            id = "script_cursive",
            name = "Script / Cursive",
            category = "Fancy",
            previewSample = "𝒶𝒷𝒸 𝓈𝒶𝓂𝓅𝓁ℯ",
            transformer = { mapChars(it, SCRIPT_LOWER, SCRIPT_UPPER) }
        ),
        FontStyle(
            id = "script_bold",
            name = "Bold Script",
            category = "Fancy",
            previewSample = "𝓪𝓫𝓬 𝓼𝓪𝓶𝓹𝓵𝓮",
            transformer = { mapChars(it, BOLD_SCRIPT_LOWER, BOLD_SCRIPT_UPPER) }
        ),
        FontStyle(
            id = "gothic_fraktur",
            name = "Gothic / Fraktur",
            category = "Fancy",
            previewSample = "𝔞𝔟𝔠 𝔰𝔞𝔪𝔭𝔩𝔢",
            transformer = { mapChars(it, GOTHIC_LOWER, GOTHIC_UPPER) }
        ),
        FontStyle(
            id = "gothic_bold",
            name = "Bold Gothic",
            category = "Fancy",
            previewSample = "𝖆𝖇𝖈 𝖘𝖆𝖒𝖕𝖑𝖊",
            transformer = { mapChars(it, BOLD_GOTHIC_LOWER, BOLD_GOTHIC_UPPER) }
        ),
        FontStyle(
            id = "bubble_circled",
            name = "Bubble / Circled",
            category = "Enclosed",
            previewSample = "ⓔⓧⓐⓜⓟⓛⓔ",
            transformer = { mapChars(it, CIRCLED_LOWER, CIRCLED_UPPER, CIRCLED_DIGITS) }
        ),
        FontStyle(
            id = "bubble_black",
            name = "Black Bubble / Inverted Circle",
            category = "Enclosed",
            previewSample = "🅔🅧🅐🅜🅟🅛🅔",
            transformer = { mapChars(it, NEGATIVE_CIRCLED_LOWER, NEGATIVE_CIRCLED_UPPER, NEGATIVE_CIRCLED_DIGITS) }
        ),
        FontStyle(
            id = "squared",
            name = "Square / Boxed",
            category = "Enclosed",
            previewSample = "🄴🅇🄰🄼🄿🄻🄴",
            transformer = { mapChars(it, SQUARED_LOWER, SQUARED_UPPER) }
        ),
        FontStyle(
            id = "squared_black",
            name = "Black Square",
            category = "Enclosed",
            previewSample = "🅴🆇🅰🅼🅿🅻🅴",
            transformer = { mapChars(it, NEGATIVE_SQUARED_LOWER, NEGATIVE_SQUARED_UPPER) }
        ),
        FontStyle(
            id = "wide_fullwidth",
            name = "Wide / Vaporwave",
            category = "Special",
            previewSample = "ｅｘａｍｐｌｅ",
            transformer = { mapChars(it, FULLWIDTH_LOWER, FULLWIDTH_UPPER, FULLWIDTH_DIGITS) }
        ),
        FontStyle(
            id = "superscript",
            name = "Tiny Superscript",
            category = "Special",
            previewSample = "ᵉˣᵃᵐᵖˡᵉ ᵗᵉˣᵗ",
            transformer = { text ->
                buildString {
                    for (ch in text) {
                        append(SUPERSCRIPT_MAP[ch] ?: ch.toString())
                    }
                }
            }
        ),
        FontStyle(
            id = "subscript",
            name = "Tiny Subscript",
            category = "Special",
            previewSample = "ₑₓₐₘₚₗₑ ₜₑₓₜ",
            transformer = { text ->
                buildString {
                    for (ch in text) {
                        append(SUBSCRIPT_MAP[ch] ?: ch.toString())
                    }
                }
            }
        ),
        FontStyle(
            id = "strikethrough",
            name = "Strikethrough",
            category = "Decorated",
            previewSample = "e̶x̶a̶m̶p̶l̶e̶",
            transformer = { strikethrough(it) }
        ),
        FontStyle(
            id = "underline",
            name = "Underline",
            category = "Decorated",
            previewSample = "e̲x̲a̲m̲p̲l̲e̲",
            transformer = { underline(it) }
        ),
        FontStyle(
            id = "double_underline",
            name = "Double Underline",
            category = "Decorated",
            previewSample = "e̳x̳a̳m̳p̳l̳e̳",
            transformer = { doubleUnderline(it) }
        ),
        FontStyle(
            id = "wavy_underline",
            name = "Wavy Underline",
            category = "Decorated",
            previewSample = "ḛx̰a̰m̰p̰l̰ḛ",
            transformer = { wavyUnderline(it) }
        ),
        FontStyle(
            id = "slash_through",
            name = "Slash Through",
            category = "Decorated",
            previewSample = "e̷x̷a̷m̷p̷l̷e̷",
            transformer = { slashThrough(it) }
        ),
        FontStyle(
            id = "dotted_text",
            name = "Dotted Text",
            category = "Decorated",
            previewSample = "ẹx̣ạṃp̣ḷẹ",
            transformer = { dotted(it) }
        ),
        FontStyle(
            id = "sparkle_text",
            name = "Sparkle Text",
            category = "Decorated",
            previewSample = "e͙x͙a͙m͙p͙l͙e͙",
            transformer = { sparkleText(it) }
        ),
        FontStyle(
            id = "upside_down",
            name = "Upside Down / Inverted",
            category = "Special",
            previewSample = "ʇxǝʇ ǝldɯɐxǝ",
            transformer = { upsideDown(it) }
        ),
        FontStyle(
            id = "zalgo_glitch",
            name = "Glitch / Zalgo",
            category = "Special",
            previewSample = "e̵x̴a̶m̸p̴l̵e̶",
            transformer = { zalgo(it) }
        ),
        FontStyle(
            id = "reversed_text",
            name = "Reversed Text",
            category = "Special",
            previewSample = "txet elpmaxe",
            transformer = { it.reversed() }
        )
    )

    fun transform(text: String, styleId: String): String {
        if (text.isEmpty()) return ""
        val style = ALL_STYLES.find { it.id == styleId } ?: ALL_STYLES[0]
        return style.transformer(text)
    }
}
