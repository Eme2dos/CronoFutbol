package com.eme2.cronofutbol.data.model

enum class TipoEvento { GOL, TARJETA_AMARILLA, TARJETA_ROJA, INICIO, FIN }

data class EventoPartido(
    val minuto: String, // Ej: "12'", "45+2'"
    val tipo: TipoEvento,
    val equipo: String, // "Local" o "Visitante"
    val descripcion: String // Ej: "Gol de Messi"
)