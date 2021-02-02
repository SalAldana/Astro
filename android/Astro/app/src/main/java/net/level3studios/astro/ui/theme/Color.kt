package net.level3studios.astro.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val purple200 = Color(0xFFBB86FC)
val purple500 = Color(0xFF6200EE)
val purple700 = Color(0xFF3700B3)
val teal200 = Color(0xFF03DAC5)

//region Day Colors
val fuzzyBrown200 = Color(0xFFC35F53)
val fuzzyBrown500 = Color(0xFF8E312A)
val pickledBluewood500 = Color(0xFF353B5C)
private val dayGradientStart = Color(0xFF8694B7)
private val dayGradientEnd = Color(0xFFE2EAF4)
val dayGradient = Brush.linearGradient(colors = listOf(dayGradientStart, dayGradientEnd))
//endregion

//region Peak Colors
val bouquet200 = Color(0xFFAC6F9F)
val bouquet500 = Color(0xFF7C4270)
val martinique500 = Color(0xFF313151)
private val peakGradientStart = Color(0xFF8360A8)
private val peakGradientEnd = Color(0xFFFFA79F)
val peakGradient = Brush.linearGradient(listOf(peakGradientStart, peakGradientEnd))
//endregion

//region Night Colors
val portGore200 = Color(0xFF6167B6)
val portGore500 = Color(0xFF2F3D86)
val waterloo200 = Color(0xFF7E849C)
private val nightGradientStart = Color(0xFF13103B)
private val nightGradientEnd = Color(0xFFD6737A)
val nightGradient = Brush.linearGradient(listOf(nightGradientStart, nightGradientEnd))
//endregion