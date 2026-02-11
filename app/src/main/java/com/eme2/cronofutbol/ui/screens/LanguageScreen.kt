package com.eme2.cronofutbol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.data.*

@Composable
fun LanguageScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(LanguageManager.s.idiomaTitulo, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val idiomas = listOf(
                Triple(LanguageManager.s.idiomaAuto, LanguageCode.AUTO, LanguageManager.s.idiomaAutoDesc),
                Triple("Español", LanguageCode.ES, "Castellano"),
                Triple("English", LanguageCode.EN, "English (Intl)"),
                Triple("Français", LanguageCode.FR, "Français"),
                Triple("Italiano", LanguageCode.IT, "Italiano"),
                Triple("Deutsch", LanguageCode.DE, "Deutsch"),
                Triple("Português", LanguageCode.PT, "Português")
            )
            items(idiomas) { (titulo, codigo, desc) ->
                Row(Modifier.fillMaxWidth().height(80.dp).selectable(selected = (codigo == LanguageManager.selectedLanguage), onClick = { LanguageManager.setLanguage(context, codigo); SoundManager.feedback(context) }, role = androidx.compose.ui.semantics.Role.RadioButton), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = (codigo == LanguageManager.selectedLanguage), onClick = null, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen, unselectedColor = Color.Gray))
                    Column(modifier = Modifier.padding(start = 16.dp)) { Text(text = titulo, fontSize = 18.sp, color = PureWhite, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(4.dp)); Text(text = desc, fontSize = 13.sp, color = Color.Gray) }
                }
            }
        }
    }
}