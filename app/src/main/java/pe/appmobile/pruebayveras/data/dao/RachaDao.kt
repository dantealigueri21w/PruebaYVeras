package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.RachaEntity

@Dao
interface RachaDao {
    @Query("SELECT * FROM racha WHERE id = 1")
    fun observar(): Flow<RachaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(racha: RachaEntity)
}
