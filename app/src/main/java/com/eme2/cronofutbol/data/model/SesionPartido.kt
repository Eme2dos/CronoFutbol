package com.eme2.cronofutbol.data.model

data class SesionPartido(
    val id: Long = System.currentTimeMillis(),
    val nombre: String, // Nombre del partido o torneo
    val estadio: String = "", // Nuevo campo
    val fecha: String,
    val duracion1: String,
    val duracion2: String,
    // Nuevos campos para el modo marcador (con valores por defecto para compatibilidad)
    val marcadorLocal: Int? = null,
    val marcadorVisitante: Int? = null,
    val nombreLocal: String = "Local",
    val nombreVisitante: String = "Visitante",
    val eventos: List<EventoPartido> = emptyList()
)