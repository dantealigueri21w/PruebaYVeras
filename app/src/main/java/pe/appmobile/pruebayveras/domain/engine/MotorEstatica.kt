package pe.appmobile.pruebayveras.domain.engine

object MotorEstatica {

    private const val TOPE_PAPELITOS = 10
    private const val DISTANCIA_BASE_CM = 5

    /**
     * La atracción electrostática cae con la distancia: por cada 5 cm de más sobre la
     * base, un papelito menos (reto_faro_medio ya preguntaba por esto sin tener nunca
     * la variable real).
     */
    fun papelitosAtraidos(frotadas: Int, distanciaCm: Int = DISTANCIA_BASE_CM): Int {
        val frotadasValidas = frotadas.coerceAtLeast(0)
        val base = (frotadasValidas / 3).coerceAtMost(TOPE_PAPELITOS)
        val penalizacion = ((distanciaCm - DISTANCIA_BASE_CM).coerceAtLeast(0)) / 5
        return (base - penalizacion).coerceAtLeast(0)
    }
}
