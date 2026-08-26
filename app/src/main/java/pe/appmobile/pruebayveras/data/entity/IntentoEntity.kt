package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intento")
data class IntentoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val idReto: String,
    val variableCambiada: String,
    val valorProbado: String,
    val resultadoReal: Float,
    val logrado: Boolean,
    val timestamp: Long,
)
