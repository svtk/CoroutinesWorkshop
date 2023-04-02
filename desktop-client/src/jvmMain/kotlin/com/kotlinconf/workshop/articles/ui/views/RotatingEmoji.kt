package com.kotlinconf.workshop.articles.ui.views

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource

@Composable
fun RotatingEmoji() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = 360.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Image(
        modifier = Modifier.rotate(rotation),
        painter = painterResource("smile.png"),
        contentDescription =
        null
    )
}