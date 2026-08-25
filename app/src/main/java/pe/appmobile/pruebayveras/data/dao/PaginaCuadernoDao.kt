package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity

@Dao
interface PaginaCuadernoDao {
    @Query("SELECT * FROM pagina_cuaderno ORDER BY timestamp ASC")
    fun observarTodas(): Flow<List<PaginaCuadernoEntity>>

    @Insert
    suspend fun guardar(pagina: PaginaCuadernoEntity)
}
