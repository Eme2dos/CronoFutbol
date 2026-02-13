package com.eme2.cronofutbol.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eme2.cronofutbol.data.model.SesionPartido
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object HistoryManager {
    var isEnabled by mutableStateOf(true)
    var sesiones = mutableStateListOf<SesionPartido>()
    var fechaSesion: String = ""
    var tempDuracion1: String? by mutableStateOf(null)
    var tempDuracion2: String? by mutableStateOf(null)

    fun iniciarSesion() {
        if (fechaSesion.isEmpty()) {
            fechaSesion = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        }
    }

    fun registrarT1(segundos: Long) {
        tempDuracion1 = formatear(segundos)
    }

    fun registrarT2(segundosTotal: Long) {
        // --- AQUÍ ESTABA EL ERROR ---
        // Antes la app restaba el tiempo inicial. Ahora simplemente coge el tiempo
        // del reloj tal cual está (ej: 45:04) y lo guarda directamente en la tarjeta.
        tempDuracion2 = formatear(segundosTotal)
    }

    fun guardarSesion(nombre: String) {
        if (tempDuracion1 != null) {
            val nuevaSesion = SesionPartido(
                nombre = nombre,
                fecha = if (fechaSesion.isNotEmpty()) fechaSesion else SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                duracion1 = tempDuracion1 ?: "00:00",
                duracion2 = tempDuracion2 ?: "00:00"
            )
            sesiones.add(0, nuevaSesion)
            limpiarTemp()
        }
    }

    fun limpiarTemp() {
        fechaSesion = ""
        tempDuracion1 = null
        tempDuracion2 = null
    }

    private fun formatear(seg: Long): String {
        val m = seg / 60
        val s = seg % 60
        return String.format("%02d:%02d", m, s)
    }
}