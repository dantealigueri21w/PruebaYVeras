package pe.appmobile.pruebayveras.ui.screens.archipielago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.IslaEntity
import pe.appmobile.pruebayveras.data.repository.CienciaLabRepository

class ArchipielagoViewModel(db: AppDatabase) : ViewModel() {
    private val repository = CienciaLabRepository(db)

    init {
        viewModelScope.launch { repository.sembrarSiEsPrimeraVez() }
    }

    val islas: StateFlow<List<IslaEntity>> = db.islaDao().observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
