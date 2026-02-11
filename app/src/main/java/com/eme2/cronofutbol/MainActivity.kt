package com.eme2.cronofutbol

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- PALETA DE COLORES ---
val SportBlack = Color(0xFF121212)
val SportGray = Color(0xFF2C2C2C)
val PureWhite = Color(0xFFFFFFFF)

// Colores seleccionables
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

// --- GESTIÓN DE IDIOMAS MULTILENGUAJE (6 IDIOMAS) ---

interface AppStrings {
    val menuTitulo: String
    val menuHistorial: String
    val menuSonidos: String
    val menuTiempo: String
    val menuColores: String
    val menuIdioma: String
    val appTitulo: String
    val btnPausar: String
    val btn1Tiempo: String
    val btn2Tiempo: String
    val btnReiniciar: String
    val desde: String
    val guardarDialogTitulo: String
    val guardarDialogMensaje: String
    val guardarDialogLabel: String
    val btnGuardar: String
    val btnCancelar: String
    val desarrolladoPor: String
    val historialTitulo: String
    val historialSwitch: String
    val historialVacio: String
    val ajustesTiempoTitulo: String
    val ajustesTiempoDesc1: String
    val ajustesTiempoDesc2: String
    val ajustesTiempoLabel: String
    val coloresTitulo: String
    val coloresRestaurar: String
    val sonidosTitulo: String
    val sonidoSilencio: String
    val sonidoSilencioDesc: String
    val sonidoSonido: String
    val sonidoSonidoDesc: String
    val sonidoVibracion: String
    val sonidoVibracionDesc: String
    val sonidoAmbos: String
    val sonidoAmbosDesc: String
    val btnElegirTono: String
    val idiomaTitulo: String
    val idiomaAuto: String
    val idiomaAutoDesc: String
}

// 1. ESPAÑOL
object EsStrings : AppStrings {
    override val menuTitulo = "MENÚ"
    override val menuHistorial = "Historial"
    override val menuSonidos = "Sonidos"
    override val menuTiempo = "Ajustar Tiempo"
    override val menuColores = "Colores"
    override val menuIdioma = "Idioma"
    override val appTitulo = "CRONOFUTBOL"
    override val btnPausar = "PAUSAR"
    override val btn1Tiempo = "1 TIEMPO"
    override val btn2Tiempo = "2 TIEMPO"
    override val btnReiniciar = "REINICIAR"
    override val desde = "Desde"
    override val guardarDialogTitulo = "Guardar Partido"
    override val guardarDialogMensaje = "Asigna un nombre para identificar esta sesión:"
    override val guardarDialogLabel = "Nombre del partido"
    override val btnGuardar = "Guardar"
    override val btnCancelar = "Cancelar"
    override val desarrolladoPor = "Desarrollado por MLaOrden"
    override val historialTitulo = "HISTORIAL"
    override val historialSwitch = "Registrar sesiones"
    override val historialVacio = "No hay partidos guardados"
    override val ajustesTiempoTitulo = "AJUSTAR 2º TIEMPO"
    override val ajustesTiempoDesc1 = "Configuración del inicio de la segunda parte."
    override val ajustesTiempoDesc2 = "Establece el minuto desde el que comenzará a correr el crono al pulsar '2 TIEMPO'."
    override val ajustesTiempoLabel = "Minuto de inicio:"
    override val coloresTitulo = "COLORES"
    override val coloresRestaurar = "Restaurar defecto"
    override val sonidosTitulo = "SONIDOS"
    override val sonidoSilencio = "Silencio"
    override val sonidoSilencioDesc = "Sin aviso sonoro ni vibración"
    override val sonidoSonido = "Sonido"
    override val sonidoSonidoDesc = "Reproduce un tono al pulsar"
    override val sonidoVibracion = "Vibración"
    override val sonidoVibracionDesc = "Vibra al pulsar botones"
    override val sonidoAmbos = "Sonido + Vibración"
    override val sonidoAmbosDesc = "Tono y vibración simultáneos"
    override val btnElegirTono = "Elegir tono personalizado"
    override val idiomaTitulo = "IDIOMA"
    override val idiomaAuto = "Automático"
    override val idiomaAutoDesc = "Usa el idioma del dispositivo"
}

