package com.example.cronofutbol // IMPORTANTE: Revisa que tu paquete sea correcto

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
    // ESTADO
    var tiempoTranscurridoSegundos by remember { mutableLongStateOf(0L) }
    var estaCorriendo by remember { mutableStateOf(false) }
    var minutoInicialInput by remember { mutableStateOf("0") }

    // LÓGICA DEL CRONÓMETRO (Se ejecuta cada 100ms para precisión)
    LaunchedEffect(estaCorriendo) {
        if (estaCorriendo) {
            val tiempoInicio = System.currentTimeMillis() - (tiempoTranscurridoSegundos * 1000)
            while (estaCorriendo) {
                tiempoTranscurridoSegundos = (System.currentTimeMillis() - tiempoInicio) / 1000
                delay(100)
            }
        }
    }

    // CÁLCULOS VISUALES
    val minutoBase = minutoInicialInput.toLongOrNull() ?: 0L

    // El tiempo total a mostrar suma lo que ha corrido el crono + el input del usuario (convertido a segundos)
    val totalSegundosReales = tiempoTranscurridoSegundos + (minutoBase * 60)

    val minutosAmostrar = totalSegundosReales / 60
    val segundosAmostrar = totalSegundosReales % 60

    // Formateamos para que siempre tenga dos dígitos (ej: 05 en vez de 5)
    val textoTiempo = String.format("%02d:%02d", minutosAmostrar, segundosAmostrar)

    // DISEÑO VISUAL (UI)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CronoFutbol",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // EL RELOJ GIGANTE (Ahora MM:SS)
        Text(
            text = textoTiempo,
            fontSize = 80.sp, // Un poco más pequeño para que quepan los segundos
            fontWeight = FontWeight.Bold,
            // Usamos una fuente monoespaciada para que los números no bailen al cambiar
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(32.dp))

        // CONFIGURACIÓN
        if (!estaCorriendo) {
            OutlinedTextField(
                value = minutoInicialInput,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.all { it.isDigit() }) {
                        minutoInicialInput = nuevoValor
                    }
                },
                label = { Text("Minuto inicial (ej: 45)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Text(
                text = "Ingresa el minuto desde donde quieres empezar",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BOTONES
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { estaCorriendo = !estaCorriendo },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estaCorriendo) Color.Red else Color(0xFF4CAF50)
                ),
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = if (estaCorriendo) "PAUSAR" else "INICIAR",
                    fontSize = 18.sp
                )
            }

            if (!estaCorriendo && tiempoTranscurridoSegundos > 0) {
                OutlinedButton(
                    onClick = { tiempoTranscurridoSegundos = 0 },
                    modifier = Modifier.height(50.dp)
                ) {
                    Text("Reiniciar")
                }
            }
        }
    }
}