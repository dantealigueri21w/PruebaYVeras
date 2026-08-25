package pe.appmobile.pruebayveras.domain.engine

object MotorAbsorcionLuz {

    private const val FACTOR_OSCURO = 0.8f
    private const val FACTOR_CLARO = 0.3f

    fun temperaturaGanada(esOscuro: Boolean, minutosAlSol: Int): Float {
        val minutos = minutosAlSol.coerceAtLeast(0)
        val factor = if (esOscuro) FACTOR_OSCURO else FACTOR_CLARO
        return minutos * factor
    }
}