// 2. INGLÉS (ENGLISH)
object EnStrings : AppStrings {
    override val menuTitulo = "MENU"
    override val menuHistorial = "History"
    override val menuSonidos = "Sounds"
    override val menuTiempo = "Time Settings"
    override val menuColores = "Colors"
    override val menuIdioma = "Language"
    override val appTitulo = "FOOTBALL TIMER"
    override val btnPausar = "PAUSE"
    override val btn1Tiempo = "1st HALF"
    override val btn2Tiempo = "2nd HALF"
    override val btnReiniciar = "RESET"
    override val desde = "From"
    override val guardarDialogTitulo = "Save Match"
    override val guardarDialogMensaje = "Assign a name to identify this session:"
    override val guardarDialogLabel = "Match name"
    override val btnGuardar = "Save"
    override val btnCancelar = "Cancel"
    override val desarrolladoPor = "Developed by MLaOrden"
    override val historialTitulo = "HISTORY"
    override val historialSwitch = "Record sessions"
    override val historialVacio = "No matches saved"
    override val ajustesTiempoTitulo = "2nd HALF SETTINGS"
    override val ajustesTiempoDesc1 = "Second half start configuration."
    override val ajustesTiempoDesc2 = "Set the minute from which the timer will start counting when pressing '2nd HALF'."
    override val ajustesTiempoLabel = "Start minute:"
    override val coloresTitulo = "COLORS"
    override val coloresRestaurar = "Restore default"
    override val sonidosTitulo = "SOUNDS"
    override val sonidoSilencio = "Silent"
    override val sonidoSilencioDesc = "No sound or vibration"
    override val sonidoSonido = "Sound"
    override val sonidoSonidoDesc = "Plays a tone on click"
    override val sonidoVibracion = "Vibration"
    override val sonidoVibracionDesc = "Vibrates on click"
    override val sonidoAmbos = "Sound + Vibration"
    override val sonidoAmbosDesc = "Tone and vibration together"
    override val btnElegirTono = "Choose custom tone"
    override val idiomaTitulo = "LANGUAGE"
    override val idiomaAuto = "Automatic"
    override val idiomaAutoDesc = "Uses device language"
}

// 3. FRANCÉS (FRANÇAIS)
object FrStrings : AppStrings {
    override val menuTitulo = "MENU"
    override val menuHistorial = "Historique"
    override val menuSonidos = "Sons"
    override val menuTiempo = "Réglage Temps"
    override val menuColores = "Couleurs"
    override val menuIdioma = "Langue"
    override val appTitulo = "CHRONO FOOT"
    override val btnPausar = "PAUSE"
    override val btn1Tiempo = "1ère MI-TEMPS"
    override val btn2Tiempo = "2ème MI-TEMPS"
    override val btnReiniciar = "RÉINITIALISER"
    override val desde = "De"
    override val guardarDialogTitulo = "Sauvegarder"
    override val guardarDialogMensaje = "Donnez un nom pour identifier cette session :"
    override val guardarDialogLabel = "Nom du match"
    override val btnGuardar = "Sauvegarder"
    override val btnCancelar = "Annuler"
    override val desarrolladoPor = "Développé par MLaOrden"
    override val historialTitulo = "HISTORIQUE"
    override val historialSwitch = "Enregistrer sessions"
    override val historialVacio = "Aucun match enregistré"
    override val ajustesTiempoTitulo = "RÉGLAGE 2ème MI-TEMPS"
    override val ajustesTiempoDesc1 = "Configuration du début de la seconde période."
    override val ajustesTiempoDesc2 = "Définit la minute de départ du chronomètre lors de l'appui sur '2ème MI-TEMPS'."
    override val ajustesTiempoLabel = "Minute de début :"
    override val coloresTitulo = "COULEURS"
    override val coloresRestaurar = "Rétablir par défaut"
    override val sonidosTitulo = "SONS"
    override val sonidoSilencio = "Silencieux"
    override val sonidoSilencioDesc = "Ni son ni vibration"
    override val sonidoSonido = "Son"
    override val sonidoSonidoDesc = "Joue une tonalité au clic"
    override val sonidoVibracion = "Vibration"
    override val sonidoVibracionDesc = "Vibre au clic"
    override val sonidoAmbos = "Son + Vibration"
    override val sonidoAmbosDesc = "Tonalité et vibration"
    override val btnElegirTono = "Choisir une tonalité"
    override val idiomaTitulo = "LANGUE"
    override val idiomaAuto = "Automatique"
    override val idiomaAutoDesc = "Utilise la langue de l'appareil"
}

