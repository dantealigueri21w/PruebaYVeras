package pe.appmobile.pruebayveras.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.entity.IntentoEntity
import pe.appmobile.pruebayveras.data.entity.IslaEntity

@RunWith(RobolectricTestRunner::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun crearBaseEnMemoria() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    @Test
    fun `insertar y leer una isla`() = runBlocking {
        db.islaDao().insertarTodas(listOf(
            IslaEntity(idIsla = "isla_marea", nombre = "Isla de la Marea", fenomeno = "densidad", requisitoDesbloqueo = null)
        ))
        val islas = db.islaDao().observarTodas().first()
        assertEquals(1, islas.size)
        assertEquals("Isla de la Marea", islas.first().nombre)
    }

    @Test
    fun `guardar un intento y leerlo de vuelta por reto`() = runBlocking {
        db.intentoDao().guardar(
            IntentoEntity(
                idReto = "reto_marea_1", variableCambiada = "sal",
                valorProbado = "3", resultadoReal = 3f,
                logrado = true, timestamp = 1000L,
            )
        )
        val intentos = db.intentoDao().observarPorReto("reto_marea_1").first()
        assertEquals(1, intentos.size)
        assertEquals(true, intentos.first().logrado)
    }

    @Test
    fun `la base recien creada no tiene datos`() = runBlocking {
        assertEquals(0, db.islaDao().observarTodas().first().size)
        assertEquals(0, db.insigniaDao().observarTodas().first().size)
    }
}
