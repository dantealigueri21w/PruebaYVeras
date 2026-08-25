package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reto")
data class RetoEntity(
    @PrimaryKey val idReto: String,
    val idIsla: String,
    val dificultad: String,
    val textoCorazonada: String,
    val variableIndependiente: String,
    val completado: Boolean = false,
)
