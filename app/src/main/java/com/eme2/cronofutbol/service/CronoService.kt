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
    // Usamos Dispatchers.Main para poder actualizar variables de estado de Compose de forma segura
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
        // START_NOT_STICKY: Si el sistema mata el servicio por falta de memoria,
        // no lo reinicia automáticamente. Esto ahorra batería si Android decide que necesita recursos.
        // Si prefieres máxima seguridad de que no se cierre, usa START_STICKY.
        return START_NOT_STICKY
    }

    private fun iniciarCrono(minutoInicio: Long, etapa: Int) {
        if (estaCorriendo) return

        if (tiempoActualSegundos == 0L) {
            tiempoActualSegundos = minutoInicio * 60
        }

        etapaActual = etapa
        estaCorriendo = true
        startForeground(NOTIFICATION_ID, crearNotificacion())

        // OPTIMIZACIÓN: Cancelamos cualquier trabajo previo para no tener duplicados
        serviceJob?.cancel()
        serviceJob = serviceScope.launch {
            while (isActive && estaCorriendo) {
                delay(1000L) // Dormimos 1 segundo exacto. Consumo de CPU mínimo.
                tiempoActualSegundos++
                actualizarNotificacion()
            }
        }
    }

    private fun pausarCrono() {
        estaCorriendo = false
        // OPTIMIZACIÓN CRÍTICA: Al cancelar el Job, la corrutina deja de consumir CPU inmediatamente.
        serviceJob?.cancel()
        actualizarNotificacion()
    }

    private fun reiniciarCrono() {
        estaCorriendo = false
        serviceJob?.cancel()
        tiempoActualSegundos = 0L
        etapaActual = 1
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf() // Mata el servicio completamente
    }

    // OPTIMIZACIÓN: Si el usuario cierra la app desde la multitarea
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // Si el crono NO está corriendo (está pausado o a cero), matamos el servicio para no gastar batería.
        if (!estaCorriendo) {
            stopSelf()
        }
        // Si está corriendo, NO lo matamos, porque el usuario espera que siga contando en 2º plano.
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

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CronoFutbol: $tiempoTexto")
            .setContentText(estado)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Si tienes icono propio, pon R.drawable.logo_app
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
        serviceJob?.cancel() // Limpieza final de seguridad
    }
}