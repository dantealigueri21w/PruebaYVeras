package pe.appmobile.pruebayveras.data

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {
    @Volatile private var instancia: AppDatabase? = null

    fun obtener(context: Context): AppDatabase =
        instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "pruebayveras.db",
            ).build().also { instancia = it }
        }
}
