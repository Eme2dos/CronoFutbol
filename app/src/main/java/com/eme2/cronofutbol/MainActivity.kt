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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

// --- COLORES ---
val SportBlack = Color(0xFF121212)
val SportGray = Color(0xFF333333)

// Azules solicitados
val BlueFirstHalf = Color(0xFF2962FF) // Azul Fuerte
val BlueSecondHalf = Color(0xFF00B0FF) // Azul Claro
val SportRed = Color(0xFFD32F2F)      // Rojo para Reiniciar

val PureWhite = Color(0xFFFFFFFF)

// Paleta para personalización (manteniendo los neon por si acaso se usan en el aro)
val NeonGreen = Color(0xFF00E676)
val NeonRed = Color(0xFFFF1744)
val NeonBlue = Color(0xFF2979FF)
val AvailableColors = listOf(NeonGreen, NeonRed, NeonBlue, PureWhite)

// --- GESTORES DE ESTADO ---

object ColorManager {
    // Usamos el Azul 1º tiempo como color activo por defecto para el aro
    var activeColor by mutableStateOf(BlueFirstHalf)
    var textColor by mutableStateOf(PureWhite)

    fun resetColors() {
        activeColor = BlueFirstHalf
        textColor = PureWhite
    }
}

object TimeManager {
    var secondHalfStartMinute by mutableStateOf("45")
}

enum class ModoSonido { SILENCIO, SONIDO, VIBRACION, AMBOS }

object SoundManager {
    var modoActual by mutableStateOf(ModoSonido.SILENCIO)
    var customToneUri: Uri? by mutableStateOf(null)

    fun feedback(context: Context) {
        when (modoActual) {
            ModoSonido.SILENCIO -> { }
            ModoSonido.SONIDO -> playSound(context)
            ModoSonido.VIBRACION -> vibrate(context)
            ModoSonido.AMBOS -> { playSound(context); vibrate(context) }
        }
    }

    private fun playSound(context: Context) {
        try {
            val uri = customToneUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mp = MediaPlayer()
            mp.setDataSource(context, uri)
            mp.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
            mp.prepare(); mp.start(); mp.setOnCompletionListener { it.release() }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun vibrate(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION") context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        else @Suppress("DEPRECATION") vibrator.vibrate(50)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                CronoFutbolApp()
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
                drawerContainerColor = SportGray,
                drawerContentColor = ColorManager.textColor,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("MENÚ", modifier = Modifier.padding(start = 24.dp, bottom = 16.dp), style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.activeColor))
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                NavigationDrawerItem(
                    label = { Text("Sonidos", fontSize = 18.sp) }, selected = false,
                    icon = { Icon(Icons.Default.MusicNote, contentDescription = null, tint = ColorManager.textColor) },
                    onClick = { navController.navigate("settings"); scope.launch { drawerState.close() } },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent, unselectedTextColor = ColorManager.textColor)
                )

                NavigationDrawerItem(
                    label = { Text("Ajustar Tiempo", fontSize = 18.sp) }, selected = false,
                    icon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = ColorManager.textColor) },
                    onClick = { navController.navigate("time_settings"); scope.launch { drawerState.close() } },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent, unselectedTextColor = ColorManager.textColor)
                )

                NavigationDrawerItem(
                    label = { Text("Colores", fontSize = 18.sp) }, selected = false,
                    icon = { Icon(Icons.Default.Palette, contentDescription = null, tint = ColorManager.textColor) },
                    onClick = { navController.navigate("colors"); scope.launch { drawerState.close() } },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent, unselectedTextColor = ColorManager.textColor)
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { CronoFutbolScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
            composable("settings") { SonidosScreen(onBackClick = { navController.popBackStack() }) }
            composable("colors") { ColoresScreen(onBackClick = { navController.popBackStack() }) }
            composable("time_settings") { TimeSettingsScreen(onBackClick = { navController.popBackStack() }) }
        }
    }
}

