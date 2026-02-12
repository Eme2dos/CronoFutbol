package com.eme2.cronofutbol.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eme2.cronofutbol.data.model.EventoPartido
import com.eme2.cronofutbol.data.model.TipoEvento
import com.eme2.cronofutbol.service.CronoService

object MatchManager {
    // Configuración
    var isScoreboardEnabled by mutableStateOf(false) // El interruptor de ajustes

    // Datos del Partido en curso
    var equipoLocal by mutableStateOf("Local")
    var equipoVisitante by mutableStateOf("Visitante")
    var estadio by mutableStateOf("")

    var golesLocal by mutableIntStateOf(0)
    var golesVisitante by mutableIntStateOf(0)

    // Lista de eventos en vivo
    var eventos = mutableStateListOf<EventoPartido>()

    fun agregarGol(esLocal: Boolean) {
        if (esLocal) golesLocal++ else golesVisitante++
        registrarEvento(
            tipo = TipoEvento.GOL,
            esLocal = esLocal,
            desc = "Gol de ${if (esLocal) equipoLocal else equipoVisitante}"
        )
    }

    fun quitarGol(esLocal: Boolean) {
        if (esLocal && golesLocal > 0) {
            golesLocal--
            eliminarUltimoEventoGol(esLocal)
        } else if (!esLocal && golesVisitante > 0) {
            golesVisitante--
            eliminarUltimoEventoGol(esLocal)
        }
    }

    private fun registrarEvento(tipo: TipoEvento, esLocal: Boolean, desc: String) {
        // Calculamos el minuto actual basándonos en el CronoService
        val seg = CronoService.tiempoActualSegundos
        val minuto = (seg / 60) + 1 // Minuto 0-59 es minuto 1
        val tiempoStr = "$minuto'"

        eventos.add(0, EventoPartido( // Añadimos al principio para ver el último arriba
            minuto = tiempoStr,
            tipo = tipo,
            equipo = if (esLocal) "LOCAL" else "VISIT",
            descripcion = desc
        ))
    }

    private fun eliminarUltimoEventoGol(esLocal: Boolean) {
        // Busca el último gol de este equipo y lo borra (deshacer)
        val evento = eventos.find { it.tipo == TipoEvento.GOL && it.equipo == (if(esLocal) "LOCAL" else "VISIT") }
        if (evento != null) {
            eventos.remove(evento)
        }
    }

    fun resetMatch() {
        equipoLocal = "Local"
        equipoVisitante = "Visitante"
        estadio = ""
        golesLocal = 0
        golesVisitante = 0
        eventos.clear()
    }
}