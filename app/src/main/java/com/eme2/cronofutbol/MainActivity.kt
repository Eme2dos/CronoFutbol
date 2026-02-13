package com.eme2.cronofutbol

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- GESTIÓN PREMIUM (COMPRAS) ---
object PremiumManager {
    // Cuando conectemos con Google Play Billing, esto cambiará dinámicamente
    var isPremium by mutableStateOf(false)
}

// --- GESTIÓN ADMOB ---
object AdManager {
    private var mInterstitialAd: InterstitialAd? = null
    // ID DE PRUEBA DE GOOGLE PARA INTERSTICIALES
    private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    fun loadInterstitial(context: Context) {
        if (PremiumManager.isPremium) return // Si es premium, ni lo cargamos

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    // Muestra el anuncio y ejecuta una acción al terminar (ej: cambiar de pantalla)
    fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        if (PremiumManager.isPremium || mInterstitialAd == null) {
            onAdDismissed() // Si es premium o no cargó, pasa directamente
            loadInterstitial(activity) // Precargamos el siguiente
            return
        }

        mInterstitialAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null
                onAdDismissed()
                loadInterstitial(activity) // Cargar el siguiente tras cerrar
            }
            override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                mInterstitialAd = null
                onAdDismissed()
            }
        }
        mInterstitialAd?.show(activity)
    }
}

// --- PALETA DE COLORES ---
val SportBlack = Color(0xFF121212)
val SportGray = Color(0xFF2C2C2C)
val PureWhite = Color(0xFFFFFFFF)
val NeonGreen = Color(0xFF00E676)
val NeonRed = Color(0xFFFF1744)
val NeonBlue = Color(0xFF2979FF)
val NeonCyan = Color(0xFF00E5FF)
val NeonYellow = Color(0xFFFFEA00)
val NeonPurple = Color(0xFFD500F9)
val NeonOrange = Color(0xFFFF9100)
val DeepBlue = Color(0xFF2962FF)
val SkyBlue = Color(0xFF00B0FF)
val AvailableColors = listOf(NeonGreen, NeonRed, NeonBlue, NeonCyan, NeonYellow, NeonPurple, NeonOrange, DeepBlue, SkyBlue, PureWhite)

// --- GESTORES DE ESTADO ---
object ColorManager {
    var chronoColor by mutableStateOf(NeonGreen)
    var btn1Color by mutableStateOf(DeepBlue)
    var btn2Color by mutableStateOf(SkyBlue)
    var btnResetColor by mutableStateOf(NeonRed)

    fun resetColors() {
        chronoColor = NeonGreen
        btn1Color = DeepBlue
        btn2Color = SkyBlue
        btnResetColor = NeonRed
    }
}

object TimeManager { var secondHalfStartMinute by mutableStateOf("45") }

data class SesionPartido(val id: Long = System.currentTimeMillis(), val nombre: String, val fecha: String, val duracion1: String, val duracion2: String)

object HistoryManager {
    var isEnabled by mutableStateOf(true)
    var sesiones = mutableStateListOf<SesionPartido>()
    var fechaSesion: String = ""
    var tempDuracion1: String? by mutableStateOf(null)
    var tempDuracion2: String? by mutableStateOf(null)

    fun iniciarSesion() { if (fechaSesion.isEmpty()) fechaSesion = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) }
    fun registrarT1(segundos: Long) { tempDuracion1 = formatear(segundos) }
    fun registrarT2(segundosTotal: Long) {
        val inicio2 = (TimeManager.secondHalfStartMinute.toLongOrNull() ?: 45L) * 60
        val duracionReal = if (segundosTotal > inicio2) segundosTotal - inicio2 else 0
        tempDuracion2 = formatear(duracionReal)
    }
    fun guardarSesion(nombre: String) {
        if (tempDuracion1 != null) {
            val nuevaSesion = SesionPartido(nombre = nombre, fecha = if (fechaSesion.isNotEmpty()) fechaSesion else SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()), duracion1 = tempDuracion1 ?: "00:00", duracion2 = tempDuracion2 ?: "00:00")
            sesiones.add(0, nuevaSesion); limpiarTemp()
        }
    }
    fun limpiarTemp() { fechaSesion = ""; tempDuracion1 = null; tempDuracion2 = null }
    private fun formatear(seg: Long): String { val m = seg / 60; val s = seg % 60; return String.format("%02d:%02d", m, s) }
}

