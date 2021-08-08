package com.scz.cointracker.presentation.components.animation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean) {
    if (isDisplayed)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
}