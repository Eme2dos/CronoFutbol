package com.eme2.cronofutbol.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.*
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import com.eme2.cronofutbol.data.WearColors
import com.eme2.cronofutbol.service.CronoService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedir permiso de notificación al iniciar (simple para Wear)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {}.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            WearApp(this)
        }
    }
}

@Composable
fun WearApp(context: Context) {
    val listState = rememberScalingLazyListState()
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo

    val m = tiempoSegundos / 60
    val s = tiempoSegundos % 60
    val textoTiempo = String.format("%02d:%02d", m, s)

    MaterialTheme(
        colors = Colors(
            primary = WearColors.NeonGreen,
            background = Color.Black,
            surface = Color.DarkGray
        )
    ) {
        Scaffold(
            timeText = { TimeText() }, // Muestra la hora real arriba
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) } // Efecto borde oscuro
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize().background(Color.Black),
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // TÍTULO
                item {
                    Text(
                        "CRONOFUTBOL",
                        color = WearColors.NeonGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // TIEMPO GRANDE
                item {
                    Text(
                        textoTiempo,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // BOTÓN 1er TIEMPO
                item {
                    Chip(
                        onClick = {
                            val intent = Intent(context, CronoService::class.java)
                            if (estaCorriendo && CronoService.etapaActual == 1) intent.action = "PAUSAR"
                            else { intent.action = "INICIAR"; intent.putExtra("MINUTO_INICIO", 0L); intent.putExtra("ETAPA", 1) }
                            startServiceCompat(context, intent)
                        },
                        label = { Text("1º TIEMPO", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                        colors = ChipDefaults.primaryChipColors(backgroundColor = if(estaCorriendo && CronoService.etapaActual == 1) Color.DarkGray else WearColors.DeepBlue),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }

                // BOTÓN 2do TIEMPO
                item {
                    Chip(
                        onClick = {
                            val intent = Intent(context, CronoService::class.java)
                            if (estaCorriendo && CronoService.etapaActual == 2) intent.action = "PAUSAR"
                            else { intent.action = "INICIAR"; intent.putExtra("MINUTO_INICIO", 45L); intent.putExtra("ETAPA", 2) }
                            startServiceCompat(context, intent)
                        },
                        label = { Text("2º TIEMPO", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                        colors = ChipDefaults.primaryChipColors(backgroundColor = if(estaCorriendo && CronoService.etapaActual == 2) Color.DarkGray else WearColors.SkyBlue),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }

                // BOTÓN REINICIAR (Más pequeño)
                item {
                    CompactChip(
                        onClick = {
                            val intent = Intent(context, CronoService::class.java)
                            intent.action = "REINICIAR"
                            startServiceCompat(context, intent)
                        },
                        label = { Text("RESET", color = Color.White, fontWeight = FontWeight.Bold) },
                        colors = ChipDefaults.secondaryChipColors(backgroundColor = WearColors.NeonRed),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

fun startServiceCompat(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ContextCompat.startForegroundService(context, intent)
    } else {
        context.startService(intent)
    }
}