// 4. ITALIANO
object ItStrings : AppStrings {
    override val menuTitulo = "MENU"
    override val menuHistorial = "Cronologia"
    override val menuSonidos = "Suoni"
    override val menuTiempo = "Imposta Tempo"
    override val menuColores = "Colori"
    override val menuIdioma = "Lingua"
    override val appTitulo = "CRONOFUTBOL"
    override val btnPausar = "PAUSA"
    override val btn1Tiempo = "1° TEMPO"
    override val btn2Tiempo = "2° TEMPO"
    override val btnReiniciar = "RESET"
    override val desde = "Da"
    override val guardarDialogTitulo = "Salva Partita"
    override val guardarDialogMensaje = "Assegna un nome per identificare questa sessione:"
    override val guardarDialogLabel = "Nome della partita"
    override val btnGuardar = "Salva"
    override val btnCancelar = "Annulla"
    override val desarrolladoPor = "Sviluppato da MLaOrden"
    override val historialTitulo = "CRONOLOGIA"
    override val historialSwitch = "Registra sessioni"
    override val historialVacio = "Nessuna partita salvata"
    override val ajustesTiempoTitulo = "IMPOSTA 2° TEMPO"
    override val ajustesTiempoDesc1 = "Configurazione dell'inizio del secondo tempo."
    override val ajustesTiempoDesc2 = "Imposta il minuto da cui partirà il cronometro premendo '2° TEMPO'."
    override val ajustesTiempoLabel = "Minuto di inizio:"
    override val coloresTitulo = "COLORI"
    override val coloresRestaurar = "Ripristina default"
    override val sonidosTitulo = "SUONI"
    override val sonidoSilencio = "Silenzioso"
    override val sonidoSilencioDesc = "Nessun suono o vibrazione"
    override val sonidoSonido = "Suono"
    override val sonidoSonidoDesc = "Riproduce un tono al tocco"
    override val sonidoVibracion = "Vibrazione"
    override val sonidoVibracionDesc = "Vibra al tocco"
    override val sonidoAmbos = "Suono + Vibrazione"
    override val sonidoAmbosDesc = "Tono e vibrazione insieme"
    override val btnElegirTono = "Scegli tono personalizzato"
    override val idiomaTitulo = "LINGUA"
    override val idiomaAuto = "Automatico"
    override val idiomaAutoDesc = "Usa la lingua del dispositivo"
}

// 5. ALEMÁN (DEUTSCH)
object DeStrings : AppStrings {
    override val menuTitulo = "MENÜ"
    override val menuHistorial = "Verlauf"
    override val menuSonidos = "Töne"
    override val menuTiempo = "Zeit einstellen"
    override val menuColores = "Farben"
    override val menuIdioma = "Sprache"
    override val appTitulo = "FUSSBALL TIMER"
    override val btnPausar = "PAUSE"
    override val btn1Tiempo = "1. HALBZEIT"
    override val btn2Tiempo = "2. HALBZEIT"
    override val btnReiniciar = "NEUSTART"
    override val desde = "Ab"
    override val guardarDialogTitulo = "Spiel speichern"
    override val guardarDialogMensaje = "Geben Sie einen Namen für diese Sitzung ein:"
    override val guardarDialogLabel = "Spielname"
    override val btnGuardar = "Speichern"
    override val btnCancelar = "Abbrechen"
    override val desarrolladoPor = "Entwickelt von MLaOrden"
    override val historialTitulo = "VERLAUF"
    override val historialSwitch = "Sitzungen aufzeichnen"
    override val historialVacio = "Keine Spiele gespeichert"
    override val ajustesTiempoTitulo = "2. HALBZEIT EINSTELLEN"
    override val ajustesTiempoDesc1 = "Konfiguration für den Beginn der zweiten Hälfte."
    override val ajustesTiempoDesc2 = "Stellen Sie die Startminute ein, wenn Sie '2. HALBZEIT' drücken."
    override val ajustesTiempoLabel = "Startminute:"
    override val coloresTitulo = "FARBEN"
    override val coloresRestaurar = "Standard wiederherstellen"
    override val sonidosTitulo = "TÖNE"
    override val sonidoSilencio = "Lautlos"
    override val sonidoSilencioDesc = "Kein Ton oder Vibration"
    override val sonidoSonido = "Ton"
    override val sonidoSonidoDesc = "Spielt einen Ton beim Klicken"
    override val sonidoVibracion = "Vibration"
    override val sonidoVibracionDesc = "Vibriert beim Klicken"
    override val sonidoAmbos = "Ton + Vibration"
    override val sonidoAmbosDesc = "Ton und Vibration zusammen"
    override val btnElegirTono = "Eigenen Ton wählen"
    override val idiomaTitulo = "SPRACHE"
    override val idiomaAuto = "Automatisch"
    override val idiomaAutoDesc = "Gerätesprache verwenden"
}

