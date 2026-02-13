package com.eme2.cronofutbol.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.data.*
import com.eme2.cronofutbol.utils.AdManager

@Composable
fun PremiumScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SportBlack)
            .padding(top = 80.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                LanguageManager.s.premiumTitulo,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Icono Central
        Icon(
            imageVector = Icons.Default.WorkspacePremium,
            contentDescription = null,
            tint = NeonYellow,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = LanguageManager.s.premiumDesc,
            color = Color.LightGray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Beneficios
        Card(
            colors = CardDefaults.cardColors(containerColor = SportGray),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(LanguageManager.s.premiumVentaja1, color = PureWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(LanguageManager.s.premiumVentaja2, color = PureWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón de Compra o Mensaje de Agradecimiento
        if (AdManager.isPremium) {
            Text(
                text = LanguageManager.s.premiumGracias,
                color = NeonGreen,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        } else {
            Button(
                onClick = {
                    // Aquí en el futuro irá el código de facturación real de Google Play
                    AdManager.isPremium = true
                    context.getSharedPreferences("CronoPrefs", Context.MODE_PRIVATE)
                        .edit().putBoolean("is_premium", true).apply()
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonYellow),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 10.dp)
            ) {
                Text(
                    text = LanguageManager.s.premiumBtnComprar,
                    color = SportBlack,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}