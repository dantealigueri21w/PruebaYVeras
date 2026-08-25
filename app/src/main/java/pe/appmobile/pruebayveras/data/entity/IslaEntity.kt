package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "isla")
data class IslaEntity(
    @PrimaryKey val idIsla: String,
    val nombre: String,
    val fenomeno: String,
    val requisitoDesbloqueo: String?,
    val completada: Boolean = false,
)
