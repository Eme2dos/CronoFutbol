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

    // Singleton: Variables globales
    companion object {
        var tiempoActualSegundos by mutableStateOf(0L)
        var estaCorriendo by mutableStateOf(false)
        const val CANAL_ID = "CronoFutbolCanal"
        const val NOTIFICACION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val accion = intent?.action

        when (accion) {
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

        if (tiempoActualSegundos == 0L && minutoInicial > 0) {
            tiempoActualSegundos = minutoInicial * 60
        }

        estaCorriendo = true
        crearCanalNotificacion()
        startForeground(NOTIFICACION_ID, crearNotificacion("Partido en curso..."))

        timerJob = serviceScope.launch {
            while (estaCorriendo) {
                delay(1000)
                tiempoActualSegundos++

                val minutos = tiempoActualSegundos / 60
                val segundos = tiempoActualSegundos % 60
                val textoTiempo = String.format("%02d:%02d", minutos, segundos)
                actualizarNotificacion("Tiempo: $textoTiempo")
            }
        }
    }

    private fun pausarCrono() {
        estaCorriendo = false
        timerJob?.cancel()
        actualizarNotificacion("Partido Pausado")
        // STOP_FOREGROUND_DETACH: Deja la notificación visible pero quita el servicio de primer plano
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun reiniciarCrono() {
        // 1. Paramos todo
        estaCorriendo = false
        timerJob?.cancel()
        tiempoActualSegundos = 0L

        // 2. IMPORTANTE: STOP_FOREGROUND_REMOVE
        // Esto le dice al sistema: "Deja de ser servicio importante Y BORRA la notificación"
        stopForeground(STOP_FOREGROUND_REMOVE)

        // 3. Matamos el servicio
        stopSelf()
    }

    // --- Lógica de Notificaciones ---

    private fun crearNotificacion(contenido: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CANAL_ID)
            .setContentTitle("CronoFutbol")
            .setContentText(contenido)
            // AQUÍ EL CAMBIO: Usamos el icono vectorial que acabas de crear
            // Si te da error en rojo, asegúrate de haber creado el icono en el Paso 1
            .setSmallIcon(R.drawable.ic_notificacion_crono)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun actualizarNotificacion(contenido: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICACION_ID, crearNotificacion(contenido))
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                "Cronómetro Futbol",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
}