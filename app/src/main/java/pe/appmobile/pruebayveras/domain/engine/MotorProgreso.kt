package pe.appmobile.pruebayveras.domain.engine

object MotorProgreso {

    fun islaDesbloqueada(idIsla: String, requisito: String?, completadas: Set<String>): Boolean {
        if (requisito == null) return true
        return requisito in completadas
    }

    fun chirimboloCompleto(piezasConfirmadas: Set<String>): Boolean =
        piezasConfirmadas.size >= 9

    fun actualizarRacha(rachaActual: Int, diasDesdeUltimoJuego: Int): Int = when {
        diasDesdeUltimoJuego == 0 -> rachaActual
        diasDesdeUltimoJuego == 1 -> rachaActual + 1
        else -> 1
    }
}
