package com.example.githubapi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubapi.MainViewModel
import com.example.githubapi.ThemeMode

private val DarkColorScheme = darkColorScheme(

    primary = LightPrimary,
    secondary = DarkPrimary,
    tertiary = Pink40

)

private val LightColorScheme = lightColorScheme(
    primary = DarkPrimary,
    secondary = LightPrimary,
    tertiary = Pink80

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    viewModel: MainViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {

    val themeMode by viewModel.themeMode.observeAsState()

    val colorScheme = when {

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && themeMode == ThemeMode.DARK -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme

        themeMode == ThemeMode.SYSTEM_DEFAULT -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

        themeMode == ThemeMode.DARK -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
//            (view.context as Activity).window.statusBarColor = colorScheme.surface.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}