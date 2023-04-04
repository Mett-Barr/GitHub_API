package com.example.githubapi.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null
) {

    val modifier2 = if (onClick != null) {
        Modifier.clickable(
            indication = rememberRipple(bounded = false, radius = 20.dp),
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    } else Modifier

    Icon(
        painter = painter, contentDescription = contentDescription,
        modifier = modifier
            .then(modifier2)
            .padding(12.dp)
            .size(24.dp),
        tint = tint
    )
}
