package com.eme2.cronofutbol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.data.*

@Composable
fun ColorScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(LanguageManager.s.coloresTitulo, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        ColorSection("Crono (Números/Aro)", ColorManager.chronoColor) { ColorManager.chronoColor = it }; HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Botón 1 Tiempo", ColorManager.btn1Color) { ColorManager.btn1Color = it }; HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Botón 2 Tiempo", ColorManager.btn2Color) { ColorManager.btn2Color = it }; HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Botón Reiniciar", ColorManager.btnResetColor) { ColorManager.btnResetColor = it }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { ColorManager.resetColors() }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.Refresh, contentDescription = null, tint = PureWhite); Spacer(modifier = Modifier.width(8.dp)); Text(LanguageManager.s.coloresRestaurar, color = PureWhite) }; Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ColorSection(title: String, currentColor: Color, onColorSelected: (Color) -> Unit) {
    Column { Text(title, color = Color.Gray, fontSize = 14.sp); Spacer(modifier = Modifier.height(12.dp)); LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) { items(AvailableColors) { color -> Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color).border(width = if (color == currentColor) 3.dp else 0.dp, color = if (color == currentColor) Color.White else Color.Transparent, shape = CircleShape).clickable { onColorSelected(color) }) } } }
}