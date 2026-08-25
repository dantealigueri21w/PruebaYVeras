package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pieza_chirimbolo")
data class PiezaChirimboloEntity(
    @PrimaryKey val idPieza: String,
    val idIsla: String,
    val nombre: String,
    val confirmada: Boolean = false,
)
