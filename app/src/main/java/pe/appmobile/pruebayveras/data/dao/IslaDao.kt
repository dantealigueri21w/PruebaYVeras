package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.IslaEntity

@Dao
interface IslaDao {
    @Query("SELECT * FROM isla")
    fun observarTodas(): Flow<List<IslaEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(islas: List<IslaEntity>)

    @Update
    suspend fun actualizar(isla: IslaEntity)
}
