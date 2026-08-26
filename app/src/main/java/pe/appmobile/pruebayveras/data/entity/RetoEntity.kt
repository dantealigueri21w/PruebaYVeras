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
    /** "SUBE", "BAJA" o "NO_CAMBIA" — la meta direccional que se muestra antes de
     * tocar nada, nunca un número exacto que cazar. */
    val direccionEsperada: String,
    /** Dato científico real, la tarjeta que se gana al terminar este reto en una
     * prueba justa — no una recompensa genérica, texto propio de este reto. */
    val datoCientifico: String,
    val completado: Boolean = false,
)