@Composable
fun CronoFutbolScreen(onMenuClick: () -> Unit) {
    val context = LocalContext.current
    val tiempoSegundos = CronoService.tiempoActualSegundos
    val estaCorriendo = CronoService.estaCorriendo
    // Obtenemos la etapa actual del servicio (0=Reset, 1=1er Tiempo, 2=2do Tiempo)
    val etapaActual = CronoService.etapaActual

    val minutosAmostrar = tiempoSegundos / 60
    val segundosAmostrar = tiempoSegundos % 60
    val textoTiempo = String.format("%02d:%02d", minutosAmostrar, segundosAmostrar)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SportBlack)
            .padding(top = 80.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 1. HEADER SIMPLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menú", tint = ColorManager.textColor, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "CRONOFUTBOL",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 2.sp,
                    color = ColorManager.textColor
                )
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 2. RELOJ
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(320.dp)) {
            // El aro cambia de color según la etapa
            val colorAro = when (etapaActual) {
                1 -> BlueFirstHalf
                2 -> BlueSecondHalf
                else -> ColorManager.activeColor
            }

            CronometroRing(estaCorriendo = estaCorriendo, colorActivo = colorAro, colorFondo = SportGray)
            Text(text = textoTiempo, style = TextStyle(fontSize = 85.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.textColor))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 3. BOTONERA PRINCIPAL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LÓGICA DE HABILITACIÓN 1ER TIEMPO
            // Se deshabilita si: estamos en el 2º tiempo.
            val enabled1 = etapaActual != 2

            Button(
                onClick = {
                    SoundManager.feedback(context)
                    val intent = Intent(context, CronoService::class.java)
                    if (estaCorriendo && etapaActual == 1) {
                        intent.action = "PAUSAR"
                    } else {
                        intent.action = "INICIAR"
                        intent.putExtra("MINUTO_INICIO", 0L)
                        intent.putExtra("ETAPA", 1)
                    }
                    context.startService(intent)
                },
                enabled = enabled1,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueFirstHalf,
                    disabledContainerColor = BlueFirstHalf.copy(alpha = 0.3f)
                ),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        // Solo muestra PAUSAR si estamos corriendo ESTE tiempo
                        text = if (estaCorriendo && etapaActual == 1) "PAUSAR" else "1 TIEMPO",
                        fontSize = 20.sp, fontWeight = FontWeight.Black, color = if (enabled1) Color.White else Color.White.copy(alpha=0.5f)
                    )
                    if (enabled1 && !(estaCorriendo && etapaActual == 1)) {
                        Text("Desde 00:00", fontSize = 12.sp, color = Color.White.copy(alpha=0.8f))
                    }
                }
            }

            // LÓGICA DE HABILITACIÓN 2DO TIEMPO
            // Se deshabilita si: estamos corriendo el 1er tiempo.
            // Solo se habilita si estamos parados (pausa del 1º) o ya estamos en el 2º.
            val enabled2 = !(estaCorriendo && etapaActual == 1)

            Button(
                onClick = {
                    SoundManager.feedback(context)
                    val intent = Intent(context, CronoService::class.java)
                    if (estaCorriendo && etapaActual == 2) {
                        intent.action = "PAUSAR"
                    } else {
                        intent.action = "INICIAR"
                        val minInicio = TimeManager.secondHalfStartMinute.toLongOrNull() ?: 45L
                        intent.putExtra("MINUTO_INICIO", minInicio)
                        intent.putExtra("ETAPA", 2)
                    }
                    context.startService(intent)
                },
                enabled = enabled2,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueSecondHalf,
                    disabledContainerColor = BlueSecondHalf.copy(alpha = 0.3f)
                ),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (estaCorriendo && etapaActual == 2) "PAUSAR" else "2 TIEMPO",
                        fontSize = 20.sp, fontWeight = FontWeight.Black, color = if (enabled2) Color.White else Color.White.copy(alpha=0.5f)
                    )
                    if (enabled2 && !(estaCorriendo && etapaActual == 2)) {
                        Text("Desde ${TimeManager.secondHalfStartMinute}:00", fontSize = 12.sp, color = Color.White.copy(alpha=0.8f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. BOTÓN REINICIAR (Centrado abajo, Rojo)
        Button(
            onClick = {
                SoundManager.feedback(context)
                val intent = Intent(context, CronoService::class.java)
                intent.action = "REINICIAR"
                context.startService(intent)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SportRed),
            modifier = Modifier.width(200.dp).height(50.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("REINICIAR", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.alpha(0.7f)) {
            Icon(painter = painterResource(id = R.drawable.ic_notificacion_crono), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Desarrollado por MLaOrden", color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

// --- PANTALLAS SECUNDARIAS ---
@Composable
fun TimeSettingsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = ColorManager.textColor, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text("AJUSTAR 2º TIEMPO", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.textColor))
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("Configuración del inicio de la segunda parte.", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorManager.textColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Establece el minuto desde el que comenzará a correr el cronómetro cuando pulses el botón '2 TIEMPO' en la pantalla principal.", fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
        Spacer(modifier = Modifier.height(40.dp))
        Text("Minuto de inicio:", color = BlueSecondHalf, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.border(2.dp, ColorManager.textColor, RoundedCornerShape(12.dp)).padding(24.dp)) {
            BasicTextField(value = TimeManager.secondHalfStartMinute, onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) TimeManager.secondHalfStartMinute = it }, textStyle = TextStyle(color = ColorManager.textColor, fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.width(100.dp))
            Text("min", fontSize = 24.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ColoresScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = ColorManager.textColor, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text("COLORES", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.textColor))
        }
        Spacer(modifier = Modifier.height(40.dp))
        ColorSection("Aro Cronómetro", ColorManager.activeColor) { ColorManager.activeColor = it }
        HorizontalDivider(color = SportGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
        ColorSection("Texto y Números", ColorManager.textColor) { ColorManager.textColor = it }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { ColorManager.resetColors() }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = ColorManager.textColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Restaurar defecto", color = ColorManager.textColor)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ColorSection(title: String, currentColor: Color, onColorSelected: (Color) -> Unit) {
    Column {
        Text(title, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(AvailableColors) { color ->
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color).border(width = if (color == currentColor) 3.dp else 0.dp, color = if (color == currentColor) Color.White else Color.Transparent, shape = CircleShape).clickable { onColorSelected(color) })
            }
        }
    }
}

@Composable
fun SonidosScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val ringtonePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (uri != null) { SoundManager.customToneUri = uri; SoundManager.modoActual = ModoSonido.SONIDO }
        }
    }
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = ColorManager.textColor, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text("SONIDOS", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.textColor))
        }
        Spacer(modifier = Modifier.height(40.dp))
        val opciones = listOf(Triple("Silencio", ModoSonido.SILENCIO, "Sin aviso sonoro ni vibración"), Triple("Sonido", ModoSonido.SONIDO, "Reproduce un tono al pulsar"), Triple("Vibración", ModoSonido.VIBRACION, "Vibra al pulsar botones"), Triple("Sonido + Vibración", ModoSonido.AMBOS, "Tono y vibración simultáneos"))
        opciones.forEach { (titulo, modo, descripcion) ->
            Row(Modifier.fillMaxWidth().height(80.dp).selectable(selected = (modo == SoundManager.modoActual), onClick = { SoundManager.modoActual = modo; SoundManager.feedback(context) }, role = androidx.compose.ui.semantics.Role.RadioButton), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = (modo == SoundManager.modoActual), onClick = null, colors = RadioButtonDefaults.colors(selectedColor = ColorManager.activeColor, unselectedColor = Color.Gray))
                Column(modifier = Modifier.padding(start = 16.dp)) { Text(text = titulo, fontSize = 18.sp, color = ColorManager.textColor, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(4.dp)); Text(text = descripcion, fontSize = 13.sp, color = Color.Gray) }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (SoundManager.modoActual == ModoSonido.SONIDO || SoundManager.modoActual == ModoSonido.AMBOS) {
            Button(onClick = { val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply { putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION); putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Selecciona un tono"); putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, SoundManager.customToneUri) }; ringtonePicker.launch(intent) }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = ColorManager.activeColor); Spacer(modifier = Modifier.width(8.dp)); Text("Elegir tono personalizado", color = ColorManager.textColor)
            }
        }
    }
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
        if (estaCorriendo) {
            rotate(degrees = anguloRotacion, pivot = centerOffset) {
                drawArc(color = colorActivo, startAngle = -90f, sweepAngle = 120f, useCenter = false, topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius), size = Size(radius * 2, radius * 2), style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
            }
        }
    }
}