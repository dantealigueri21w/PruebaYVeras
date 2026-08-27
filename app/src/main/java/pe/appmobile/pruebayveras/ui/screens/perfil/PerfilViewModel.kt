package pe.appmobile.pruebayveras.ui.screens.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.PerfilEntity

class PerfilViewModel(private val db: AppDatabase) : ViewModel() {
    val perfil: StateFlow<PerfilEntity?> = db.perfilDao().observar()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun guardar(alias: String, avatarId: Int) {
        viewModelScope.launch { db.perfilDao().guardar(PerfilEntity(alias = alias, avatarId = avatarId)) }
    }
}
