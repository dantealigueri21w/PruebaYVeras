package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity

class CuadernoViewModel(db: AppDatabase) : ViewModel() {
    val paginas: StateFlow<List<PaginaCuadernoEntity>> = db.paginaCuadernoDao().observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
