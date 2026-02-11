package com.eme2.cronofutbol

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.init(this)

        // Comprobamos si el onboarding ya se hizo
        val prefs = getSharedPreferences("CronoPrefs", Context.MODE_PRIVATE)
        val onboardingCompleto = prefs.getBoolean("onboarding_completed", false)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                var showOnboarding by remember { mutableStateOf(!onboardingCompleto) }

                if (showOnboarding) {
                    OnboardingScreen(onFinish = {
                        prefs.edit().putBoolean("onboarding_completed", true).apply()
                        showOnboarding = false
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
                    ModernDrawerItem(LanguageManager.s.menuHistorial, Icons.Default.History) { navController.navigate("history"); scope.launch { drawerState.close() } }
                    ModernDrawerItem(LanguageManager.s.menuSonidos, Icons.Default.MusicNote) { navController.navigate("settings"); scope.launch { drawerState.close() } }
                    ModernDrawerItem(LanguageManager.s.menuTiempo, Icons.Default.AccessTime) { navController.navigate("time_settings"); scope.launch { drawerState.close() } }
                    ModernDrawerItem(LanguageManager.s.menuColores, Icons.Default.Palette) { navController.navigate("colors"); scope.launch { drawerState.close() } }
                    ModernDrawerItem(LanguageManager.s.menuIdioma, Icons.Default.Language) { navController.navigate("language"); scope.launch { drawerState.close() } }
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
        }
    }
}