package pe.appmobile.pruebayveras.data.seed

import pe.appmobile.pruebayveras.data.entity.IslaEntity

object SemillaIslas {
    val islas = listOf(
        IslaEntity("isla_marea", "Isla de la Marea", "densidad y flotabilidad", requisitoDesbloqueo = null),
        IslaEntity("isla_viento", "Isla del Viento", "resistencia del aire", requisitoDesbloqueo = null),
        IslaEntity("isla_jardin", "Isla del Jardín", "germinación", requisitoDesbloqueo = "isla_marea"),
        IslaEntity("isla_risco", "Isla del Risco", "fricción", requisitoDesbloqueo = "isla_marea"),
        IslaEntity("isla_cueva", "Isla de la Cueva", "sonido y eco", requisitoDesbloqueo = "isla_viento"),
        IslaEntity("isla_faro", "Isla del Faro", "electricidad estática", requisitoDesbloqueo = "isla_viento"),
        IslaEntity("isla_olas", "Isla de las Olas", "disolución", requisitoDesbloqueo = "isla_jardin"),
        IslaEntity("isla_iman", "Isla del Imán", "magnetismo", requisitoDesbloqueo = "isla_risco"),
        IslaEntity("isla_reflejo", "Isla del Reflejo", "absorción de luz y calor", requisitoDesbloqueo = "isla_cueva"),
    )
}
