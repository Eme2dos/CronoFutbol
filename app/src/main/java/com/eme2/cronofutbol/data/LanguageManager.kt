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
    val menuAyuda: String
    val menuQuitarAnuncios: String // NUEVO
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
    val ayudaTitulo: String
    val ayudaIntro: String
    val ayudaPaso1: String
    val ayudaPaso2: String
    val ayudaPaso3: String
    val ayudaNota: String
    val ayudaBateriaTitulo: String
    val ayudaBateriaDesc: String
    val btnWeb: String
}

object EsStrings : AppStrings {
    override val menuTitulo = "MENÚ"
    override val menuHistorial = "Historial"
    override val menuSonidos = "Sonidos"
    override val menuTiempo = "Ajustar Tiempo"
    override val menuColores = "Colores"
    override val menuIdioma = "Idioma"
    override val menuAyuda = "Ayuda"
    override val menuQuitarAnuncios = "Quitar Anuncios"
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
    override val ayudaTitulo = "AYUDA"
    override val ayudaIntro = "Guía rápida para usar CronoFutbol:"
    override val ayudaPaso1 = "1. Pulsa '1 TIEMPO' para iniciar el partido desde 00:00."
    override val ayudaPaso2 = "2. Al terminar la primera parte, pausa el crono. Pulsa '2 TIEMPO' para iniciar la segunda parte (por defecto desde el min 45)."
    override val ayudaPaso3 = "3. Puedes configurar el minuto de inicio del 2º tiempo en el menú 'Ajustar Tiempo'."
    override val ayudaNota = "Nota: El cronómetro sigue funcionando aunque cierres la app o apagues la pantalla gracias a la notificación activa."
    override val ayudaBateriaTitulo = "Gestión Inteligente de Batería"
    override val ayudaBateriaDesc = "Esta app usa fondo negro puro para ahorrar energía en pantallas OLED. Además, si pausas el cronómetro y cierras la app, el proceso se detiene completamente para un consumo 0%."
    override val btnWeb = "Visitar web oficial"
}

object EnStrings : AppStrings {
    override val menuTitulo = "MENU"
    override val menuHistorial = "History"
    override val menuSonidos = "Sounds"
    override val menuTiempo = "Time Settings"
    override val menuColores = "Colors"
    override val menuIdioma = "Language"
    override val menuAyuda = "Help"
    override val menuQuitarAnuncios = "Remove Ads"
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
    override val ayudaTitulo = "HELP"
    override val ayudaIntro = "Quick guide to using Football Timer:"
    override val ayudaPaso1 = "1. Press '1st HALF' to start the match from 00:00."
    override val ayudaPaso2 = "2. When the first half ends, pause. Press '2nd HALF' to start the second half (default from min 45)."
    override val ayudaPaso3 = "3. You can configure the 2nd half start minute in 'Time Settings'."
    override val ayudaNota = "Note: The timer keeps running even if you close the app or turn off the screen thanks to the active notification."
    override val ayudaBateriaTitulo = "Smart Battery Management"
    override val ayudaBateriaDesc = "This app uses a pure black background to save power on OLED screens. Also, if you pause the timer and close the app, the process stops completely for 0% consumption."
    override val btnWeb = "Visit official website"
}

object FrStrings : AppStrings {
    override val menuTitulo = "MENU"
    override val menuHistorial = "Historique"
    override val menuSonidos = "Sons"
    override val menuTiempo = "Réglage Temps"
    override val menuColores = "Couleurs"
    override val menuIdioma = "Langue"
    override val menuAyuda = "Aide"
    override val menuQuitarAnuncios = "Supprimer les pubs"
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
    override val ayudaTitulo = "AIDE"
    override val ayudaIntro = "Guide rapide :"
    override val ayudaPaso1 = "1. Appuyez sur '1ère MI-TEMPS' pour commencer à 00:00."
    override val ayudaPaso2 = "2. Appuyez sur '2ème MI-TEMPS' pour la seconde période (par défaut min 45)."
    override val ayudaPaso3 = "3. Configurez le début de la 2ème mi-temps dans 'Réglage Temps'."
    override val ayudaNota = "Note : Le chrono continue de tourner grâce à la notification."
    override val ayudaBateriaTitulo = "Gestion Intelligente de la Batterie"
    override val ayudaBateriaDesc = "Cette application utilise un fond noir pur pour économiser l'énergie (OLED). De plus, si vous mettez en pause et fermez l'app, le processus s'arrête complètement (0% consommation)."
    override val btnWeb = "Visiter le site web"
}

object ItStrings : AppStrings {
    override val menuTitulo = "MENU"