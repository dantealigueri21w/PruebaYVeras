package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.IntentoEntity
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity

class CuadernoViewModel(private val db: AppDatabase) : ViewModel() {
    val paginas: StateFlow<List<PaginaCuadernoEntity>> = db.paginaCuadernoDao().observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Los datos reales (nunca de ejemplo) detrás de una página: las corridas
     * guardadas de ese reto, en el orden en que se jugaron. */
    fun intentosDe(idReto: String): Flow<List<IntentoEntity>> = db.intentoDao().observarPorReto(idReto)
}
