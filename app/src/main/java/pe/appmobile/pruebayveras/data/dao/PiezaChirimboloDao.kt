package pe.appmobile.pruebayveras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pe.appmobile.pruebayveras.data.entity.PiezaChirimboloEntity

@Dao
interface PiezaChirimboloDao {
    @Query("SELECT * FROM pieza_chirimbolo")
    fun observarTodas(): Flow<List<PiezaChirimboloEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(piezas: List<PiezaChirimboloEntity>)

    @Update
    suspend fun actualizar(pieza: PiezaChirimboloEntity)
}
