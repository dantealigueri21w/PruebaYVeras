package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.InsigniaEntity

@Dao
interface InsigniaDao {
    @Query("SELECT * FROM insignia")
    fun observarTodas(): Flow<List<InsigniaEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(insignias: List<InsigniaEntity>)

    @Update
    suspend fun actualizar(insignia: InsigniaEntity)
}
