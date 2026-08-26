package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class IslaScreenInteraccionTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()

    @After
    fun cerrarViewModel() {
        store.clear()
    }

    private data class IslaDeTest(val viewModel: IslaViewModel, val db: AppDatabase)

    /**
     * El sembrado y la carga de retos corren en el executor propio de Room (un hilo
     * real de fondo, no el reloj de Compose) — un solo waitForIdle() no siempre alcanza
     * a que ese hilo termine y publique el nuevo estado. Se espera la condicion real,
     * sondeando, en vez de confiar en una sola pasada de idle.
     */
    private fun cargarIsla(idIsla: String): IslaDeTest {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, idIsla) }

        compose.setContent {
            PruebaYVerasTheme { IslaScreen(viewModel = viewModel, onVolver = {}) }
        }
        compose.waitForIdle()

        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            Thread.sleep(50)
            compose.waitForIdle()
            pasadas++
        }
        assertTrue("los retos deben haberse cargado antes de 5s", viewModel.estado.value.retos.isNotEmpty())
        return IslaDeTest(viewModel, db)
    }

    @Test
    fun `correr la prueba en la Isla de la Marea guarda un intento real`() {
        val (viewModel, db) = cargarIsla("isla_marea")

        // Simula lo que hace arrastrar la perilla: cambia la sal de la prueba para que
        // difiera del control en exactamente una variable. Sin este paso, el montaje de
        // prueba nace igual al de control (0 diferencias) y "Correr la prueba" avisa,
        // con razón, que no es una prueba justa — no hay nada que comparar todavía.
        viewModel.cambiarVariablePrueba("sal", 3)
        compose.waitForIdle()

        compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
        compose.waitForIdle()

        val intentos = runBlocking { db.intentoDao().observarPorReto("reto_marea_facil").first() }
        assertTrue("debe haber guardado al menos un intento real", intentos.isNotEmpty())
    }

    @Test
    fun `tras correr la prueba se ve el resultado, y Continuar avanza al siguiente reto`() {
        val (viewModel, _) = cargarIsla("isla_marea")
        val indiceInicial = viewModel.estado.value.indiceRetoActual

        // Antes, "Correr la prueba" avanzaba en silencio al siguiente reto: no habia
        // forma de notar que algo habia pasado. Ahora debe quedar un resultado visible
        // (el panel con "Resultado del control"/"Resultado de la prueba") hasta que el
        // niño toque "Continuar".
        viewModel.cambiarVariablePrueba("sal", 3)
        compose.waitForIdle()
        compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
        compose.waitForIdle()

        var pasadas = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadas < 100) {
            Thread.sleep(50)
            compose.waitForIdle()
            pasadas++
        }
        assertTrue("debe verse un resultado tras correr la prueba", viewModel.estado.value.ultimoResultado != null)
        compose.onNodeWithText("Resultado del control", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Resultado de la prueba", substring = true).assertIsDisplayed()

        compose.onNodeWithText("Continuar").performScrollTo().performClick()
        compose.waitForIdle()

        assertTrue("Continuar debe cerrar el panel de resultado", viewModel.estado.value.ultimoResultado == null)
        assertEquals(
            "Continuar debe avanzar al siguiente reto",
            indiceInicial + 1,
            viewModel.estado.value.indiceRetoActual,
        )
    }

    @Test
    fun `el lado Control muestra un booleano en palabras, no el false literal de Kotlin`() {
        cargarIsla("isla_viento")

        // Antes esto se leia literalmente paracaidas: false — ingles de programacion,
        // no una palabra que un niño de 8 a 12 años reconozca.
        compose.onNodeWithText("paracaidas: No").assertIsDisplayed()
    }

    @Test
    fun `un arrastre real dentro de la pantalla completa llega al limite superior del rango`() {
        val (viewModel, _) = cargarIsla("isla_marea")
        val perilla = compose.onNodeWithContentDescription("sal:", substring = true)

        // Un solo arrastre continuo, muy por encima de lo necesario para cubrir 0..20.
        // Si esto no llega a 20, algo detiene el gesto antes de que el dedo real lo haga
        // (por ejemplo, el scroll vertical que envuelve la pantalla robandose el
        // arrastre, o el calculo del paso quedando corto).
        perilla.performTouchInput { swipeUp(startY = bottom, endY = bottom - 3000f, durationMillis = 3000) }
        compose.waitForIdle()

        val valorFinal = viewModel.estado.value.prueba.valorDe("sal") as Int
        assertEquals(
            "un arrastre de sobra deberia llegar al limite superior del rango (20), no quedarse a mitad de camino",
            20,
            valorFinal,
        )
    }

    // Antes, la sensibilidad era una cantidad fija de pixeles por paso: un rango de
    // 0..60 (variables continuas, ej. altura) necesitaba un arrastre casi tres veces mas
    // largo que uno de 0..20 para completarse, y un arrastre comodo de un solo gesto se
    // quedaba a mitad de camino en el rango ancho. Las dos pruebas siguientes usan
    // exactamente el mismo largo de arrastre sobre un rango angosto y uno ancho: la
    // perilla debe sentirse igual de jugable sin importar cuantos pasos tenga su rango.
    private val distanciaComodaDeArrastre get() = with(compose.density) { 400.dp.toPx() }

    @Test
    fun `un arrastre comodo completa un rango angosto (sal, 0-20)`() {
        val (viewModel, _) = cargarIsla("isla_marea")
        compose.onNodeWithContentDescription("sal:", substring = true).performTouchInput {
            swipeUp(startY = bottom, endY = bottom - distanciaComodaDeArrastre, durationMillis = 300)
        }
        compose.waitForIdle()

        assertEquals(20, viewModel.estado.value.prueba.valorDe("sal") as Int)
    }

    @Test
    fun `el mismo arrastre comodo tambien completa un rango ancho (altura, 0-60)`() {
        val (viewModel, _) = cargarIsla("isla_viento")
        compose.onNodeWithContentDescription("altura:", substring = true).performTouchInput {
            swipeUp(startY = bottom, endY = bottom - distanciaComodaDeArrastre, durationMillis = 300)
        }
        compose.waitForIdle()

        assertEquals(
            "el rango ancho (0..60) deberia completarse con el mismo arrastre que el angosto",
            60f,
            viewModel.estado.value.prueba.valorDe("altura") as Float,
            0.01f,
        )
    }
}