// 6. PORTUGUÉS (PORTUGUÊS)
object PtStrings : AppStrings {
    override val menuTitulo = "MENU"
    override val menuHistorial = "Histórico"
    override val menuSonidos = "Sons"
    override val menuTiempo = "Ajustar Tempo"
    override val menuColores = "Cores"
    override val menuIdioma = "Idioma"
    override val appTitulo = "CRONOFUTBOL"
    override val btnPausar = "PAUSA"
    override val btn1Tiempo = "1º TEMPO"
    override val btn2Tiempo = "2º TEMPO"
    override val btnReiniciar = "REINICIAR"
    override val desde = "De"
    override val guardarDialogTitulo = "Salvar Jogo"
    override val guardarDialogMensaje = "Atribua um nome para identificar esta sessão:"
    override val guardarDialogLabel = "Nome do jogo"
    override val btnGuardar = "Salvar"
    override val btnCancelar = "Cancelar"
    override val desarrolladoPor = "Desenvolvido por MLaOrden"
    override val historialTitulo = "HISTÓRICO"
    override val historialSwitch = "Registrar sessões"
    override val historialVacio = "Nenhum jogo salvo"
    override val ajustesTiempoTitulo = "AJUSTAR 2º TEMPO"
    override val ajustesTiempoDesc1 = "Configuração do início da segunda parte."
    override val ajustesTiempoDesc2 = "Defina o minuto de início ao pressionar o botão '2º TEMPO'."
    override val ajustesTiempoLabel = "Minuto de início:"
    override val coloresTitulo = "CORES"
    override val coloresRestaurar = "Restaurar padrão"
    override val sonidosTitulo = "SONS"
    override val sonidoSilencio = "Silêncio"
    override val sonidoSilencioDesc = "Sem som ou vibração"
    override val sonidoSonido = "Som"
    override val sonidoSonidoDesc = "Toca um tom ao clicar"
    override val sonidoVibracion = "Vibração"
    override val sonidoVibracionDesc = "Vibra ao clicar"
    override val sonidoAmbos = "Som + Vibração"
    override val sonidoAmbosDesc = "Tom e vibração juntos"
    override val btnElegirTono = "Escolher tom personalizado"
    override val idiomaTitulo = "IDIOMA"
    override val idiomaAuto = "Automático"
    override val idiomaAutoDesc = "Usa o idioma do dispositivo"
}

// --- GESTOR DE IDIOMAS LÓGICA ---

enum class LanguageCode { AUTO, ES, EN, FR, IT, DE, PT }

object LanguageManager {
    var selectedLanguage by mutableStateOf(LanguageCode.AUTO)

    val s: AppStrings
        get() = when (selectedLanguage) {
            LanguageCode.ES -> EsStrings
            LanguageCode.EN -> EnStrings
            LanguageCode.FR -> FrStrings
            LanguageCode.IT -> ItStrings
            LanguageCode.DE -> DeStrings
            LanguageCode.PT -> PtStrings
            LanguageCode.AUTO -> {
                when (Locale.getDefault().language) {
                    "es" -> EsStrings
                    "fr" -> FrStrings
                    "it" -> ItStrings
                    "de" -> DeStrings
                    "pt" -> PtStrings
                    else -> EnStrings // Default internacional
                }
            }
        }

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("CronoPrefs", Context.MODE_PRIVATE)
        val langStr = prefs.getString("language", "AUTO") ?: "AUTO"
        selectedLanguage = try { LanguageCode.valueOf(langStr) } catch(e: Exception) { LanguageCode.AUTO }
    }

    fun setLanguage(context: Context, code: LanguageCode) {
        selectedLanguage = code
        val prefs = context.getSharedPreferences("CronoPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("language", code.name).apply()
    }
}