enum class ModoSonido { SILENCIO, SONIDO, VIBRACION, AMBOS }
object SoundManager {
    var modoActual by mutableStateOf(ModoSonido.SILENCIO)
    var customToneUri: Uri? by mutableStateOf(null)
    fun feedback(context: Context) {
        try {
            when (modoActual) {
                ModoSonido.SILENCIO -> { }
                ModoSonido.SONIDO -> playSound(context)
                ModoSonido.VIBRACION -> vibrate(context)
                ModoSonido.AMBOS -> { playSound(context); vibrate(context) }
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    private fun playSound(context: Context) { try { val uri = customToneUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); if (uri == null) return; val mp = MediaPlayer(); mp.setDataSource(context, uri); mp.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()); mp.prepare(); mp.start(); mp.setOnCompletionListener { it.release() } } catch (e: Exception) { e.printStackTrace() } }
    private fun vibrate(context: Context) { try { val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator else @Suppress("DEPRECATION") context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator; if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)) else @Suppress("DEPRECATION") vibrator.vibrate(50) } catch (e: Exception) { e.printStackTrace() } }
}

fun startCronoService(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ContextCompat.startForegroundService(context, intent)
    } else {
        context.startService(intent)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Google AdMob
        MobileAds.initialize(this) {}
        // Precargar el primer anuncio
        AdManager.loadInterstitial(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0) }
        setContent { MaterialTheme(colorScheme = darkColorScheme()) { CronoFutbolApp() } }
    }
}

@Composable
fun CronoFutbolApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity

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
                        "MENÚ",
                        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, color = NeonYellow, letterSpacing = 4.sp)
                    )
                }

                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    // BOTÓN PREMIUM (NUEVO)
                    if (!PremiumManager.isPremium) {
                        Surface(
                            onClick = { /* Aquí lanzaremos el billing flow de Google Play después */ },
                            shape = RoundedCornerShape(16.dp),
                            color = NeonOrange.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange),
                            modifier = Modifier.fillMaxWidth().height(60.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 20.dp)) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = NeonOrange, modifier = Modifier.size(26.dp))
                                Spacer(modifier = Modifier.width(20.dp))
                                Text("Quitar Anuncios", color = NeonOrange, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Navegación con Anuncios intercalados
                    ModernDrawerItem("Historial", Icons.Default.History) {
                        scope.launch { drawerState.close() }
                        activity?.let { AdManager.showInterstitial(it) { navController.navigate("history") } } ?: navController.navigate("history")
                    }
                    ModernDrawerItem("Sonidos", Icons.Default.MusicNote) {
                        scope.launch { drawerState.close() }
                        activity?.let { AdManager.showInterstitial(it) { navController.navigate("settings") } } ?: navController.navigate("settings")
                    }
                    ModernDrawerItem("Ajustar Tiempo", Icons.Default.AccessTime) {
                        scope.launch { drawerState.close() }
                        activity?.let { AdManager.showInterstitial(it) { navController.navigate("time_settings") } } ?: navController.navigate("time_settings")
                    }
                    ModernDrawerItem("Colores", Icons.Default.Palette) {
                        scope.launch { drawerState.close() }
                        activity?.let { AdManager.showInterstitial(it) { navController.navigate("colors") } } ?: navController.navigate("colors")
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { CronoFutbolScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
            composable("settings") { SonidosScreen(onBackClick = { navController.popBackStack() }) }
            composable("colors") { ColoresScreen(onBackClick = { navController.popBackStack() }) }
            composable("time_settings") { TimeSettingsScreen(onBackClick = { navController.popBackStack() }) }
            composable("history") { HistorialScreen(onBackClick = { navController.popBackStack() }) }
        }
    }
}

