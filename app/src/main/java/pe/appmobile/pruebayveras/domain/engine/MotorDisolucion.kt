package pe.appmobile.pruebayveras.domain.engine

object MotorDisolucion {

    private const val TIEMPO_MINIMO = 5

    fun tiempoSegundos(temperaturaC: Int): Int {
        val temperatura = temperaturaC.coerceAtLeast(0)
        return (60 - temperatura).coerceAtLeast(TIEMPO_MINIMO)
    }
}
