package pe.appmobile.pruebayveras.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase

@RunWith(RobolectricTestRunner::class)
class CienciaLabRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: CienciaLabRepository

    @Before
    fun crearBaseYRepositorio() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = CienciaLabRepository(db)
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    @Test
    fun `sembrar carga las 9 islas, 27 retos, 9 piezas y 12 insignias`() = runBlocking {
        repository.sembrarSiEsPrimeraVez()

        assertEquals(9, db.islaDao().observarTodas().first().size)
        assertEquals(9, db.piezaChirimboloDao().observarTodas().first().size)
        assertEquals(12, db.insigniaDao().observarTodas().first().size)
    }

    @Test
    fun `sembrar dos veces no duplica los datos`() = runBlocking {
        repository.sembrarSiEsPrimeraVez()
        repository.sembrarSiEsPrimeraVez()

        assertEquals(9, db.islaDao().observarTodas().first().size)
    }

    @Test
    fun `registrar un intento queda guardado de verdad`() = runBlocking {
        repository.registrarIntento(
            idReto = "reto_marea_dificil", variableCambiada = "sal",
            valorProbado = "18", resultadoReal = 15f, logrado = true,
        )

        val intentos = db.intentoDao().observarPorReto("reto_marea_dificil").first()
        assertEquals(1, intentos.size)
        assertEquals("18", intentos.first().valorProbado)
        assertEquals(15f, intentos.first().resultadoReal)
        assertTrue(intentos.first().logrado)
    }

    @Test
    fun `confirmar una pieza de chirimbolo la marca como confirmada`() = runBlocking {
        repository.sembrarSiEsPrimeraVez()

        repository.confirmarPieza("pieza_tanque_flotador")

        val pieza = db.piezaChirimboloDao().observarTodas().first()
            .first { it.idPieza == "pieza_tanque_flotador" }
        assertTrue(pieza.confirmada)
    }

    @Test
    fun `registrar una pagina de logro queda guardada de verdad`() = runBlocking {
        repository.registrarPaginaLogro("reto_marea_dificil", resultadoReal = 15f)

        val paginas = db.paginaCuadernoDao().observarTodas().first()
        assertEquals(1, paginas.size)
        assertEquals("reto_marea_dificil", paginas.first().idReto)
        assertEquals(15f, paginas.first().resultadoReal)
    }
}