@Composable
fun ModernDrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(16.dp), color = SportGray, modifier = Modifier.fillMaxWidth().height(60.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 20.dp)) {
            Icon(icon, contentDescription = null, tint = NeonYellow, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text, color = PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun CronoFutbolScreen(onMenuClick: () -> Unit) {
    val context = LocalContext.current
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo
    val etapaActual = CronoService.etapaActual

    val minutosAmostrar = tiempoSegundos / 60
    val segundosAmostrar = tiempoSegundos % 60
    val textoTiempo = String.format("%02d:%02d", minutosAmostrar, segundosAmostrar)

    var showSaveDialog by remember { mutableStateOf(false) }
    var nombrePartido by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, bottom = 16.dp, start = 24.dp, end = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(text = "CRONOFUTBOL", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Italic, letterSpacing = 2.sp, color = PureWhite)) }
        Spacer(modifier = Modifier.height(30.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) { CronometroRing(estaCorriendo = estaCorriendo, colorActivo = ColorManager.chronoColor, colorFondo = SportGray); Text(text = textoTiempo, style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.chronoColor)) }
        Spacer(modifier = Modifier.height(20.dp))
        if (HistoryManager.isEnabled && HistoryManager.tempDuracion1 != null) { Card(colors = CardDefaults.cardColors(containerColor = SportGray), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) { Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn1Color)); Spacer(modifier = Modifier.width(8.dp)); Text("1º Tiempo: ${HistoryManager.tempDuracion1}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }; if (HistoryManager.tempDuracion2 != null) { Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn2Color)); Spacer(modifier = Modifier.width(8.dp)); Text("2º Tiempo: ${HistoryManager.tempDuracion2}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) } } }; if (HistoryManager.tempDuracion2 != null) { IconButton(onClick = { showSaveDialog = true }) { Icon(Icons.Default.Save, contentDescription = "Guardar", tint = NeonGreen, modifier = Modifier.size(32.dp)) } } } } } else { Spacer(modifier = Modifier.height(60.dp)) }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val enabled1 = etapaActual != 2
            Button(onClick = { SoundManager.feedback(context); if (!estaCorriendo) HistoryManager.iniciarSesion() else if (estaCorriendo && etapaActual == 1) HistoryManager.registrarT1(tiempoSegundos); val intent = Intent(context, CronoService::class.java); if (estaCorriendo && etapaActual == 1) intent.action = "PAUSAR" else { intent.action = "INICIAR"; intent.putExtra("MINUTO_INICIO", 0L); intent.putExtra("ETAPA", 1) }; startCronoService(context, intent) }, enabled = enabled1, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btn1Color, disabledContainerColor = ColorManager.btn1Color.copy(alpha = 0.3f)), modifier = Modifier.weight(1f).height(80.dp)) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(text = if (estaCorriendo && etapaActual == 1) "PAUSAR" else "1 TIEMPO", fontSize = 16.sp, fontWeight = FontWeight.Black, color = if (enabled1) Color.White else Color.White.copy(alpha=0.5f), maxLines = 1); if (enabled1 && !(estaCorriendo && etapaActual == 1)) Text("Desde 00:00", fontSize = 12.sp, color = Color.White.copy(alpha=0.8f)) } }
            val enabled2 = !(estaCorriendo && etapaActual == 1)
            Button(onClick = { SoundManager.feedback(context); if (estaCorriendo && etapaActual == 1) HistoryManager.registrarT1(tiempoSegundos); if (estaCorriendo && etapaActual == 2) HistoryManager.registrarT2(tiempoSegundos); val intent = Intent(context, CronoService::class.java); if (estaCorriendo && etapaActual == 2) intent.action = "PAUSAR" else { intent.action = "INICIAR"; val minInicio = TimeManager.secondHalfStartMinute.toLongOrNull() ?: 45L; intent.putExtra("MINUTO_INICIO", minInicio); intent.putExtra("ETAPA", 2) }; startCronoService(context, intent) }, enabled = enabled2, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btn2Color, disabledContainerColor = ColorManager.btn2Color.copy(alpha = 0.3f)), modifier = Modifier.weight(1f).height(80.dp)) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(text = if (estaCorriendo && etapaActual == 2) "PAUSAR" else "2 TIEMPO", fontSize = 16.sp, fontWeight = FontWeight.Black, color = if (enabled2) Color.White else Color.White.copy(alpha=0.5f), maxLines = 1); if (enabled2 && !(estaCorriendo && etapaActual == 2)) Text("Desde ${TimeManager.secondHalfStartMinute}:00", fontSize = 12.sp, color = Color.White.copy(alpha=0.8f)) } }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { SoundManager.feedback(context); val intent = Intent(context, CronoService::class.java); intent.action = "REINICIAR"; startCronoService(context, intent); HistoryManager.limpiarTemp() }, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btnResetColor), modifier = Modifier.width(200.dp).height(55.dp)) { Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White); Spacer(modifier = Modifier.width(8.dp)); Text("REINICIAR", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) }
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.alpha(0.7f)) { Icon(painter = painterResource(id = R.drawable.ic_notificacion_crono), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(8.dp)); Text(text = "Desarrollado por MLaOrden", color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace) }
    }

    if (showSaveDialog) { AlertDialog(onDismissRequest = { showSaveDialog = false }, title = { Text("Guardar Partido") }, text = { Column { Text("Asigna un nombre para identificar esta sesión:"); Spacer(modifier = Modifier.height(8.dp)); OutlinedTextField(value = nombrePartido, onValueChange = { nombrePartido = it }, label = { Text("Nombre del partido") }, singleLine = true) } }, confirmButton = { Button(onClick = { HistoryManager.guardarSesion(if (nombrePartido.isBlank()) "Partido sin nombre" else nombrePartido); showSaveDialog = false; nombrePartido = "" }) { Text("Guardar") } }, dismissButton = { TextButton(onClick = { showSaveDialog = false }) { Text("Cancelar") } }) }
}

