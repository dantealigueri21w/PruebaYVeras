package pe.appmobile.pruebayveras.domain.model

enum class TipoSuperficie { LISA, RUGOSA }

enum class TipoObstaculo { CARTON, METAL_GRUESO }

enum class Isla(val idIsla: String) {
    MAREA("isla_marea"),
    VIENTO("isla_viento"),
    JARDIN("isla_jardin"),
    CUEVA("isla_cueva"),
    FARO("isla_faro"),
    OLAS("isla_olas"),
    RISCO("isla_risco"),
    IMAN("isla_iman"),
    REFLEJO("isla_reflejo"),
}

enum class DificultadReto { FACIL, MEDIO, DIFICIL }
