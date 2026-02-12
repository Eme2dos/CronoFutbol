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
import com.eme2.cronofutbol.presentation.MainActivity // OJO: Apunta a la activity del reloj
import com.eme2.cronofutbol.R
import kotlinx.coroutines.*

class CronoService : Service() {

    companion object {
        var tiempoActualSegundos by mutableStateOf(0L)
        var estaCorriendo by mutableStateOf(false)
        var etapaActual by mutableStateOf(1)

        const val CHANNEL_ID = "CronoFutbolWearChannel"
        const val NOTIFICATION_ID = 101
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
        if (!estaCorriendo) stopSelf()
    }

    private fun crearNotificacion(): Notification {
        crearCanalNotificacion()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val m = tiempoActualSegundos / 60
        val s = tiempoActualSegundos % 60
        val texto = String.format("%02d:%02d", m, s)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Crono: $texto")
            .setSmallIcon(android.R.drawable.ic_media_play) // Icono genérico por ahora
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun actualizarNotificacion() {
        getSystemService(NotificationManager::class.java).notify(NOTIFICATION_ID, crearNotificacion())
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Crono Wear", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob?.cancel()
    }
}