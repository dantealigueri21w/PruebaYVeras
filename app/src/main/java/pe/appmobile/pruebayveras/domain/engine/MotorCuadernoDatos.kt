package pe.appmobile.pruebayveras.domain.engine

enum class Tendencia { SUBE, BAJA, NO_CAMBIA }

object MotorCuadernoDatos {

    private const val MARGEN_IGUALDAD = 0.5f

    fun tendenciaReal(datos: List<Float>): Tendencia {
        if (datos.size < 2) return Tendencia.NO_CAMBIA

        val diferencias = datos.zipWithNext { a, b -> b - a }
        val todasSuben = diferencias.all { it > MARGEN_IGUALDAD }
        val todasBajan = diferencias.all { it < -MARGEN_IGUALDAD }

        return when {
            todasSuben -> Tendencia.SUBE
            todasBajan -> Tendencia.BAJA
            else -> Tendencia.NO_CAMBIA
        }
    }

    fun conclusionEsCorrecta(datos: List<Float>, conclusionDelNino: Tendencia): Boolean =
        tendenciaReal(datos) == conclusionDelNino
}
