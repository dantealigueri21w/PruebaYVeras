package pe.appmobile.pruebayveras.domain.engine

object MotorFlotabilidad {

    private const val DENSIDAD_AGUA_DULCE = 1.00f
    private const val DENSIDAD_POR_CUCHARADA = 0.01f
    private const val DENSIDAD_HUEVO = 1.03f

    data class Resultado(val flota: Boolean, val alturaFlotacion: Float)

    fun densidadAgua(cucharadasDeSal: Int): Float {
        val cucharadas = cucharadasDeSal.coerceAtLeast(0)
        return DENSIDAD_AGUA_DULCE + (cucharadas * DENSIDAD_POR_CUCHARADA)
    }

    /**
     * [volumenAguaMl] no entra en la fórmula a propósito: la densidad es una propiedad
     * intensiva, no depende de cuánta agua hay — es la variable señuelo real de esta
     * isla (reto_marea_medio ya la menciona en su texto).
     */
    fun calcular(cucharadasDeSal: Int, volumenAguaMl: Int = 250): Resultado {
        val densidad = densidadAgua(cucharadasDeSal)
        val diferencia = densidad - DENSIDAD_HUEVO
        val flota = diferencia >= 0f
        val altura = if (flota) diferencia * 100f else 0f
        return Resultado(flota = flota, alturaFlotacion = altura)
    }
}
