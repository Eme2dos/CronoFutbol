package com.eme2.cronofutbol

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CronoService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var timerJob: Job? = null

    // Variables de tiempo interno
    private var tiempoInicioMillis = 0L
    private var tiempoAcumuladoMillis = 0L

    companion object {
        var tiempoActualSegundos by mutableStateOf(0L)
        var estaCorriendo by mutableStateOf(false)

        // 0 = Inicio/Reset, 1 = 1er Tiempo, 2 = 2do Tiempo
        var etapaActual by mutableStateOf(0)

        const val CANAL_ID = "CronoFutbolCanal"
        const val NOTIFICACION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "INICIAR" -> {
                val minutoInicial = intent.getLongExtra("MINUTO_INICIO", 0L)
                val etapa = intent.getIntExtra("ETAPA", 1)
                iniciarCrono(minutoInicial, etapa)
            }
            "PAUSAR" -> pausarCrono()
            "REINICIAR" -> reiniciarCrono()
        }
        return START_STICKY
    }

    private fun iniciarCrono(minutoInicial: Long, etapa: Int) {
        if (estaCorriendo) return

        // --- CORRECCIÓN AQUÍ ---
        // Detectamos si estamos cambiando de etapa (ej: del 1 al 2)
        // o si es el arranque inicial (etapaActual es 0).
        // Si hay cambio, FORZAMOS el tiempo acumulado al minuto de inicio (ej: 45).
        if (etapa != etapaActual || tiempoAcumuladoMillis == 0L) {
            tiempoAcumuladoMillis = minutoInicial * 60 * 1000L
        }

        // Actualizamos la etapa actual
        etapaActual = etapa

        // Marcamos el tiempo de referencia del sistema
        tiempoInicioMillis = System.currentTimeMillis()
        estaCorriendo = true

        crearCanalNotificacion()
        val textoEtapa = if (etapa == 1) "1er Tiempo" else "2do Tiempo"
        startForeground(NOTIFICACION_ID, crearNotificacionBase("En juego ($textoEtapa)...").build())

        timerJob = serviceScope.launch {
            while (estaCorriendo) {
                // Calculamos tiempo real transcurrido
                val tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMillis + tiempoAcumuladoMillis
                tiempoActualSegundos = tiempoTranscurrido / 1000

                // Actualizamos notificación
                val texto = formatearTiempo(tiempoActualSegundos)
                actualizarNotificacion("$textoEtapa: $texto")

                delay(1000)
            }
        }
    }

    private fun pausarCrono() {
        if (!estaCorriendo) return

        estaCorriendo = false
        // Guardamos el progreso actual
        tiempoAcumuladoMillis += System.currentTimeMillis() - tiempoInicioMillis
        timerJob?.cancel()

        actualizarNotificacion("Partido Pausado")
        stopForeground(false)
    }

    private fun reiniciarCrono() {
        estaCorriendo = false
        timerJob?.cancel()

        // Reseteo total
        etapaActual = 0
        tiempoActualSegundos = 0L
        tiempoAcumuladoMillis = 0L
        tiempoInicioMillis = 0L

        // Limpieza de notificación
        val manager = getSystemService(NotificationManager::class.java)
        manager.cancel(NOTIFICACION_ID)

        stopForeground(true)
        stopSelf()
    }

    private fun crearNotificacionBase(contenido: String): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CANAL_ID)
            .setContentTitle("CronoFutbol")
            .setContentText(contenido)
            .setSmallIcon(R.drawable.ic_notificacion_crono)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
    }

    private fun actualizarNotificacion(contenido: String) {
        if (!estaCorriendo && contenido != "Partido Pausado") return
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICACION_ID, crearNotificacionBase(contenido).build())
    }

    private fun formatearTiempo(segundos: Long): String {
        val min = segundos / 60
        val seg = segundos % 60
        return String.format("%02d:%02d", min, seg)
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID, "Cronómetro Futbol", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
}