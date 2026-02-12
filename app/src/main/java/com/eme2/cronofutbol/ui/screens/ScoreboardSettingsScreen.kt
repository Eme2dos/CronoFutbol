package com.eme2.cronofutbol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Scoreboard
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

@Composable
fun ScoreboardSettingsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(LanguageManager.s.marcadorTitulo, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Icono Grande
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Scoreboard,
                contentDescription = null,
                tint = NeonYellow,
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Interruptor
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SportGray, RoundedCornerShape(12.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(LanguageManager.s.marcadorSwitchLabel, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Switch(
                checked = MatchManager.isScoreboardEnabled,
                onCheckedChange = { MatchManager.isScoreboardEnabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Explicación
        Text(
            text = LanguageManager.s.marcadorDesc,
            color = Color.Gray,
            fontSize = 15.sp,
            lineHeight = 24.sp
        )
    }
}