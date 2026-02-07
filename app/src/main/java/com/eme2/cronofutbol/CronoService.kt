package com.eme2.cronofutbol // <--- REVISA QUE ESTO COINCIDA CON TU PAQUETE

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
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

    // Singleton: Una variable global para acceder al tiempo desde la MainActivity
    companion object {
        var tiempoActualSegundos by mutableStateOf(0L) // Variable observable
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

        return START_STICKY // Si el sistema mata el servicio, intenta revivirlo
    }

    private fun iniciarCrono(minutoInicial: Long) {
        if (estaCorriendo) return

        // Si venimos de 0, aplicamos el minuto inicial
        if (tiempoActualSegundos == 0L && minutoInicial > 0) {
            tiempoActualSegundos = minutoInicial * 60
        }

        estaCorriendo = true
        crearCanalNotificacion()
        startForeground(NOTIFICACION_ID, crearNotificacion("Partido en curso..."))

        timerJob = serviceScope.launch {
            while (estaCorriendo) {
                delay(1000) // Contamos cada segundo
                tiempoActualSegundos++

                // Actualizamos la notificación con el tiempo
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
        stopForeground(STOP_FOREGROUND_DETACH) // Quita la notificación permanente pero mantiene el servicio vivo un rato
    }

    private fun reiniciarCrono() {
        pausarCrono()
        tiempoActualSegundos = 0L
        stopSelf() // Matamos el servicio
    }

    // --- Lógica de Notificaciones ---

    private fun crearNotificacion(contenido: String): Notification {
        return NotificationCompat.Builder(this, CANAL_ID)
            .setContentTitle("CronoFutbol")
            .setContentText(contenido)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history) // Icono por defecto de Android
            .setOnlyAlertOnce(true) // Para que no suene cada segundo
            .setOngoing(true) // El usuario no puede quitarla deslizando
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
                NotificationManager.IMPORTANCE_LOW // Low para que no haga ruido cada segundo
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
}