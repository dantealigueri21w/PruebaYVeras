package pe.appmobile.pruebayveras.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.appmobile.pruebayveras.data.dao.InsigniaDao
import pe.appmobile.pruebayveras.data.dao.IntentoDao
import pe.appmobile.pruebayveras.data.dao.IslaDao
import pe.appmobile.pruebayveras.data.dao.PaginaCuadernoDao
import pe.appmobile.pruebayveras.data.dao.PerfilDao
import pe.appmobile.pruebayveras.data.dao.PiezaChirimboloDao
import pe.appmobile.pruebayveras.data.dao.RachaDao
import pe.appmobile.pruebayveras.data.dao.RetoDao
import pe.appmobile.pruebayveras.data.entity.InsigniaEntity
import pe.appmobile.pruebayveras.data.entity.IntentoEntity
import pe.appmobile.pruebayveras.data.entity.IslaEntity
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity
import pe.appmobile.pruebayveras.data.entity.PerfilEntity
import pe.appmobile.pruebayveras.data.entity.PiezaChirimboloEntity
import pe.appmobile.pruebayveras.data.entity.RachaEntity
import pe.appmobile.pruebayveras.data.entity.RetoEntity

@Database(
    entities = [
        PerfilEntity::class,
        IslaEntity::class,
        RetoEntity::class,
        IntentoEntity::class,
        PiezaChirimboloEntity::class,
        PaginaCuadernoEntity::class,
        InsigniaEntity::class,
        RachaEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun islaDao(): IslaDao
    abstract fun retoDao(): RetoDao
    abstract fun intentoDao(): IntentoDao
    abstract fun piezaChirimboloDao(): PiezaChirimboloDao
    abstract fun paginaCuadernoDao(): PaginaCuadernoDao
    abstract fun insigniaDao(): InsigniaDao
    abstract fun rachaDao(): RachaDao
}
