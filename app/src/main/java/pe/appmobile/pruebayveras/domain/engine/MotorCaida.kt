package pe.appmobile.pruebayveras.domain.engine

import kotlin.math.sqrt

object MotorCaida {

    private const val GRAVEDAD = 9.8f
    private const val FACTOR_PARACAIDAS = 3.5f

    fun calcular(alturaMetros: Float, tieneParacaidas: Boolean): Float {
        val altura = alturaMetros.coerceAtLeast(0f)
        val tiempoBase = sqrt(2 * altura / GRAVEDAD)
        return if (tieneParacaidas) tiempoBase * FACTOR_PARACAIDAS else tiempoBase
    }
}
