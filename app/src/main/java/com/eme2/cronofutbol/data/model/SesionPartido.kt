package com.eme2.cronofutbol.data.model

data class SesionPartido(
    val id: Long = System.currentTimeMillis(),
    val nombre: String,
    val fecha: String,
    val duracion1: String,
    val duracion2: String
)