// --- GESTORES DE ESTADO EXISTENTES ---

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

object TimeManager {
    var secondHalfStartMinute by mutableStateOf("45")
}

// --- GESTOR DE HISTORIAL ---
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
    private fun playSound(context: Context) { try { val uri = customToneUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); val mp = MediaPlayer(); mp.setDataSource(context, uri); mp.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()); mp.prepare(); mp.start(); mp.setOnCompletionListener { it.release() } } catch (e: Exception) { e.printStackTrace() } }
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
        LanguageManager.init(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0) }
        setContent { MaterialTheme(colorScheme = darkColorScheme()) { CronoFutbolApp() } }
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
            composable("home") { CronoFutbolScreen(onMenuClick = { scope.launch { drawerState.open() } }) }
            composable("settings") { SonidosScreen(onBackClick = { navController.popBackStack() }) }
            composable("colors") { ColoresScreen(onBackClick = { navController.popBackStack() }) }
            composable("time_settings") { TimeSettingsScreen(onBackClick = { navController.popBackStack() }) }
            composable("history") { HistorialScreen(onBackClick = { navController.popBackStack() }) }
            composable("language") { LanguageScreen(onBackClick = { navController.popBackStack() }) }
        }
    }
}

@Composable
fun ModernDrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = SportGray,
        modifier = Modifier.fillMaxWidth().height(60.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Icon(icon, contentDescription = null, tint = NeonYellow, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(text, color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
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

    Column(
        modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, bottom = 16.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            // --- CORRECCIÓN VISUAL TÍTULO ---
            Text(
                text = LanguageManager.s.appTitulo,
                style = TextStyle(
                    fontSize = 20.sp, // Reducido de 24 a 20
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.sp, // Eliminado el espaciado extra
                    color = PureWhite
                ),
                maxLines = 1,
                softWrap = false
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
            CronometroRing(estaCorriendo = estaCorriendo, colorActivo = ColorManager.chronoColor, colorFondo = SportGray)
            Text(text = textoTiempo, style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = ColorManager.chronoColor))
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (HistoryManager.isEnabled && HistoryManager.tempDuracion1 != null) {
            Card(colors = CardDefaults.cardColors(containerColor = SportGray), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn1Color)); Spacer(modifier = Modifier.width(8.dp)); Text("1º: ${HistoryManager.tempDuracion1}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
                        if (HistoryManager.tempDuracion2 != null) { Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorManager.btn2Color)); Spacer(modifier = Modifier.width(8.dp)); Text("2º: ${HistoryManager.tempDuracion2}", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) } }
                    }
                    if (HistoryManager.tempDuracion2 != null) { IconButton(onClick = { showSaveDialog = true }) { Icon(Icons.Default.Save, contentDescription = "Guardar", tint = NeonGreen, modifier = Modifier.size(32.dp)) } }
                }
            }
        } else { Spacer(modifier = Modifier.height(60.dp)) }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val enabled1 = etapaActual != 2
            Button(
                onClick = {
                    SoundManager.feedback(context);
                    if (!estaCorriendo) HistoryManager.iniciarSesion() else if (estaCorriendo && etapaActual == 1) HistoryManager.registrarT1(tiempoSegundos);
                    val intent = Intent(context, CronoService::class.java);
                    if (estaCorriendo && etapaActual == 1) intent.action = "PAUSAR" else { intent.action = "INICIAR"; intent.putExtra("MINUTO_INICIO", 0L); intent.putExtra("ETAPA", 1) };
                    startCronoService(context, intent)
                },
                enabled = enabled1, shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btn1Color, disabledContainerColor = ColorManager.btn1Color.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (estaCorriendo && etapaActual == 1) LanguageManager.s.btnPausar else LanguageManager.s.btn1Tiempo, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (enabled1) Color.White else Color.White.copy(alpha=0.5f), maxLines = 1, softWrap = false)
                    if (enabled1 && !(estaCorriendo && etapaActual == 1)) Text("${LanguageManager.s.desde} 00:00", fontSize = 11.sp, color = Color.White.copy(alpha=0.8f))
                }
            }

            val enabled2 = !(estaCorriendo && etapaActual == 1)
            Button(
                onClick = {
                    SoundManager.feedback(context);
                    if (estaCorriendo && etapaActual == 1) HistoryManager.registrarT1(tiempoSegundos);
                    if (estaCorriendo && etapaActual == 2) HistoryManager.registrarT2(tiempoSegundos);
                    val intent = Intent(context, CronoService::class.java);
                    if (estaCorriendo && etapaActual == 2) intent.action = "PAUSAR" else { intent.action = "INICIAR"; val minInicio = TimeManager.secondHalfStartMinute.toLongOrNull() ?: 45L; intent.putExtra("MINUTO_INICIO", minInicio); intent.putExtra("ETAPA", 2) };
                    startCronoService(context, intent)
                },
                enabled = enabled2, shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btn2Color, disabledContainerColor = ColorManager.btn2Color.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (estaCorriendo && etapaActual == 2) LanguageManager.s.btnPausar else LanguageManager.s.btn2Tiempo, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (enabled2) Color.White else Color.White.copy(alpha=0.5f), maxLines = 1, softWrap = false)
                    if (enabled2 && !(estaCorriendo && etapaActual == 2)) Text("${LanguageManager.s.desde} ${TimeManager.secondHalfStartMinute}:00", fontSize = 11.sp, color = Color.White.copy(alpha=0.8f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                SoundManager.feedback(context)
                val intent = Intent(context, CronoService::class.java)
                intent.action = "REINICIAR"
                context.startService(intent)
                HistoryManager.limpiarTemp()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorManager.btnResetColor),
            modifier = Modifier.width(200.dp).height(55.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(LanguageManager.s.btnReiniciar, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.alpha(0.7f)) { Icon(painter = painterResource(id = R.drawable.ic_notificacion_crono), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(8.dp)); Text(text = LanguageManager.s.desarrolladoPor, color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Monospace) }
    }

    if (showSaveDialog) {
        AlertDialog(onDismissRequest = { showSaveDialog = false }, title = { Text(LanguageManager.s.guardarDialogTitulo) }, text = { Column { Text(LanguageManager.s.guardarDialogMensaje); Spacer(modifier = Modifier.height(8.dp)); OutlinedTextField(value = nombrePartido, onValueChange = { nombrePartido = it }, label = { Text(LanguageManager.s.guardarDialogLabel) }, singleLine = true) } }, confirmButton = { Button(onClick = { HistoryManager.guardarSesion(if (nombrePartido.isBlank()) "Partido sin nombre" else nombrePartido); showSaveDialog = false; nombrePartido = "" }) { Text(LanguageManager.s.btnGuardar) } }, dismissButton = { TextButton(onClick = { showSaveDialog = false }) { Text(LanguageManager.s.btnCancelar) } })
    }
}

