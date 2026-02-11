package com.eme2.cronofutbol.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Definimos los colores aquí o los importamos si tienes un archivo de tema.
// Para simplificar, los dejaré aquí como constantes privadas por ahora.
val SportBlack = Color(0xFF121212)
val SportGray = Color(0xFF2C2C2C)
val PureWhite = Color(0xFFFFFFFF)
val NeonGreen = Color(0xFF00E676)
val NeonRed = Color(0xFFFF1744)
val NeonBlue = Color(0xFF2979FF)
val NeonCyan = Color(0xFF00E5FF)
val NeonYellow = Color(0xFFFFEA00)
val NeonPurple = Color(0xFFD500F9)
val NeonOrange = Color(0xFFFF9100)
val DeepBlue = Color(0xFF2962FF)
val SkyBlue = Color(0xFF00B0FF)

object ColorManager {
    var chronoColor by mutableStateOf(NeonGreen)
    var btn1Color by mutableStateOf(DeepBlue)
    var btn2Color by mutableStateOf(SkyBlue)
    var btnResetColor by mutableStateOf(NeonRed)

    fun resetColors() {
        chronoColor = NeonGreen
        btn1Color = DeepBlue
        btn2Color = SkyBlue
        btnResetColor = NeonRed
    }
}