// --- PANTALLAS SECUNDARIAS (Sin Cambios Lógicos) ---
@Composable
fun ColoresScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text("COLORES", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        ColorSection("Crono (Números y Aro)", ColorManager.chronoColor) { ColorManager.chronoColor = it }; HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Botón 1er Tiempo", ColorManager.btn1Color) { ColorManager.btn1Color = it }; HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Botón 2do Tiempo", ColorManager.btn2Color) { ColorManager.btn2Color = it }; HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Botón Reiniciar", ColorManager.btnResetColor) { ColorManager.btnResetColor = it }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { ColorManager.resetColors() }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.Refresh, contentDescription = null, tint = PureWhite); Spacer(modifier = Modifier.width(8.dp)); Text("Restaurar defecto", color = PureWhite) }; Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun HistorialScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text("HISTORIAL", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth().background(SportGray, RoundedCornerShape(12.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Text("Registrar sesiones", color = PureWhite, fontSize = 16.sp); Switch(checked = HistoryManager.isEnabled, onCheckedChange = { HistoryManager.isEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen)) }
        Spacer(modifier = Modifier.height(24.dp))
        if (HistoryManager.sesiones.isEmpty()) { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No hay partidos guardados", color = Color.Gray) } } else { LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) { items(HistoryManager.sesiones) { sesion -> SesionCard(sesion) } } }
    }
}

@Composable
fun SesionCard(sesion: SesionPartido) {
    Card(colors = CardDefaults.cardColors(containerColor = SportGray), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(sesion.nombre, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PureWhite)); IconButton(onClick = { HistoryManager.sesiones.remove(sesion) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Gray) } }
            Text(sesion.fecha, style = TextStyle(fontSize = 12.sp, color = Color.Gray))
            HorizontalDivider(color = SportBlack, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) { Column(modifier = Modifier.weight(1f)) { Text("1er Tiempo", color = ColorManager.btn1Color, fontSize = 12.sp, fontWeight = FontWeight.Bold); Text(sesion.duracion1, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = FontFamily.Monospace) }; Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) { Text("2do Tiempo", color = ColorManager.btn2Color, fontSize = 12.sp, fontWeight = FontWeight.Bold); Text(sesion.duracion2, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = FontFamily.Monospace) } }
        }
    }
}

