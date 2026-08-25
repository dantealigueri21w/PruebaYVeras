package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.IntentoEntity

@Dao
interface IntentoDao {
    @Query("SELECT * FROM intento WHERE idReto = :idReto ORDER BY timestamp ASC")
    fun observarPorReto(idReto: String): Flow<List<IntentoEntity>>

    @Insert
    suspend fun guardar(intento: IntentoEntity)
}
