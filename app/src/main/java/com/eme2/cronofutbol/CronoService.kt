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

    // Variables para calcular el tiempo exacto sin gastar CPU contando de 1 en 1
    private var tiempoInicioMillis = 0L
    private var tiempoAcumuladoMillis = 0L

    companion object {
        var tiempoActualSegundos by mutableStateOf(0L)
        var estaCorriendo by mutableStateOf(false)
        const val CANAL_ID = "CronoFutbolCanal"
        const val NOTIFICACION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "INICIAR" -> {
                val minutoInicial = intent.getLongExtra("MINUTO_INICIO", 0L)
                iniciarCrono(minutoInicial)
            }
            "PAUSAR" -> pausarCrono()
            "REINICIAR" -> reiniciarCrono()
        }
        return START_STICKY
    }

    private fun iniciarCrono(minutoInicial: Long) {
        if (estaCorriendo) return

        // Configuración inicial eficiente
        if (tiempoAcumuladoMillis == 0L && minutoInicial > 0) {
            tiempoAcumuladoMillis = minutoInicial * 60 * 1000L
        }

        // Marcamos la hora exacta de inicio del sistema
        tiempoInicioMillis = System.currentTimeMillis()
        estaCorriendo = true

        crearCanalNotificacion()
        // Iniciamos el servicio en primer plano inmediatamente
        startForeground(NOTIFICACION_ID, crearNotificacionBase("Partido en curso...").build())

        // Corrutina optimizada: Solo actualiza la UI, no hace cálculos pesados
        timerJob = serviceScope.launch {
            while (estaCorriendo) {
                // Calculamos la diferencia de tiempo (Matemáticas simples = Menos batería)
                val tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioMillis + tiempoAcumuladoMillis
                tiempoActualSegundos = tiempoTranscurrido / 1000

                // Actualizamos notificación
                val texto = formatearTiempo(tiempoActualSegundos)
                actualizarNotificacion("Tiempo: $texto")

                delay(1000) // Esperamos 1 segundo
            }
        }
    }

    private fun pausarCrono() {
        if (!estaCorriendo) return

        estaCorriendo = false
        // Guardamos lo que llevamos cronometrado
        tiempoAcumuladoMillis += System.currentTimeMillis() - tiempoInicioMillis
        timerJob?.cancel()

        actualizarNotificacion("Partido Pausado")

        // Dejamos de ser servicio prioritario pero mantenemos la notificacion (false)
        // Esto permite que el sistema ahorre batería al no tener que garantizar recursos críticos
        stopForeground(false)
    }

    private fun reiniciarCrono() {
        estaCorriendo = false
        timerJob?.cancel()

        // Reseteamos variables a 0
        tiempoActualSegundos = 0L
        tiempoAcumuladoMillis = 0L
        tiempoInicioMillis = 0L

        // Eliminamos la notificación agresivamente
        val manager = getSystemService(NotificationManager::class.java)
        manager.cancel(NOTIFICACION_ID)

        // Matamos el servicio y removemos todo rastro
        stopForeground(true)
        stopSelf()
    }

    // --- Optimización de Notificaciones ---

    // Creamos el "Builder" una sola vez para no gastar memoria recreándolo cada segundo
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

        val notificationManager = getSystemService(NotificationManager::class.java)
        // Reutilizamos el builder base y solo cambiamos el texto
        val notificacion = crearNotificacionBase(contenido).build()
        notificationManager.notify(NOTIFICACION_ID, notificacion)
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