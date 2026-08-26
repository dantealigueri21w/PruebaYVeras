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

    private fun esperarResultado(viewModel: IslaViewModel) {
        var pasadas = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadas < 100) {
            Thread.sleep(50)
            compose.waitForIdle()
            pasadas++
        }
        assertTrue("debe verse un resultado tras tocar ¡Pruébalo!", viewModel.estado.value.ultimoResultado != null)
    }

    @Test
    fun `probar en la Isla de la Marea guarda un intento real`() {
        val (viewModel, db) = cargarIsla("isla_marea")

        // sal=8 da alturaFlotacion=5 con MotorFlotabilidad — la meta real del reto facil.
        viewModel.cambiarVariablePrueba("sal", 8)
        compose.waitForIdle()

        compose.onNodeWithText("¡Pruébalo!").performScrollTo().performClick()
        compose.waitForIdle()

        val intentos = runBlocking { db.intentoDao().observarPorReto("reto_marea_facil").first() }
        assertTrue("debe haber guardado al menos un intento real", intentos.isNotEmpty())
    }

    @Test
    fun `tras lograr la meta se ve el resultado, y Continuar avanza al siguiente reto`() {
        val (viewModel, _) = cargarIsla("isla_marea")
        val indiceInicial = viewModel.estado.value.indiceRetoActual

        viewModel.cambiarVariablePrueba("sal", 8)
        compose.waitForIdle()
        compose.onNodeWithText("¡Pruébalo!").performScrollTo().performClick()
        compose.waitForIdle()
        esperarResultado(viewModel)

        assertTrue("la meta se logra con sal=8", viewModel.estado.value.ultimoResultado?.logrado == true)
        compose.onNodeWithText("¡Lo lograste!", substring = true).assertIsDisplayed()

        compose.onNodeWithText("Continuar").performScrollTo().performClick()
        compose.waitForIdle()

        assertTrue("Continuar debe cerrar el panel de resultado", viewModel.estado.value.ultimoResultado == null)
        assertEquals(
            "Continuar debe avanzar al siguiente reto cuando se logro la meta",
            indiceInicial + 1,
            viewModel.estado.value.indiceRetoActual,
        )
    }

    @Test
    fun `si no se logra la meta, Reintentar deja en el mismo reto con el control de nuevo`() {
        val (viewModel, _) = cargarIsla("isla_marea")

        // sal=0 nunca llega a flotar (alturaFlotacion=0), lejos de la meta del reto facil (5).
        viewModel.cambiarVariablePrueba("sal", 0)
        compose.waitForIdle()
        compose.onNodeWithText("¡Pruébalo!").performScrollTo().performClick()
        compose.waitForIdle()
        esperarResultado(viewModel)

        assertTrue("sal=0 no deberia lograr la meta", viewModel.estado.value.ultimoResultado?.logrado == false)
        compose.onNodeWithText("Todavía no", substring = true).assertIsDisplayed()

        compose.onNodeWithText("Reintentar").performScrollTo().performClick()
        compose.waitForIdle()

        assertEquals("no debe avanzar de reto si no se logro la meta", 0, viewModel.estado.value.indiceRetoActual)
        // El control (la perilla de "sal") debe estar visible de nuevo para reintentar.
        compose.onNodeWithContentDescription("sal:", substring = true).assertIsDisplayed()
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

        // "altura" es la variable del reto dificil de esta isla — se avanza hasta ahi
        // logrando facil y medio primero (los dos con "paracaidas").
        repeat(2) {
            viewModel.cambiarVariablePrueba("paracaidas", true)
            viewModel.probar()
            esperarResultado(viewModel)
            viewModel.continuarTrasResultado()
        }
        assertEquals("altura", viewModel.estado.value.retoActual?.variableIndependiente)

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
