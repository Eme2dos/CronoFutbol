package com.eme2.cronofutbol.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eme2.cronofutbol.R
import com.eme2.cronofutbol.data.*

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ -> onFinish() }
    )

    // Leer la versión automáticamente desde Gradle
    val appVersion = remember {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            "v${packageInfo.versionName}"
        } catch (e: Exception) {
            "v1.0"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SportBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo de la App
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo App",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Título
        Text(
            text = LanguageManager.s.appTitulo,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = PureWhite
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtítulo Bienvenida
        Text(
            text = LanguageManager.s.welcomeTitulo,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = NeonBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Explicación de permisos
        Text(
            text = LanguageManager.s.welcomeDesc,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Botón de Continuar
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    onFinish()
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = SportBlack)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = LanguageManager.s.welcomeBtn,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SportBlack
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Versión de la app
        Text(
            text = appVersion,
            color = Color.DarkGray,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}