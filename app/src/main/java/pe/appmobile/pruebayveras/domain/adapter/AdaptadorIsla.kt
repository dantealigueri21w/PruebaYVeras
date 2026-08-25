package pe.appmobile.pruebayveras.domain.adapter

import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable

interface AdaptadorIsla {
    /** Valores de control por defecto para todas las variables que puede tener esta isla. */
    val variablesBase: List<Variable>

    /** Calcula el resultado real (el número que se compara y se grafica) para un montaje. */
    fun calcular(montaje: Montaje): Float
}
