package com.scz.cointracker.presentation.util

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntSize

class AnimationUtil {
    companion object {
        fun getAnimationSpecIntsize(
            duration: Int = 500,
            delay: Int = 0
        ): FiniteAnimationSpec<IntSize> {
            return tween(durationMillis = duration, delayMillis = delay)
        }

        fun getAnimationSpecFloat(
            duration: Int = 500,
            delay: Int = 0
        ): FiniteAnimationSpec<Float> {
            return tween(durationMillis = duration, delayMillis = delay)
        }
    }
}