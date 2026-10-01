package com.bimiboo.toddlergames.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class ConfettiDot(
    val xRatio: Float,
    val initialY: Float,
    val speed: Float,
    val radius: Float,
    val color: Color
)

@Composable
fun ConfettiView(modifier: Modifier = Modifier) {
    val colors = listOf(
        Color(0xFFEF4444),
        Color(0xFF3B82F6),
        Color(0xFF10B981),
        Color(0xFFF59E0B),
        Color(0xFF8B5CF6),
        Color(0xFFEC4899)
    )

    val dots = remember {
        List(40) {
            ConfettiDot(
                xRatio = Random.nextFloat(),
                initialY = Random.nextFloat() * 1000f,
                speed = Random.nextFloat() * 4f + 3f,
                radius = Random.nextFloat() * 8f + 6f,
                color = colors[Random.nextInt(colors.size)]
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        dots.forEach { dot ->
            val yPos = (dot.initialY + progress * size.height * dot.speed) % size.height
            val xPos = dot.xRatio * size.width
            drawCircle(
                color = dot.color,
                radius = dot.radius,
                center = Offset(xPos, yPos)
            )
        }
    }
}
