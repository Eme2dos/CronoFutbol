package com.eme2.cronofutbol.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun CronometroRing(estaCorriendo: Boolean, colorActivo: Color, colorFondo: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin_transition")
    val anguloRotacion by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "spin_animation"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 12.dp.toPx()
        val radius = size.minDimension / 2 - strokeWidth
        val centerOffset = Offset(size.width / 2, size.height / 2)

        // Círculo de fondo
        drawCircle(
            color = colorFondo,
            radius = radius,
            center = centerOffset,
            style = Stroke(width = strokeWidth)
        )

        // Arco giratorio (solo si corre)
        if (estaCorriendo) {
            rotate(degrees = anguloRotacion, pivot = centerOffset) {
                drawArc(
                    color = colorActivo,
                    startAngle = -90f,
                    sweepAngle = 120f,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }
    }
}