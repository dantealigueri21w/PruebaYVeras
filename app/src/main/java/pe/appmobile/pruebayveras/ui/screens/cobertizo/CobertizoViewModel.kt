package pe.appmobile.pruebayveras.ui.screens.cobertizo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.PiezaChirimboloEntity

class CobertizoViewModel(db: AppDatabase) : ViewModel() {
    val piezas: StateFlow<List<PiezaChirimboloEntity>> = db.piezaChirimboloDao().observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
