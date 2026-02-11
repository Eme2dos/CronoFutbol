package com.eme2.cronofutbol.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.eme2.cronofutbol.data.ColorManager
import com.eme2.cronofutbol.data.HistoryManager
import com.eme2.cronofutbol.data.PureWhite
import com.eme2.cronofutbol.data.SportBlack
import com.eme2.cronofutbol.data.SportGray
import com.eme2.cronofutbol.data.model.SesionPartido

@Composable
fun SesionCard(sesion: SesionPartido) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SportGray),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sesion.nombre,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PureWhite)
                )
                IconButton(
                    onClick = { HistoryManager.sesiones.remove(sesion) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Gray)
                }
            }
            Text(
                text = sesion.fecha,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
            HorizontalDivider(
                color = SportBlack,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("1T", color = ColorManager.btn1Color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = sesion.duracion1,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text("2T", color = ColorManager.btn2Color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = sesion.duracion2,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}