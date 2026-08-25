package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insignia")
data class InsigniaEntity(
    @PrimaryKey val idInsignia: String,
    val nombre: String,
    val obtenida: Boolean = false,
    val timestampObtenida: Long? = null,
)
