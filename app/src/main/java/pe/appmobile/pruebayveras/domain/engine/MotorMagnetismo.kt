package pe.appmobile.pruebayveras.domain.engine

import pe.appmobile.pruebayveras.domain.model.TipoObstaculo

object MotorMagnetismo {

    private const val GROSOR_LIMITE_MM = 5

    data class Resultado(val clipSeMueve: Boolean)

    fun calcular(grosorMm: Int, obstaculo: TipoObstaculo): Resultado {
        val grosor = grosorMm.coerceAtLeast(0)
        // El material no metálico casi no influye a propósito: la lección de esta
        // isla es que la distancia importa mucho más que el tipo de obstáculo.
        return Resultado(clipSeMueve = grosor <= GROSOR_LIMITE_MM)
    }
}
