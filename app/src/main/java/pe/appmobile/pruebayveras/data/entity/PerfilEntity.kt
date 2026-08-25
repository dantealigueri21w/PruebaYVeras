package pe.appmobile.pruebayveras.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 1,
    val alias: String,
    val avatarId: Int,
)
