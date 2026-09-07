package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.*
import com.example.ui.theme.AppThemePalette
import com.example.ui.theme.getAppColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name and developer name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    val devName = context.getString(R.string.developer_name)
    assertEquals("BlazeFont", appName)
    assertEquals("Bilal Ahmed", devName)
  }

  @Test
  fun `font engine transforms sans bold accurately`() {
    val result = FontEngine.transform("abc 123", "sans_bold")
    assertEquals("𝗮𝗯𝗰 𝟭𝟮𝟯", result)
    assertTrue(result.isNotEmpty())
  }

  @Test
  fun `font engine transforms large 10K character text`() {
    val longInput = "Blaze ".repeat(2000) // 12,000 chars
    val truncated = longInput.take(10000)
    val result = FontEngine.transform(truncated, "sans_bold")
    assertTrue(result.isNotEmpty())
    assertTrue(result.contains("𝗕𝗹𝗮𝘇𝗲"))
  }

  @Test
  fun `all requested theme palettes are defined and valid`() {
    val palettes = listOf(
      AppThemePalette.WHITE_BLUE,
      AppThemePalette.WHITE_ORANGE,
      AppThemePalette.WHITE_GREEN,
      AppThemePalette.BLACK_ORANGE,
      AppThemePalette.BLACK_BLUE
    )
    palettes.forEach { palette ->
      val colors = getAppColors(palette)
      assertNotNull(colors)
      assertNotNull(colors.primary)
      assertNotNull(colors.bg)
    }
  }

  @Test
  fun `number fonts convert circled numbers`() {
    val result = NumberFonts.convertNumber("2026", "circled_white")
    assertEquals("②⓪②⑥", result)
  }

  @Test
  fun `text decorator adds ornaments`() {
    val result = TextDecorator.decorate("Test", "royal_wings")
    assertTrue(result.contains("꧁༺"))
    assertTrue(result.contains("༻꧂"))
  }
}
