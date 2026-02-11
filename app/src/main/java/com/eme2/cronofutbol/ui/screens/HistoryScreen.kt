package com.eme2.cronofutbol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.data.*
import com.eme2.cronofutbol.ui.components.SesionCard

@Composable
fun HistoryScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(LanguageManager.s.historialTitulo, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth().background(SportGray, RoundedCornerShape(12.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Text(LanguageManager.s.historialSwitch, color = PureWhite, fontSize = 16.sp); Switch(checked = HistoryManager.isEnabled, onCheckedChange = { HistoryManager.isEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen)) }
        Spacer(modifier = Modifier.height(24.dp))
        if (HistoryManager.sesiones.isEmpty()) { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(LanguageManager.s.historialVacio, color = Color.Gray) } } else { LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) { items(HistoryManager.sesiones) { sesion -> SesionCard(sesion) } } }
    }
}