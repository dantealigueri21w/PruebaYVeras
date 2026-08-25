package pe.appmobile.pruebayveras.domain.engine

object MotorGerminacion {

    private const val CRECIMIENTO_DIARIO_SIN_LUZ = 0.4f
    private const val CRECIMIENTO_DIARIO_CON_LUZ = 1.2f

    fun calcular(tieneAgua: Boolean, tieneLuz: Boolean, dias: Int): Float {
        if (!tieneAgua) return 0f
        val diasValidos = dias.coerceAtLeast(0)
        val crecimientoDiario = if (tieneLuz) CRECIMIENTO_DIARIO_CON_LUZ else CRECIMIENTO_DIARIO_SIN_LUZ
        return crecimientoDiario * diasValidos
    }
}
