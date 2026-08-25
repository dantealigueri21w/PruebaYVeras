package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racha")
data class RachaEntity(
    @PrimaryKey val id: Int = 1,
    val diasSeguidos: Int = 0,
    val ultimoDiaJugado: Long,
)
