package pe.appmobile.pruebayveras.data

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {
    @Volatile private var instancia: AppDatabase? = null

    fun obtener(context: Context): AppDatabase =
        instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "pruebayveras.db",
            )
                // Todavia no hay datos reales de usuario que conservar entre versiones
                // (solo semillas re-generadas en cada primer arranque) — no hace falta
                // escribir una migracion real por cada cambio de esquema en desarrollo.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build().also { instancia = it }
        }
}
