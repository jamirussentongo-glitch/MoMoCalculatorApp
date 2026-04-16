package com.ndejje.momocal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.ndejje.momocal.BrandGold
import com.ndejje.momocal.DarkSurface
import com.ndejje.momocal.ErrorRed
import com.ndejje.momocal.LightGrey
import com.ndejje.momocal.MoMoShapes
import com.ndejje.momocal.MoMoTypography
import com.ndejje.momocal.NavyBlue
import com.ndejje.momocal.NavyBlueDark
import com.ndejje.momocal.OnErrorWhite
import com.ndejje.momocal.White

private val LightColorScheme = lightColorScheme(
    primary         = NavyBlue,
    onPrimary       = White,
    secondary       = BrandGold,
    onSecondary     = NavyBlueDark,
    background      = LightGrey,
    onBackground    = DarkSurface,
    surface         = White,
    onSurface       = DarkSurface,
    error           = ErrorRed,
    onError         = OnErrorWhite
)

@Composable
fun MoMoAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = MoMoTypography,
        shapes      = MoMoShapes,
        content     = content
    )
}