@Composable
fun TimeSettingsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text("AJUSTAR 2º TIEMPO", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        Text("Configuración del inicio de la segunda parte.", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PureWhite); Spacer(modifier = Modifier.height(8.dp))
        Text("Establece el minuto desde el que comenzará a correr el cronómetro cuando pulses el botón '2 TIEMPO' en la pantalla principal.", fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp); Spacer(modifier = Modifier.height(40.dp))
        Text("Minuto de inicio:", color = NeonBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.border(2.dp, PureWhite, RoundedCornerShape(12.dp)).padding(24.dp)) { BasicTextField(value = TimeManager.secondHalfStartMinute, onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) TimeManager.secondHalfStartMinute = it }, textStyle = TextStyle(color = PureWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.width(100.dp)); Text("min", fontSize = 24.sp, color = Color.Gray, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun SonidosScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val ringtonePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result -> if (result.resultCode == Activity.RESULT_OK) { val uri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI); if (uri != null) { SoundManager.customToneUri = uri; SoundManager.modoActual = ModoSonido.SONIDO } } }
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text("SONIDOS", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        val opciones = listOf(Triple("Silencio", ModoSonido.SILENCIO, "Sin aviso sonoro ni vibración"), Triple("Sonido", ModoSonido.SONIDO, "Reproduce un tono al pulsar"), Triple("Vibración", ModoSonido.VIBRACION, "Vibra al pulsar botones"), Triple("Sonido + Vibración", ModoSonido.AMBOS, "Tono y vibración simultáneos"))
        opciones.forEach { (titulo, modo, descripcion) -> Row(Modifier.fillMaxWidth().height(80.dp).selectable(selected = (modo == SoundManager.modoActual), onClick = { SoundManager.modoActual = modo; SoundManager.feedback(context) }, role = androidx.compose.ui.semantics.Role.RadioButton), verticalAlignment = Alignment.CenterVertically) { RadioButton(selected = (modo == SoundManager.modoActual), onClick = null, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen, unselectedColor = Color.Gray)); Column(modifier = Modifier.padding(start = 16.dp)) { Text(text = titulo, fontSize = 18.sp, color = PureWhite, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(4.dp)); Text(text = descripcion, fontSize = 13.sp, color = Color.Gray) } } }
        Spacer(modifier = Modifier.height(32.dp))
        if (SoundManager.modoActual == ModoSonido.SONIDO || SoundManager.modoActual == ModoSonido.AMBOS) { Button(onClick = { val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply { putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION); putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Selecciona un tono"); putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, SoundManager.customToneUri) }; ringtonePicker.launch(intent) }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonGreen); Spacer(modifier = Modifier.width(8.dp)); Text("Elegir tono personalizado", color = PureWhite) } }
    }
}

@Composable
fun ColorSection(title: String, currentColor: Color, onColorSelected: (Color) -> Unit) {
    Column { Text(title, color = Color.Gray, fontSize = 14.sp); Spacer(modifier = Modifier.height(12.dp)); LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) { items(AvailableColors) { color -> Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color).border(width = if (color == currentColor) 3.dp else 0.dp, color = if (color == currentColor) Color.White else Color.Transparent, shape = CircleShape).clickable { onColorSelected(color) }) } } }
}

@Composable
fun CronometroRing(estaCorriendo: Boolean, colorActivo: Color, colorFondo: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin_transition")
    val anguloRotacion by infiniteTransition.animateFloat(initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Restart), label = "spin_animation")
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 12.dp.toPx()
        val radius = size.minDimension / 2 - strokeWidth
        val centerOffset = Offset(size.width / 2, size.height / 2)
        drawCircle(color = colorFondo, radius = radius, center = centerOffset, style = Stroke(width = strokeWidth))
        if (estaCorriendo) { rotate(degrees = anguloRotacion, pivot = centerOffset) { drawArc(color = colorActivo, startAngle = -90f, sweepAngle = 120f, useCenter = false, topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius), size = Size(radius * 2, radius * 2), style = Stroke(width = strokeWidth, cap = StrokeCap.Round)) } }
    }
}