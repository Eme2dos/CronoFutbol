package com.eme2.cronofutbol.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

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
    val welcomeTitulo: String
    val welcomeDesc: String
    val welcomeBtn: String
}

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
    override val welcomeTitulo = "¡Bienvenido!"
    override val welcomeDesc = "Para que el cronómetro siga funcionando aunque apagues la pantalla, necesitamos permiso para mostrar notificaciones."
    override val welcomeBtn = "Continuar y Habilitar"
}

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
    override val welcomeTitulo = "Welcome!"
    override val welcomeDesc = "To keep the timer running even when you turn off the screen, we need permission to show notifications."
    override val welcomeBtn = "Continue & Enable"
}

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
    override val welcomeTitulo = "Bienvenue !"
    override val welcomeDesc = "Pour que le chronomètre continue de fonctionner écran éteint, nous avons besoin de la permission de notification."
    override val welcomeBtn = "Continuer et Activer"
}

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
    override val welcomeTitulo = "Benvenuto!"
    override val welcomeDesc = "Affinché il cronometro funzioni anche a schermo spento, abbiamo bisogno del permesso per le notifiche."
    override val welcomeBtn = "Continua e Abilita"
}

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
    override val welcomeTitulo = "Willkommen!"
    override val welcomeDesc = "Damit der Timer auch bei ausgeschaltetem Bildschirm weiterläuft, benötigen wir die Benachrichtigungserlaubnis."
    override val welcomeBtn = "Weiter & Aktivieren"
}

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
    override val welcomeTitulo = "Bem-vindo!"
    override val welcomeDesc = "Para manter o cronômetro funcionando com a tela desligada, precisamos de permissão para notificações."
    override val welcomeBtn = "Continuar e Habilitar"
}

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
                    else -> EnStrings
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