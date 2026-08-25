package pe.appmobile.pruebayveras.domain.engine

object MotorEco {

    private const val VELOCIDAD_SONIDO = 340f

    fun retardoSegundos(distanciaMetros: Float): Float {
        val distancia = distanciaMetros.coerceAtLeast(0f)
        return (distancia * 2) / VELOCIDAD_SONIDO
    }

    fun intensidad(distanciaMetros: Float, materialAbsorbente: Boolean): Float {
        val distancia = distanciaMetros.coerceAtLeast(0f)
        val base = (100f - distancia * 8f).coerceAtLeast(0f)
        val factor = if (materialAbsorbente) 0.4f else 1.0f
        return base * factor
    }
}
