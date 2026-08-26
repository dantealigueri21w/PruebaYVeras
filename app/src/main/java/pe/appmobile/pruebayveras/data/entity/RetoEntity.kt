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
    /** El valor real que debe dar el motor de esta isla para considerarse logrado —
     * verificado contra la fórmula real del motor, nunca puesto a ojo. */
    val valorObjetivo: Float,
    /** Tolerancia alrededor de [valorObjetivo]: más ancha en fácil, más angosta en
     * difícil — calculada a mano por reto porque cada motor devuelve resultados en
     * una escala distinta (0-17 en Marea, 0-6 en Faro, etc.). */
    val margenObjetivo: Float,
    val completado: Boolean = false,
)
