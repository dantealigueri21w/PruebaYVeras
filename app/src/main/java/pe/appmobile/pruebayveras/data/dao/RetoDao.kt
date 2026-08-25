package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.RetoEntity

@Dao
interface RetoDao {
    @Query("SELECT * FROM reto WHERE idIsla = :idIsla")
    fun observarPorIsla(idIsla: String): Flow<List<RetoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodos(retos: List<RetoEntity>)

    @Update
    suspend fun actualizar(reto: RetoEntity)
}
