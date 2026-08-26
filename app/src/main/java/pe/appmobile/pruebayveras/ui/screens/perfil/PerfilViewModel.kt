package pe.appmobile.pruebayveras.ui.screens.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.PerfilEntity

class PerfilViewModel(private val db: AppDatabase) : ViewModel() {
    fun guardar(alias: String, avatarId: Int) {
        viewModelScope.launch { db.perfilDao().guardar(PerfilEntity(alias = alias, avatarId = avatarId)) }
    }
}
