package pe.appmobile.pruebayveras.ui.screens.isla

import android.os.Looper
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest

/** Cubre la mecánica nueva (26/08/2026): tocar "¡Pruébalo!" calcula el resultado real
 * con el motor de la isla y lo compara contra la meta del reto — sin control, sin
 * prueba justa, sin corridas ni pregunta de tendencia. */
@RunWith(RobolectricTestRunner::class)
class IslaViewModelTest {

    private val store = ViewModelStore()

    @After
    fun cerrarViewModel() {
        store.clear()
    }

    private suspend fun esperarRetos(viewModel: IslaViewModel) {
        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadas++
        }
    }

    private suspend fun esperarResultado(viewModel: IslaViewModel) {
        var pasadas = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadas++
        }
    }

    @Test
    fun `lograr la meta de los tres retos de una isla confirma la pieza de Chirimbolo`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_marea") }
        esperarRetos(viewModel)

        // sal=8,12,18 dan alturaFlotacion=5,9,15 con MotorFlotabilidad (altura = sal-3)
        // — exactamente los tres objetivos reales de fácil/medio/difícil.
        for (sal in listOf(8, 12, 18)) {
            viewModel.cambiarVariablePrueba("sal", sal)
            viewModel.probar()
            esperarResultado(viewModel)
            assertTrue("sal=$sal debería lograr la meta de su reto", viewModel.estado.value.ultimoResultado?.logrado == true)
            viewModel.continuarTrasResultado()
        }

        assertTrue("debe confirmarse la pieza tras completar los tres retos", viewModel.estado.value.piezaConfirmada)

        var pasadasPaginas = 0
        var paginas = db.paginaCuadernoDao().observarTodas().first()
        while (paginas.size < 3 && pasadasPaginas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            paginas = db.paginaCuadernoDao().observarTodas().first()
            pasadasPaginas++
        }
        assertEquals("cada reto logrado debe dejar su propia pagina real", 3, paginas.size)
    }

    @Test
    fun `no lograr la meta no avanza de reto y deja reintentar`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_marea") }
        esperarRetos(viewModel)

        // sal=0 -> alturaFlotacion=0, lejos del objetivo del reto facil (5, margen 1.75).
        viewModel.cambiarVariablePrueba("sal", 0)
        viewModel.probar()
        esperarResultado(viewModel)
        assertFalse(viewModel.estado.value.ultimoResultado?.logrado ?: true)

        viewModel.continuarTrasResultado()
        assertEquals("no debe avanzar de reto si no se logro la meta", 0, viewModel.estado.value.indiceRetoActual)
        assertNull("el panel de resultado debe cerrarse para poder reintentar", viewModel.estado.value.ultimoResultado)

        // Reintenta con la cantidad que si logra la meta.
        viewModel.cambiarVariablePrueba("sal", 8)
        viewModel.probar()
        esperarResultado(viewModel)
        assertTrue(viewModel.estado.value.ultimoResultado?.logrado == true)
    }

    @Test
    fun `en un reto con dos variables, solo la variableIndependiente del reto se prueba`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_viento") }
        esperarRetos(viewModel)

        // reto_viento_facil: variableIndependiente="paracaidas"; "altura" queda fija
        // en su valor base (20f) sin que el niño la toque en este reto.
        assertEquals("paracaidas", viewModel.estado.value.retoActual?.variableIndependiente)

        viewModel.cambiarVariablePrueba("paracaidas", true)
        viewModel.probar()
        esperarResultado(viewModel)

        assertTrue("con paracaidas puesto debe lograr la meta de este reto", viewModel.estado.value.ultimoResultado?.logrado == true)
    }
}
