package com.eme2.cronofutbol

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat

// COLORES PERSONALIZADOS (Tema Neon)
val DarkBackgroundStart = Color(0xFF0F2027)
val DarkBackgroundEnd = Color(0xFF203A43)
val NeonGreen = Color(0xFF00FF87) // Para Iniciar
val NeonRed = Color(0xFFFF0055)   // Para Pausar
val NeonBlue = Color(0xFF60A3BC)  // Para Reiniciar
val TextWhite = Color(0xFFEEEEEE)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }

        setContent {
            // Forzamos el tema oscuro visualmente
            MaterialTheme(colorScheme = darkColorScheme()) {
                CronoFutbolScreen()
            }
        }
    }
}

@Composable
fun CronoFutbolScreen() {
    val context = LocalContext.current
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo

    var minutoInicialInput by remember { mutableStateOf("") } // Vacío por defecto para que se vea el placeholder

    val minutosAmostrar = tiempoSegundos / 60
    val segundosAmostrar = tiempoSegundos % 60
    val textoTiempo = String.format("%02d:%02d", minutosAmostrar, segundosAmostrar)

    // FONDO CON DEGRADADO (Estilo Profesional)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackgroundStart, DarkBackgroundEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // TÍTULO ESTILIZADO
            Text(
                text = "CRONOFUTBOL",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = TextWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            // EL RELOJ CON ANIMACIÓN
            Box(contentAlignment = Alignment.Center) {
                // Anillo pulsante (Solo visible si corre)
                if (estaCorriendo) {
                    PulsingRing()
                }

                // Texto del Tiempo
                Text(
                    text = textoTiempo,
                    style = TextStyle(
                        fontSize = 90.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextWhite,
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = if (estaCorriendo) NeonGreen else Color.Black,
                            blurRadius = 20f
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de estado (Texto pequeño debajo del reloj)
            Text(
                text = if (estaCorriendo) "EN JUEGO" else if (tiempoSegundos > 0) "PAUSADO" else "LISTO",
                color = if (estaCorriendo) NeonGreen else if (tiempoSegundos > 0) NeonRed else Color.Gray,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            // INPUT (Solo visible si está a cero)
            if (!estaCorriendo && tiempoSegundos == 0L) {
                OutlinedTextField(
                    value = minutoInicialInput,
                    onValueChange = { if (it.all { char -> char.isDigit() }) minutoInicialInput = it },
                    label = { Text("Minuto inicio (Opcional)") },
                    placeholder = { Text("Ej: 45") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = NeonGreen,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = NeonGreen,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(200.dp)
                )
            } else {
                // Espacio vacío para que los botones no salten de posición
                Spacer(modifier = Modifier.height(64.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            // BOTONERA
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Principal (Play/Pause)
                BotonCircular(
                    icon = if (estaCorriendo) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    color = if (estaCorriendo) NeonRed else NeonGreen,
                    size = 80.dp,
                    onClick = {
                        val intent = Intent(context, CronoService::class.java)
                        if (estaCorriendo) {
                            intent.action = "PAUSAR"
                        } else {
                            intent.action = "INICIAR"
                            val minutoBase = minutoInicialInput.toLongOrNull() ?: 0L
                            intent.putExtra("MINUTO_INICIO", minutoBase)
                        }
                        context.startService(intent)
                    }
                )

                // Botón Reiniciar (Solo si es necesario)
                if (!estaCorriendo && tiempoSegundos > 0) {
                    BotonCircular(
                        icon = Icons.Filled.Refresh,
                        color = NeonBlue,
                        size = 60.dp, // Un poco más pequeño
                        onClick = {
                            val intent = Intent(context, CronoService::class.java)
                            intent.action = "REINICIAR"
                            context.startService(intent)
                            minutoInicialInput = ""
                        }
                    )
                }
            }
        }
    }
}

// COMPONENTE: Anillo que respira (Animación)
@Composable
fun PulsingRing() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        ), label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(280.dp) // Tamaño del anillo
            .scale(scale)
            .border(width = 2.dp, color = NeonGreen.copy(alpha = alpha), shape = CircleShape)
    )
}

// COMPONENTE: Botón Redondo Personalizado
@Composable
fun BotonCircular(
    icon: ImageVector,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .size(size)
            // Sombra suave estilo neón
            .border(2.dp, color.copy(alpha = 0.5f), CircleShape),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black, // Icono negro para contraste con colores neon
            modifier = Modifier.size(size / 2)
        )
    }
}