package com.scz.cointracker.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scz.cointracker.presentation.util.AnimationUtil

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandShrinkAnimatedContent(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandIn(animationSpec = AnimationUtil.getAnimationSpecIntsize()),
        exit = shrinkOut(animationSpec = AnimationUtil.getAnimationSpecIntsize()),
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FadeInFadeOutAnimatedContent(
    visible: Boolean,
    initialAlpha: Float,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            initialAlpha = initialAlpha,
            animationSpec = AnimationUtil.getAnimationSpecFloat()
        ),
        exit = fadeOut(animationSpec = AnimationUtil.getAnimationSpecFloat()),
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandInFadeOutAnimatedContent(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandIn(animationSpec = AnimationUtil.getAnimationSpecIntsize()),
        exit = fadeOut(animationSpec = AnimationUtil.getAnimationSpecFloat(duration = 100)),
        content = content
    )
}