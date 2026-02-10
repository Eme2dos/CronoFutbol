package com.eme2.cronofutbol // <--- REVISA TU PAQUETE

import android.Manifest
import android.content.Intent
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedir permiso de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

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
    val context = LocalContext.current

    // Ahora LEEMOS el estado directamente desde el Servicio (Companion Object)
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo

    var minutoInicialInput by remember { mutableStateOf("0") }

    // Cálculos visuales
    val minutosAmostrar = tiempoSegundos / 60
    val segundosAmostrar = tiempoSegundos % 60
    val textoTiempo = String.format("%02d:%02d", minutosAmostrar, segundosAmostrar)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("CronoFutbol", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = textoTiempo,
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (!estaCorriendo && tiempoSegundos == 0L) {
            OutlinedTextField(
                value = minutoInicialInput,
                onValueChange = { if (it.all { char -> char.isDigit() }) minutoInicialInput = it },
                label = { Text("Minuto inicial") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    val intent = Intent(context, CronoService::class.java)
                    if (estaCorriendo) {
                        intent.action = "PAUSAR"
                        context.startService(intent)
                    } else {
                        intent.action = "INICIAR"
                        // Enviamos el minuto inicial solo si estamos empezando
                        val minutoBase = minutoInicialInput.toLongOrNull() ?: 0L
                        intent.putExtra("MINUTO_INICIO", minutoBase)
                        context.startService(intent)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (estaCorriendo) Color.Red else Color(0xFF4CAF50)),
                modifier = Modifier.height(50.dp)
            ) {
                Text(text = if (estaCorriendo) "PAUSAR" else "INICIAR", fontSize = 18.sp)
            }

            if (!estaCorriendo && tiempoSegundos > 0) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, CronoService::class.java)
                        intent.action = "REINICIAR"
                        context.startService(intent)
                    },
                    modifier = Modifier.height(50.dp)
                ) {
                    Text("Reiniciar")
                }
            }
        }
    }
}