package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagina_cuaderno")
data class PaginaCuadernoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val idReto: String,
    val resultadoReal: Float,
    val timestamp: Long,
)
