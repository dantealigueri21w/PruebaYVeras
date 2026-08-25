package pe.appmobile.pruebayveras.domain.engine

object MotorEstatica {

    private const val TOPE_PAPELITOS = 10

    fun papelitosAtraidos(frotadas: Int): Int {
        val frotadasValidas = frotadas.coerceAtLeast(0)
        return (frotadasValidas / 3).coerceAtMost(TOPE_PAPELITOS)
    }
}
