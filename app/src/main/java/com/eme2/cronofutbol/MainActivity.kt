package com.eme2.cronofutbol // IMPORTANTE: Asegúrate de que esto coincide con la primera línea de tu archivo original. Si tu paquete es distinto, cámbialo aquí.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aquí llamamos a nuestra pantalla principal
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CronoFutbolScreen()
                }
            }
        }
    }
}

@Composable
fun CronoFutbolScreen() {
    // ESTADO: Variables que guardan la información de la app
    var tiempoEnSegundos by remember { mutableLongStateOf(0L) }
    var estaCorriendo by remember { mutableStateOf(false) }
    var minutoInicialInput by remember { mutableStateOf("0") }

    // LÓGICA DEL CRONÓMETRO
    // Este bloque se ejecuta cada vez que 'estaCorriendo' cambia a true
    LaunchedEffect(estaCorriendo) {
        if (estaCorriendo) {
            val tiempoInicio = System.currentTimeMillis() - (tiempoEnSegundos * 1000)
            while (estaCorriendo) {
                // Actualizamos el tiempo actual basándonos en la hora del sistema (más preciso)
                tiempoEnSegundos = (System.currentTimeMillis() - tiempoInicio) / 1000
                delay(100) // Revisamos cada décima de segundo, pero actualizamos solo visualmente
            }
        }
    }

    // CÁLCULO VISUAL
    // Convertimos el texto del input a número (si falla, es 0)
    val minutoBase = minutoInicialInput.toLongOrNull() ?: 0L
    // El tiempo total es: lo que ha contado el crono + lo que el usuario puso de base
    val minutosTotales = (tiempoEnSegundos / 60) + minutoBase
    // Los segundos restantes para mostrar (opcional, si quisieras ver segundos)
    val segundosActuales = tiempoEnSegundos % 60

    // DISEÑO VISUAL (UI)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TÍTULO
        Text(
            text = "CronoFutbol",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // EL RELOJ GIGANTE
        Text(
            // Aquí mostramos solo los minutos como pediste (ej: "61 min")
            // Si quieres ver segundos pequeños, avísame.
            text = "$minutosTotales'",
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "minutos", fontSize = 20.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // CONFIGURACIÓN (Solo visible si está pausado)
        if (!estaCorriendo) {
            OutlinedTextField(
                value = minutoInicialInput,
                onValueChange = { nuevoValor ->
                    // Solo permitimos números
                    if (nuevoValor.all { it.isDigit() }) {
                        minutoInicialInput = nuevoValor
                    }
                },
                label = { Text("Minuto inicial (ej: 45)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BOTONES DE CONTROL
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Botón Iniciar / Pausar
            Button(
                onClick = { estaCorriendo = !estaCorriendo },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estaCorriendo) Color.Red else Color(0xFF4CAF50) // Verde o Rojo
                )
            ) {
                Text(
                    text = if (estaCorriendo) "PAUSAR" else "INICIAR",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Botón Reiniciar (Solo si está pausado y hay tiempo)
            if (!estaCorriendo && tiempoEnSegundos > 0) {
                OutlinedButton(
                    onClick = {
                        tiempoEnSegundos = 0
                    }
                ) {
                    Text("Reiniciar")
                }
            }
        }
    }
}