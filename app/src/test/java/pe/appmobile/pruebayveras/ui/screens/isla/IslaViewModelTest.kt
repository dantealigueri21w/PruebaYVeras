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
            // El reto dificil pide tres corridas reales (una tendencia de verdad, no
            // una comparacion de dos) antes de avanzar; facil y medio piden solo una.
            // Cada corrida necesita una magnitud de sal distinta a las anteriores del
            // MISMO reto, o el sistema la rechaza (ejecutarPrueba() no avanza).
            val corridasNecesarias = viewModel.estado.value.corridasNecesarias
            for (corrida in 1..corridasNecesarias) {
                viewModel.cambiarVariablePrueba("sal", corrida)
                viewModel.ejecutarPrueba()

                // ejecutarPrueba() ya no avanza de reto por si sola: primero deja el
                // resultado visible (ultimoResultado) para que el niño lo vea, y solo
                // se avanza al llamar continuarTrasResultado() — el equivalente a
                // tocar "Continuar" en el panel de resultado.
                var pasadasResultado = 0
                while (viewModel.estado.value.ultimoResultado == null && pasadasResultado < 100) {
                    shadowOf(Looper.getMainLooper()).idle()
                    kotlinx.coroutines.delay(50)
                    pasadasResultado++
                }
                assertTrue(
                    "debe calcularse un resultado en el reto $indice, corrida $corrida",
                    viewModel.estado.value.ultimoResultado != null,
                )

                viewModel.continuarTrasResultado()
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

    @Test
    fun `repetir la misma magnitud en el reto dificil no cuenta como una corrida nueva`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_marea") }

        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadas++
        }

        // Avanza hasta el reto dificil (facil y medio piden una sola corrida).
        repeat(2) { indice ->
            viewModel.cambiarVariablePrueba("sal", indice + 1)
            viewModel.ejecutarPrueba()
            var pasadasResultado = 0
            while (viewModel.estado.value.ultimoResultado == null && pasadasResultado < 100) {
                shadowOf(Looper.getMainLooper()).idle()
                kotlinx.coroutines.delay(50)
                pasadasResultado++
            }
            viewModel.continuarTrasResultado()
        }
        assertEquals("DIFICIL", viewModel.estado.value.retoActual?.dificultad)

        viewModel.cambiarVariablePrueba("sal", 4)
        viewModel.ejecutarPrueba()
        var pasadasPrimera = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadasPrimera < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadasPrimera++
        }
        assertEquals(1, viewModel.estado.value.corridasRetoActual)
        viewModel.continuarTrasResultado()

        // Repite la MISMA cantidad de sal (4) que ya se probo en este reto.
        viewModel.cambiarVariablePrueba("sal", 4)
        viewModel.ejecutarPrueba()
        var pasadasAviso = 0
        while (!viewModel.estado.value.ultimoAvisoMagnitudRepetida && pasadasAviso < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadasAviso++
        }

        assertTrue("debe avisar que esa magnitud ya se probo", viewModel.estado.value.ultimoAvisoMagnitudRepetida)
        assertEquals("no debe contar como una corrida nueva", 1, viewModel.estado.value.corridasRetoActual)
        assertTrue("no debe abrir un panel de resultado nuevo", viewModel.estado.value.ultimoResultado == null)
    }

    @Test
    fun `en una isla con variables distintas por reto, la tendencia final solo usa los datos reales del reto dificil`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_viento") }

        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            kotlinx.coroutines.delay(50)
            pasadas++
        }

        // Facil y medio de la Isla del Viento prueban "paracaidas" (un booleano), no
        // "altura" — antes del arreglo esos resultados se mezclaban con los del reto
        // dificil para "armar" una tendencia que en realidad comparaba variables
        // distintas. Se juegan igual que jugaria un niño real, para probar que ya no
        // contaminan la conclusion final.
        val retos = viewModel.estado.value.retos
        for (reto in retos) {
            val corridasNecesarias = viewModel.estado.value.corridasNecesarias
            for (corrida in 1..corridasNecesarias) {
                when (reto.variableIndependiente) {
                    "paracaidas" -> viewModel.cambiarVariablePrueba("paracaidas", true)
                    "altura" -> viewModel.cambiarVariablePrueba("altura", 15f * corrida) // 15, 30, 45 m: creciente
                }
                viewModel.ejecutarPrueba()

                var pasadasResultado = 0
                while (viewModel.estado.value.ultimoResultado == null && pasadasResultado < 100) {
                    shadowOf(Looper.getMainLooper()).idle()
                    kotlinx.coroutines.delay(50)
                    pasadasResultado++
                }
                viewModel.continuarTrasResultado()
            }
        }

        assertTrue("debe mostrar la pregunta de tendencia tras el ultimo reto", viewModel.estado.value.mostrarPreguntaTendencia)

        // La altura real probada en el reto dificil fue creciente (15, 30, 45 m) y sin
        // paracaidas en los tres casos: caer desde mas alto tarda mas, la tendencia
        // real es SUBE — sin importar que facil y medio hayan probado otra cosa.
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
        assertTrue(
            "la tendencia debe salir correcta usando solo los datos reales de altura del reto dificil",
            paginas.first().tendenciaCorrecta,
        )
    }
}
