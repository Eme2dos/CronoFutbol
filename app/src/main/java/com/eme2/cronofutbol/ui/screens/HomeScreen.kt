package com.eme2.cronofutbol.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.R
import com.eme2.cronofutbol.data.*
import com.eme2.cronofutbol.service.CronoService
import com.eme2.cronofutbol.ui.components.CronometroRing
import com.eme2.cronofutbol.utils.startCronoService
import androidx.compose.foundation.shape.CircleShape

@Composable
fun HomeScreen(onMenuClick: () -> Unit) {
    val context = LocalContext.current
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo
    val etapaActual = CronoService.etapaActual

    val minutosAmostrar = tiempoSegundos / 60
    val segundosAmostrar = tiempoSegundos % 60
    val textoTiempo = String.format("%02d:%02d", minutosAmostrar, segundosAmostrar)

    var showSaveDialog by remember { mutableStateOf(false) }
    var nombrePartido by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = LanguageManager.s.appTitulo,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Italic, letterSpacing = 0.sp, color = PureWhite),
                maxLines = 1, softWrap = false
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        // CRONO RING
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
            CronometroRing(estaCorriendo = estaCorriendo, colorActivo = ColorManager.chronoColor, colorFondo = SportGray)
            Text(text = textoTiempo, style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.chronoColor))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // INFO CARD
        if (HistoryManager.isEnabled && HistoryManager.tempDuracion1 != null) {
            Card(colors = CardDefaults.cardColors(containerColor = SportGray), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn1Color)); Spacer(modifier = Modifier.width(8.dp)); Text("1º: ${HistoryManager.tempDuracion1}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
                        if (HistoryManager.tempDuracion2 != null) { Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn2Color)); Spacer(modifier = Modifier.width(8.dp)); Text("2º: ${HistoryManager.tempDuracion2}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) } }
                    }
                    if (HistoryManager.tempDuracion2 != null) { IconButton(onClick = { showSaveDialog = true }) { Icon(Icons.Default.Save, contentDescription = "Guardar", tint = NeonGreen, modifier = Modifier.size(32.dp)) } }
                }
            }
        } else { Spacer(modifier = Modifier.height(60.dp)) }

        Spacer(modifier = Modifier.height(20.dp))

        // BOTONES PRINCIPALES
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val enabled1 = etapaActual != 2
            // BOTON 1
            Button(
                onClick = {
                    SoundManager.feedback(context)
                    if (!estaCorriendo) HistoryManager.iniciarSesion() else if (estaCorriendo && etapaActual == 1) HistoryManager.registrarT1(tiempoSegundos)
                    val intent = Intent(context, CronoService::class.java)
                    if (estaCorriendo && etapaActual == 1) intent.action = "PAUSAR" else { intent.action = "INICIAR"; intent.putExtra("MINUTO_INICIO", 0L); intent.putExtra("ETAPA", 1) }
                    startCronoService(context, intent)
                },
                enabled = enabled1, shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btn1Color, disabledContainerColor = ColorManager.btn1Color.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (estaCorriendo && etapaActual == 1) LanguageManager.s.btnPausar else LanguageManager.s.btn1Tiempo, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (enabled1) Color.White else Color.White.copy(alpha=0.5f), maxLines = 1, softWrap = false)
                    if (enabled1 && !(estaCorriendo && etapaActual == 1)) Text("${LanguageManager.s.desde} 00:00", fontSize = 11.sp, color = Color.White.copy(alpha=0.8f))
                }
            }

            // BOTON 2
            val enabled2 = !(estaCorriendo && etapaActual == 1)
            Button(
                onClick = {
                    SoundManager.feedback(context)
                    if (estaCorriendo && etapaActual == 1) HistoryManager.registrarT1(tiempoSegundos)
                    if (estaCorriendo && etapaActual == 2) HistoryManager.registrarT2(tiempoSegundos)
                    val intent = Intent(context, CronoService::class.java)
                    if (estaCorriendo && etapaActual == 2) intent.action = "PAUSAR" else { intent.action = "INICIAR"; val minInicio = TimeManager.secondHalfStartMinute.toLongOrNull() ?: 45L; intent.putExtra("MINUTO_INICIO", minInicio); intent.putExtra("ETAPA", 2) }
                    startCronoService(context, intent)
                },
                enabled = enabled2, shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btn2Color, disabledContainerColor = ColorManager.btn2Color.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (estaCorriendo && etapaActual == 2) LanguageManager.s.btnPausar else LanguageManager.s.btn2Tiempo, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (enabled2) Color.White else Color.White.copy(alpha=0.5f), maxLines = 1, softWrap = false)
                    if (enabled2 && !(estaCorriendo && etapaActual == 2)) Text("${LanguageManager.s.desde} ${TimeManager.secondHalfStartMinute}:00", fontSize = 11.sp, color = Color.White.copy(alpha=0.8f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTON REINICIAR
        Button(
            onClick = {
                SoundManager.feedback(context)
                val intent = Intent(context, CronoService::class.java)
                intent.action = "REINICIAR"
                context.startService(intent)
                HistoryManager.limpiarTemp()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btnResetColor),
            modifier = Modifier.width(200.dp).height(55.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.s.btnReiniciar, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.alpha(0.7f)) {
            Icon(painter = painterResource(id = R.drawable.ic_notificacion_crono), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = LanguageManager.s.desarrolladoPor, color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text(LanguageManager.s.guardarDialogTitulo) },
            text = { Column { Text(LanguageManager.s.guardarDialogMensaje); Spacer(modifier = Modifier.height(8.dp)); OutlinedTextField(value = nombrePartido, onValueChange = { nombrePartido = it }, label = { Text(LanguageManager.s.guardarDialogLabel) }, singleLine = true) } },
            confirmButton = { Button(onClick = { HistoryManager.guardarSesion(if (nombrePartido.isBlank()) "Partido sin nombre" else nombrePartido); showSaveDialog = false; nombrePartido = "" }) { Text(LanguageManager.s.btnGuardar) } },
            dismissButton = { TextButton(onClick = { showSaveDialog = false }) { Text(LanguageManager.s.btnCancelar) } }
        )
    }
}