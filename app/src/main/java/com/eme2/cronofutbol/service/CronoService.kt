package com.eme2.cronofutbol.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import com.eme2.cronofutbol.MainActivity
import com.eme2.cronofutbol.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CronoService : Service() {

    companion object {
        var tiempoActualSegundos by mutableStateOf(0L)
        var estaCorriendo by mutableStateOf(false)
        var etapaActual by mutableStateOf(1)

        const val CHANNEL_ID = "CronoFutbolChannel"
        const val NOTIFICATION_ID = 1
    }

    private var serviceJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "INICIAR" -> {
                val minInicio = intent.getLongExtra("MINUTO_INICIO", 0L)
                val etapa = intent.getIntExtra("ETAPA", 1)
                iniciarCrono(minInicio, etapa)
            }
            "PAUSAR" -> pausarCrono()
            "REINICIAR" -> reiniciarCrono()
        }
        return START_NOT_STICKY
    }

    private fun iniciarCrono(minutoInicio: Long, etapa: Int) {
        if (estaCorriendo) return

        // --- CORRECCIÓN DEL ERROR ---
        // Antes solo actualizábamos si tiempoActualSegundos == 0L.
        // Ahora actualizamos si es 0 (inicio partido) O SI CAMBIAMOS DE ETAPA (del 1º al 2º).
        // Si estamos en la etapa 2 y pausamos/reanudamos, etapa == etapaActual, así que NO reinicia.
        if (tiempoActualSegundos == 0L || etapa != etapaActual) {
            tiempoActualSegundos = minutoInicio * 60
        }

        etapaActual = etapa
        estaCorriendo = true
        startForeground(NOTIFICATION_ID, crearNotificacion())

        serviceJob?.cancel()
        serviceJob = serviceScope.launch {
            while (isActive && estaCorriendo) {
                delay(1000L)
                tiempoActualSegundos++
                actualizarNotificacion()
            }
        }
    }

    private fun pausarCrono() {
        estaCorriendo = false
        serviceJob?.cancel()
        actualizarNotificacion()
    }

    private fun reiniciarCrono() {
        estaCorriendo = false
        serviceJob?.cancel()
        tiempoActualSegundos = 0L
        etapaActual = 1
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!estaCorriendo) {
            stopSelf()
        }
    }

    private fun crearNotificacion(): Notification {
        crearCanalNotificacion()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val minutos = tiempoActualSegundos / 60
        val segundos = tiempoActualSegundos % 60
        val tiempoTexto = String.format("%02d:%02d", minutos, segundos)
        val estado = if (estaCorriendo) "En juego - ${etapaActual}T" else "Pausado"

        // Definimos el color verde neón para el acento de la notificación
        val colorNeon = 0xFF00E676.toInt()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CronoFutbol: $tiempoTexto")
            .setContentText(estado)
            // CAMBIO 1: Usamos el nuevo icono de silueta transparente
            .setSmallIcon(R.drawable.ic_notification_crono)
            // CAMBIO 2: Le damos color al icono y al texto del sistema
            .setColor(colorNeon)
            // CAMBIO 3: Esto asegura que se vea bien en todas las versiones
            .setColorized(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun actualizarNotificacion() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, crearNotificacion())
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cronómetro Activo",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob?.cancel()
    }
}