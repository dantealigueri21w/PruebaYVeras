package pe.appmobile.pruebayveras.ui.navigation

object Rutas {
    const val ARCHIPIELAGO = "archipielago"
    const val ISLA = "isla/{idIsla}"
    const val COBERTIZO = "cobertizo"
    const val CUADERNO = "cuaderno"
    const val PERFIL = "perfil"
    const val AJUSTES = "ajustes"

    fun isla(idIsla: String) = "isla/$idIsla"
}
