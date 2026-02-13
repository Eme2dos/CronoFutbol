package com.eme2.cronofutbol

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eme2.cronofutbol.data.*
import com.eme2.cronofutbol.ui.components.ModernDrawerItem
import com.eme2.cronofutbol.ui.screens.*
import com.eme2.cronofutbol.utils.AdManager
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Idioma y AdMob
        LanguageManager.init(this)
        MobileAds.initialize(this) {}
        AdManager.loadInterstitial(this)

        // Comprobamos si el usuario ya pasó por el Onboarding (y simulamos estado Premium)
        val prefs = getSharedPreferences("CronoPrefs", Context.MODE_PRIVATE)
        val onboardingCompleto = prefs.getBoolean("onboarding_completed", false)
        AdManager.isPremium = prefs.getBoolean("is_premium", false) // Por defecto false

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                var showOnboarding by remember { mutableStateOf(!onboardingCompleto) }

                if (showOnboarding) {
                    OnboardingScreen(onFinish = {
                        prefs.edit().putBoolean("onboarding_completed", true).apply()
                        showOnboarding = false
                        // Mostrar anuncio justo después del Onboarding
                        AdManager.showInterstitial(this@MainActivity) {}
                    })
                } else {
                    CronoFutbolApp()
                }
            }
        }
    }
}

@Composable
fun CronoFutbolApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current as Activity

    // Función auxiliar para navegar con anuncio
    fun navigateWithAd(route: String) {
        scope.launch { drawerState.close() }
        AdManager.showInterstitial(context) {
            navController.navigate(route)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SportBlack,
                drawerContentColor = PureWhite,
                modifier = Modifier.width(320.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(SportGray, SportBlack)))
                        .padding(vertical = 40.dp, horizontal = 24.dp)
                ) {
                    Text(
                        LanguageManager.s.menuTitulo,
                        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, color = NeonYellow, letterSpacing = 4.sp)
                    )
                }

                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Navegación normal con anuncios intercalados
                    ModernDrawerItem(LanguageManager.s.menuHistorial, Icons.Default.History) { navigateWithAd("history") }
                    ModernDrawerItem(LanguageManager.s.menuSonidos, Icons.Default.MusicNote) { navigateWithAd("settings") }
                    ModernDrawerItem(LanguageManager.s.menuTiempo, Icons.Default.AccessTime) { navigateWithAd("time_settings") }
                    ModernDrawerItem(LanguageManager.s.menuColores, Icons.Default.Palette) { navigateWithAd("colors") }
                    ModernDrawerItem(LanguageManager.s.menuIdioma, Icons.Default.Language) { navigateWithAd("language") }
                    ModernDrawerItem(LanguageManager.s.menuAyuda, Icons.Default.Help) { navigateWithAd("help") }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = SportGray)
                    Spacer(modifier = Modifier.height(20.dp))

                    // BOTÓN PREMIUM
                    if (!AdManager.isPremium) {
                        ModernDrawerItem(LanguageManager.s.menuQuitarAnuncios, Icons.Default.WorkspacePremium) {
                            scope.launch { drawerState.close() }
                            // Por ahora simulamos la compra activando el Premium.
                            // Más adelante aquí conectaremos la pasarela de pago de Google Play.
                            AdManager.isPremium = true
                            context.getSharedPreferences("CronoPrefs", Context.MODE_PRIVATE).edit().putBoolean("is_premium", true).apply()
                        }
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { HomeScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
            composable("settings") { SettingsScreen(onBackClick = { navController.popBackStack() }) }
            composable("colors") { ColorScreen(onBackClick = { navController.popBackStack() }) }
            composable("time_settings") { TimeSettingsScreen(onBackClick = { navController.popBackStack() }) }
            composable("history") { HistoryScreen(onBackClick = { navController.popBackStack() }) }
            composable("language") { LanguageScreen(onBackClick = { navController.popBackStack() }) }
            composable("help") { HelpScreen(onBackClick = { navController.popBackStack() }) }
        }
    }
}