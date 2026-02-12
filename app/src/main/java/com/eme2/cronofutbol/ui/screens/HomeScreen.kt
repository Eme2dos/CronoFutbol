package com.eme2.cronofutbol.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.R
import com.eme2.cronofutbol.data.*
import com.eme2.cronofutbol.service.CronoService
import com.eme2.cronofutbol.ui.components.CronometroRing
import com.eme2.cronofutbol.utils.startCronoService

@Composable
fun HomeScreen(onMenuClick: () -> Unit) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var nombrePartido by remember { mutableStateOf("") }

    if (MatchManager.isScoreboardEnabled) {
        ScoreboardView(onMenuClick, onSaveClick = { showSaveDialog = true })
    } else {
        SimpleTimerView(onMenuClick, onSaveClick = { showSaveDialog = true })
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

@Composable
fun SimpleTimerView(onMenuClick: () -> Unit, onSaveClick: () -> Unit) {
    val context = LocalContext.current
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo
    val etapaActual = CronoService.etapaActual
    val textoTiempo = String.format("%02d:%02d", tiempoSegundos / 60, tiempoSegundos % 60)

    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, bottom = 16.dp, start = 24.dp, end = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = LanguageManager.s.appTitulo, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Italic, letterSpacing = 0.sp, color = PureWhite), maxLines = 1, softWrap = false)
        }
        Spacer(modifier = Modifier.height(30.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
            CronometroRing(estaCorriendo = estaCorriendo, colorActivo = ColorManager.chronoColor, colorFondo = SportGray)
            Text(text = textoTiempo, style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.chronoColor))
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (HistoryManager.isEnabled && HistoryManager.tempDuracion1 != null) {
            Card(colors = CardDefaults.cardColors(containerColor = SportGray), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn1Color)); Spacer(modifier = Modifier.width(8.dp)); Text("1º: ${HistoryManager.tempDuracion1}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
                        if (HistoryManager.tempDuracion2 != null) { Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn2Color)); Spacer(modifier = Modifier.width(8.dp)); Text("2º: ${HistoryManager.tempDuracion2}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) } }
                    }
                    if (HistoryManager.tempDuracion2 != null) { IconButton(onClick = onSaveClick) { Icon(Icons.Default.Save, contentDescription = "Guardar", tint = NeonGreen, modifier = Modifier.size(32.dp)) } }
                }
            }
        } else { Spacer(modifier = Modifier.height(60.dp)) }

        Spacer(modifier = Modifier.height(20.dp))
        BotonesControl(context, estaCorriendo, etapaActual, tiempoSegundos)
        Spacer(modifier = Modifier.weight(1f))
        FooterCredits()
    }
}

@Composable
fun ScoreboardView(onMenuClick: () -> Unit, onSaveClick: () -> Unit) {
    val context = LocalContext.current
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo
    val etapaActual = CronoService.etapaActual
    val textoTiempo = String.format("%02d:%02d", tiempoSegundos / 60, tiempoSegundos % 60)

    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 60.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = PureWhite) }
            Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).background(SportGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(8.dp)) {
                if (MatchManager.estadio.isEmpty()) Text("Estadio / Sede...", color = Color.Gray, fontSize = 12.sp)
                BasicTextField(value = MatchManager.estadio, onValueChange = { MatchManager.estadio = it }, textStyle = TextStyle(color = PureWhite, fontSize = 14.sp), singleLine = true)
            }
            if (HistoryManager.tempDuracion2 != null) { IconButton(onClick = onSaveClick) { Icon(Icons.Default.Save, contentDescription = "Guardar", tint = NeonGreen) } }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TeamColumn(name = MatchManager.equipoLocal, score = MatchManager.golesLocal, onNameChange = { MatchManager.equipoLocal = it }, onAdd = { MatchManager.agregarGol(true) }, onRemove = { MatchManager.quitarGol(true) }, color = ColorManager.btn1Color)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = textoTiempo, style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.chronoColor))
                Text(if(estaCorriendo) "JUGANDO" else "PAUSA", color = if(estaCorriendo) NeonGreen else NeonRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            TeamColumn(name = MatchManager.equipoVisitante, score = MatchManager.golesVisitante, onNameChange = { MatchManager.equipoVisitante = it }, onAdd = { MatchManager.agregarGol(false) }, onRemove = { MatchManager.quitarGol(false) }, color = ColorManager.btn2Color)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Eventos:", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp))
        Card(modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = SportGray)) {
            if (MatchManager.eventos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Sin eventos", color = Color.Gray) }
            } else {
                androidx.compose.foundation.lazy.LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    items(MatchManager.eventos.size) { i ->
                        val evento = MatchManager.eventos[i]
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(evento.minuto, color = NeonYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(35.dp))
                            Text(evento.descripcion, color = PureWhite, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = SportBlack.copy(alpha = 0.5f), thickness = 0.5.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        BotonesControl(context, estaCorriendo, etapaActual, tiempoSegundos)
        Spacer(modifier = Modifier.weight(1f))
        FooterCredits()
    }
}

@Composable
fun TeamColumn(name: String, score: Int, onNameChange: (String) -> Unit, onAdd: () -> Unit, onRemove: () -> Unit, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(110.dp)) {
        BasicTextField(
            value = name,
            onValueChange = onNameChange,
            textStyle = TextStyle(color = PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha=0.1f), RoundedCornerShape(4.dp)).padding(4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$score", style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Bold, color = color))
        Row {
            IconButton(onClick = onRemove, modifier = Modifier.size(30.dp)) { Icon(Icons.Default.Remove, contentDescription = "-", tint = Color.Gray) }
            IconButton(onClick = onAdd, modifier = Modifier.size(30.dp)) { Icon(Icons.Default.Add, contentDescription = "+", tint = PureWhite) }
        }
    }
}

@Composable
fun BotonesControl(context: Context, estaCorriendo: Boolean, etapaActual: Int, tiempoSegundos: Long) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val enabled1 = etapaActual != 2
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
                    Text(text = if (estaCorriendo && etapaActual == 1) LanguageManager.s.btnPausar else LanguageManager.s.btn1Tiempo, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (enabled1) Color.White else Color.White.copy(alpha = 0.5f), maxLines = 1, softWrap = false)
                }
            }

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
                    Text(text = if (estaCorriendo && etapaActual == 2) LanguageManager.s.btnPausar else LanguageManager.s.btn2Tiempo, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (enabled2) Color.White else Color.White.copy(alpha = 0.5f), maxLines = 1, softWrap = false)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                SoundManager.feedback(context)
                val intent = Intent(context, CronoService::class.java)
                intent.action = "REINICIAR"
                context.startService(intent)
                HistoryManager.limpiarTemp()
                MatchManager.resetMatch()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btnResetColor),
            modifier = Modifier.width(200.dp).height(55.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.s.btnReiniciar, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun FooterCredits() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.alpha(0.7f)) {
        Icon(painter = painterResource(id = R.drawable.ic_notificacion_crono), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = LanguageManager.s.desarrolladoPor, color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
    }
}