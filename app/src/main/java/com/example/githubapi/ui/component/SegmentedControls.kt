package com.example.githubapi.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.githubapi.R
import com.example.githubapi.ui.theme.DarkPrimary
import com.example.githubapi.ui.theme.LightPrimary
import com.example.githubapi.ui.theme.MainTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Preview
@Composable
fun SegmentedControls() {

    var horizontalBias by remember { mutableStateOf(-1f) }
    val alignment by animateHorizontalAlignmentAsState(horizontalBias)

    Box (modifier = Modifier
        .clickable { horizontalBias *= -1 }
        .wrapContentSize()
        .height(intrinsicSize = IntrinsicSize.Min)) {
        Spacer(modifier = Modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.primary)
            .align(alignment))
        Row {
            ClickableIcon(painter = painterResource(id = R.drawable.history_black_24dp))
            ClickableIcon(painter = painterResource(id = R.drawable.tune_black_24dp))
//            Icon(painter = painterResource(id = R.drawable.card_view), contentDescription = "card view")
//            Icon(painter = painterResource(id = R.drawable.article), contentDescription = "article")
        }
    }
}

@Composable
fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<Alignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return remember { derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) } }
}

//@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnScope.Swiper(
    page: () -> Int,
    pageChange: (Int) -> Unit
) {


    val coroutineScope = rememberCoroutineScope()

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { 48.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

//    val readMode by remember { derivedStateOf { swipeableState.currentValue } }
//    val animateModifier by remember { derivedStateOf { if (readMode == 0) Modifier else Modifier.animateContentSize() } }
//    val alphaAnimation by animateFloatAsState(targetValue = if (readMode == 0) 2f else 0f)
//    val cardViewAlpha by remember { derivedStateOf { if (alphaAnimation < 1f) 0f else alphaAnimation - 1f } }
//    val cardViewSizeModifier by remember { derivedStateOf { if (alphaAnimation < 1f) Modifier.size(1.dp) else Modifier.wrapContentHeight() } }
//    val articleViewAlpha by remember { derivedStateOf { if (alphaAnimation > 1f) 0f else 1f - alphaAnimation } }
//    val articleViewSizeModifier by remember {
//        derivedStateOf {
//            if (alphaAnimation > 1f) Modifier.size(
//                1.dp
//            ) else Modifier.wrapContentHeight()
//        }
//    }

    // page state
    LaunchedEffect(page()) {
        if (swipeableState.targetValue != page()) {
            swipeableState.animateTo(page())
        }
    }
    LaunchedEffect(swipeableState.targetValue) {
        if (swipeableState.targetValue != page()) {
            pageChange(swipeableState.targetValue)
        }
    }
//    LaunchedEffect(swipeableState.currentValue) {
//        Log.d("swipe", swipeableState.currentValue.toString())
//    }




    /** SegmentedControls */
    val contentColor =
        MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface)

    val colorTint = if (isSystemInDarkTheme()) DarkPrimary else LightPrimary
//    val gray = if (!isSystemInDarkTheme()) Color.Gray else contentColor
    fun gestureColor(state: Float): Color {
        val rangeState = if (state > 1f) 1f else if (state < 0) 0f else state
        return Color(
            red = colorTint.red * rangeState + (1 - rangeState) * contentColor.red,
            green = colorTint.green * rangeState + (1 - rangeState) * contentColor.green,
            blue = colorTint.blue * rangeState + (1 - rangeState) * contentColor.blue
        )
    }

    val historySearchTint by remember { derivedStateOf { gestureColor(1 - swipeableState.offset.value / sizePx) } }
    val tuneTine by remember { derivedStateOf { gestureColor(swipeableState.offset.value / sizePx) } }

    val color = if (!isSystemInDarkTheme()) {
        LocalElevationOverlay.current!!.apply(
            MaterialTheme.colorScheme.surface,
            LocalAbsoluteElevation.current + 50.dp
        )
    } else {
        MaterialTheme.colorScheme.onSurface.copy(0.08f)
    }



    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(8.dp)
            .clip(CircleShape)
            .background(color)
            .wrapContentSize()
            .height(intrinsicSize = IntrinsicSize.Min)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {

        fun swipeAnimate(position: Int) {
            coroutineScope.launch { swipeableState.animateTo(position) }
        }
        Row {
            Spacer(modifier = Modifier
                .size(48.dp)
                .clickable(
                ) { swipeAnimate(0) })
            Spacer(modifier = Modifier
                .size(48.dp)
                .clickable(
                    indication = rememberRipple(radius = 48.dp),
                    interactionSource = remember { MutableInteractionSource() }
                ) { swipeAnimate(1) })
        }

        // picker
        if (isSystemInDarkTheme()) {
            Spacer(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            swipeableState.offset.value.roundToInt(),
                            0
                        )
                    }
                    .padding(6.dp)
                    .clip(CircleShape)
                    .size(36.dp)
                    .background(
                        if (!isSystemInDarkTheme()) Color(
                            0xFF888888
                        ) else Color.White
                    )
            )
        } else {
            Surface(
                elevation = 4.dp,
                shape = CircleShape,
                color = Color(0xFF777777),
                modifier = Modifier
                    .offset {
                        IntOffset(
                            swipeableState.offset.value.roundToInt(),
                            0
                        )
                    }
                    .padding(6.dp)
                    .size(36.dp)
            ) {}
        }
        Row {
            ClickableIcon(
                painter = painterResource(id = R.drawable.history_black_24dp),
                tint = historySearchTint,
            )
            ClickableIcon(
                painter = painterResource(id = R.drawable.tune_black_24dp),
                tint = tuneTine,
            )
        }
    }
}

@Preview
@Composable
fun SwipeTest() {
    MainTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Swiper()
//            Spacer(modifier = Modifier.size(80.dp).background())
        }
    }
}