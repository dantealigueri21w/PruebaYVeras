package pe.appmobile.pruebayveras.ui.testutil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

/**
 * Crea un ViewModel dentro de un [ViewModelStore] real, para poder cerrarlo de verdad
 * al final de un test con `store.clear()` (público). `ViewModel.clear()` es `internal`
 * en esta versión de la librería y no se puede llamar directo desde el módulo de test —
 * pasar por `ViewModelStore`/`ViewModelProvider` es el mismo camino que usa Android en
 * producción para destruir un ViewModel cuando su dueño (una Activity, un
 * `NavBackStackEntry`) desaparece.
 *
 * Sin cerrar el ViewModel, su `viewModelScope` sigue vivo después de terminar el test
 * (el `init` que siembra la base, o el `stateIn` que observa un DAO) y puede colarse en
 * el siguiente test cuando toda la suite corre junta, tocando una base de datos en
 * memoria que ya no existe.
 */
fun <T : ViewModel> viewModelDeTest(store: ViewModelStore, clase: Class<T>, crear: () -> T): T {
    val fabrica = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = crear() as VM
    }
    return ViewModelProvider(store, fabrica)[clase]
}
