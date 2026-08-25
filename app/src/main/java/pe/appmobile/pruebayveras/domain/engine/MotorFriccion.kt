package pe.appmobile.pruebayveras.domain.engine

import pe.appmobile.pruebayveras.domain.model.TipoSuperficie

object MotorFriccion {

    fun coeficiente(superficie: TipoSuperficie): Float = when (superficie) {
        TipoSuperficie.LISA -> 0.1f
        TipoSuperficie.RUGOSA -> 0.6f
    }

    fun distanciaCm(alturaCm: Float, superficie: TipoSuperficie): Float {
        val altura = alturaCm.coerceAtLeast(0f)
        if (altura == 0f) return 0f
        val coeficiente = coeficiente(superficie)
        return (altura * 4f) / (1f + coeficiente * 10f)
    }
}