// --- PANTALLAS SECUNDARIAS ---

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
                Row(
                    Modifier.fillMaxWidth().height(80.dp).selectable(selected = (codigo == LanguageManager.selectedLanguage), onClick = { LanguageManager.setLanguage(context, codigo); SoundManager.feedback(context) }, role = androidx.compose.ui.semantics.Role.RadioButton),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (codigo == LanguageManager.selectedLanguage), onClick = null, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen, unselectedColor = Color.Gray))
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = titulo, fontSize = 18.sp, color = PureWhite, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = desc, fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun ColoresScreen(onBackClick: () -> Unit) {
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
fun HistorialScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(LanguageManager.s.historialTitulo, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth().background(SportGray, RoundedCornerShape(12.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Text(LanguageManager.s.historialSwitch, color = PureWhite, fontSize = 16.sp); Switch(checked = HistoryManager.isEnabled, onCheckedChange = { HistoryManager.isEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen)) }
        Spacer(modifier = Modifier.height(24.dp))
        if (HistoryManager.sesiones.isEmpty()) { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(LanguageManager.s.historialVacio, color = Color.Gray) } } else { LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) { items(HistoryManager.sesiones) { sesion -> SesionCard(sesion) } } }
    }
}

@Composable
fun SesionCard(sesion: SesionPartido) {
    Card(colors = CardDefaults.cardColors(containerColor = SportGray), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(sesion.nombre, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PureWhite)); IconButton(onClick = { HistoryManager.sesiones.remove(sesion) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Gray) } }
            Text(sesion.fecha, style = TextStyle(fontSize = 12.sp, color = Color.Gray))
            HorizontalDivider(color = SportBlack, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) { Column(modifier = Modifier.weight(1f)) { Text("1T", color = ColorManager.btn1Color, fontSize = 12.sp, fontWeight = FontWeight.Bold); Text(sesion.duracion1, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = FontFamily.Monospace) }; Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) { Text("2T", color = ColorManager.btn2Color, fontSize = 12.sp, fontWeight = FontWeight.Bold); Text(sesion.duracion2, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = FontFamily.Monospace) } }
        }
    }
}

