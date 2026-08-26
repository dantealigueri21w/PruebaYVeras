package pe.appmobile.pruebayveras.ui.screens.isla

import android.os.Looper
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.domain.engine.Tendencia
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest

/** Cubre el bug real encontrado el 26/08/2026: `elegirTendencia` nunca guardaba una
 * página en el Cuaderno de Campo — la pantalla de Cuaderno quedaba vacía para siempre
 * en la app real, aunque el niño sí respondiera la pregunta de tendencia. */
@RunWith(RobolectricTestRunner::class)
class IslaViewModelTest {

    private val store = ViewModelStore()

    @After
    fun cerrarViewModel() {
        store.clear()
    }

    @Test
    fun `elegir una tendencia guarda de verdad una pagina en el cuaderno`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_marea") }

        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadas++
        }

        val retos = viewModel.estado.value.retos
        for ((indice, _) in retos.withIndex()) {
            viewModel.cambiarVariablePrueba("sal", indice + 1)
            viewModel.ejecutarPrueba()
            var pasadasReto = 0
            while (viewModel.estado.value.indiceRetoActual == indice && !viewModel.estado.value.mostrarPreguntaTendencia && pasadasReto < 100) {
                shadowOf(Looper.getMainLooper()).idle()
                kotlinx.coroutines.delay(50)
                pasadasReto++
            }
        }

        assertTrue("debe mostrar la pregunta de tendencia tras el ultimo reto", viewModel.estado.value.mostrarPreguntaTendencia)

        viewModel.elegirTendencia(Tendencia.SUBE)

        var pasadasPagina = 0
        var paginas = db.paginaCuadernoDao().observarTodas().first()
        while (paginas.isEmpty() && pasadasPagina < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            paginas = db.paginaCuadernoDao().observarTodas().first()
            pasadasPagina++
        }

        assertEquals(1, paginas.size)
        assertEquals(retos.last().idReto, paginas.first().idReto)
        assertEquals("SUBE", paginas.first().tendenciaElegida)
    }
}
