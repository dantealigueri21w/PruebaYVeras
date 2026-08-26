package pe.appmobile.pruebayveras.domain.engine

object MotorDisolucion {

    private const val TIEMPO_MINIMO = 5
    private const val AZUCAR_BASE_G = 10

    /**
     * Más azúcar para la misma cantidad de agua tarda más en disolverse del todo
     * (reto_olas_medio ya preguntaba por esto sin tener nunca la variable real).
     */
    fun tiempoSegundos(temperaturaC: Int, cantidadAzucarG: Int = AZUCAR_BASE_G): Int {
        val temperatura = temperaturaC.coerceAtLeast(0)
        val extraAzucar = (cantidadAzucarG - AZUCAR_BASE_G).coerceAtLeast(0)
        return ((60 - temperatura) + extraAzucar).coerceAtLeast(TIEMPO_MINIMO)
    }
}
