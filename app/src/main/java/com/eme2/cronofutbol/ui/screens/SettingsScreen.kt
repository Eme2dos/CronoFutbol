package com.eme2.cronofutbol.ui.screens

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.data.*

// --- PANTALLA DE SONIDOS ---
@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val ringtonePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (uri != null) {
                SoundManager.customToneUri = uri
                SoundManager.modoActual = ModoSonido.SONIDO
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(LanguageManager.s.sonidosTitulo, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite))
        }
        Spacer(modifier = Modifier.height(40.dp))
        val opciones = listOf(
            Triple(LanguageManager.s.sonidoSilencio, ModoSonido.SILENCIO, LanguageManager.s.sonidoSilencioDesc),
            Triple(LanguageManager.s.sonidoSonido, ModoSonido.SONIDO, LanguageManager.s.sonidoSonidoDesc),
            Triple(LanguageManager.s.sonidoVibracion, ModoSonido.VIBRACION, LanguageManager.s.sonidoVibracionDesc),
            Triple(LanguageManager.s.sonidoAmbos, ModoSonido.AMBOS, LanguageManager.s.sonidoAmbosDesc)
        )
        opciones.forEach { (titulo, modo, descripcion) ->
            Row(Modifier.fillMaxWidth().height(80.dp).selectable(selected = (modo == SoundManager.modoActual), onClick = { SoundManager.modoActual = modo; SoundManager.feedback(context) }, role = androidx.compose.ui.semantics.Role.RadioButton), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = (modo == SoundManager.modoActual), onClick = null, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen, unselectedColor = Color.Gray))
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = titulo, fontSize = 18.sp, color = PureWhite, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = descripcion, fontSize = 13.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (SoundManager.modoActual == ModoSonido.SONIDO || SoundManager.modoActual == ModoSonido.AMBOS) {
            Button(onClick = {
                val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                    putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                    putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Selecciona un tono")
                    putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, SoundManager.customToneUri)
                }
                ringtonePicker.launch(intent)
            }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text(LanguageManager.s.btnElegirTono, color = PureWhite)
            }
        }
    }
}

// --- PANTALLA DE AJUSTES DE TIEMPO (Limpia) ---
@Composable
fun TimeSettingsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(LanguageManager.s.ajustesTiempoTitulo, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Configuración del 2º tiempo
        Text(LanguageManager.s.ajustesTiempoDesc1, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PureWhite); Spacer(modifier = Modifier.height(8.dp))
        Text(LanguageManager.s.ajustesTiempoDesc2, fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp); Spacer(modifier = Modifier.height(40.dp))
        Text(LanguageManager.s.ajustesTiempoLabel, color = NeonBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.border(2.dp, PureWhite, RoundedCornerShape(12.dp)).padding(24.dp)) {
            BasicTextField(
                value = TimeManager.secondHalfStartMinute,
                onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) TimeManager.secondHalfStartMinute = it },
                textStyle = TextStyle(color = PureWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(100.dp)
            )
            Text("min", fontSize = 24.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}