@Composable
fun TimeSettingsScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(LanguageManager.s.ajustesTiempoTitulo, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        Text(LanguageManager.s.ajustesTiempoDesc1, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PureWhite); Spacer(modifier = Modifier.height(8.dp))
        Text(LanguageManager.s.ajustesTiempoDesc2, fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp); Spacer(modifier = Modifier.height(40.dp))
        Text(LanguageManager.s.ajustesTiempoLabel, color = NeonBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.border(2.dp, PureWhite, RoundedCornerShape(12.dp)).padding(24.dp)) { BasicTextField(value = TimeManager.secondHalfStartMinute, onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) TimeManager.secondHalfStartMinute = it }, textStyle = TextStyle(color = PureWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.width(100.dp)); Text("min", fontSize = 24.sp, color = Color.Gray, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun SonidosScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val ringtonePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result -> if (result.resultCode == Activity.RESULT_OK) { val uri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI); if (uri != null) { SoundManager.customToneUri = uri; SoundManager.modoActual = ModoSonido.SONIDO } } }
    Column(modifier = Modifier.fillMaxSize().background(SportBlack).padding(top = 80.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Text(LanguageManager.s.sonidosTitulo, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PureWhite)) }
        Spacer(modifier = Modifier.height(40.dp))
        val opciones = listOf(
            Triple(LanguageManager.s.sonidoSilencio, ModoSonido.SILENCIO, LanguageManager.s.sonidoSilencioDesc),
            Triple(LanguageManager.s.sonidoSonido, ModoSonido.SONIDO, LanguageManager.s.sonidoSonidoDesc),
            Triple(LanguageManager.s.sonidoVibracion, ModoSonido.VIBRACION, LanguageManager.s.sonidoVibracionDesc),
            Triple(LanguageManager.s.sonidoAmbos, ModoSonido.AMBOS, LanguageManager.s.sonidoAmbosDesc)
        )
        opciones.forEach { (titulo, modo, descripcion) -> Row(Modifier.fillMaxWidth().height(80.dp).selectable(selected = (modo == SoundManager.modoActual), onClick = { SoundManager.modoActual = modo; SoundManager.feedback(context) }, role = androidx.compose.ui.semantics.Role.RadioButton), verticalAlignment = Alignment.CenterVertically) { RadioButton(selected = (modo == SoundManager.modoActual), onClick = null, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen, unselectedColor = Color.Gray)); Column(modifier = Modifier.padding(start = 16.dp)) { Text(text = titulo, fontSize = 18.sp, color = PureWhite, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(4.dp)); Text(text = descripcion, fontSize = 13.sp, color = Color.Gray) } } }
        Spacer(modifier = Modifier.height(32.dp))
        if (SoundManager.modoActual == ModoSonido.SONIDO || SoundManager.modoActual == ModoSonido.AMBOS) { Button(onClick = { val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply { putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION); putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Selecciona un tono"); putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, SoundManager.customToneUri) }; ringtonePicker.launch(intent) }, colors = ButtonDefaults.buttonColors(containerColor = SportGray), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonGreen); Spacer(modifier = Modifier.width(8.dp)); Text(LanguageManager.s.btnElegirTono, color = PureWhite) } }
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