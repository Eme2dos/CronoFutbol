package com.eme2.cronofutbol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.data.*

@Composable
fun TimeSettingsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(LanguageManager.s.ajustesTiempoTitulo, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite))
        }
        Spacer(modifier = Modifier.height(40.dp))

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