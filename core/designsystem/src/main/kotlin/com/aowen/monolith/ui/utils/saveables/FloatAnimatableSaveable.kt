package com.aowen.monolith.ui.utils.saveables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.saveable.Saver

class FloatAnimatableSaveable(private val animatable: Animatable<Float, AnimationVector1D>) {

    companion object {
        val Saver = Saver<Animatable<Float, AnimationVector1D>, Float>(
            save = { it.value },
            restore = { Animatable(it) }
        )
    }

}