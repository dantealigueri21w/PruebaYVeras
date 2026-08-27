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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.seed.SemillaRetos
import pe.appmobile.pruebayveras.domain.engine.Tendencia
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

/**
 * Prueba la mesa de tanteo real: correr nunca bloquea, una prueba injusta explica por
 * qué y deja repetir, una prueba justa entrega la tarjeta "¿Sabías que...?" y avanza,
 * y terminar la isla lleva a la pregunta de tendencia y a confirmar la pieza.
 */
@RunWith(RobolectricTestRunner::class)
class IslaScreenInteraccionTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()
    private var dbAbierta: AppDatabase? = null

    @After
    fun cerrarViewModel() {
        store.clear()
        dbAbierta?.close()
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
            .allowMainThreadQueries().build().also { dbAbierta = it }
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
        assertTrue("debe verse un resultado tras correr la prueba", viewModel.estado.value.ultimoResultado != null)
    }

    /** Cierra la tarjeta "¿Sabías que...?" si está abierta — se gana en cada prueba
     * justa y tapa el resto de la pantalla (scrim encima) hasta tocar "Continuar". Vive
     * fuera de la Column con scroll (es un overlay sobre todo el Box), así que a
     * diferencia de los botones de la pantalla del reto, no admite `performScrollTo()`. */
    private fun cerrarTarjetaSiHayUna(viewModel: IslaViewModel) {
        if (viewModel.estado.value.tarjetaSabiasQue != null) {
            compose.onNodeWithText("Continuar").performClick()
            compose.waitForIdle()
        }
    }

    @Test
    fun `correr la prueba en la Isla de la Marea guarda un intento real`() {
        val (viewModel, db) = cargarIsla("isla_marea")

        viewModel.cambiarVariablePrueba("sal", 3)
        compose.waitForIdle()

        compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
        compose.waitForIdle()

        val intentos = runBlocking { db.intentoDao().observarPorReto("reto_marea_facil").first() }
        assertTrue("debe haber guardado al menos un intento real", intentos.isNotEmpty())
    }

    @Test
    fun `cambiar dos variables no bloquea la prueba, explica por que no es justa, y Volver a intentar reinicia el montaje`() {
        val (viewModel, _) = cargarIsla("isla_marea")

        // sal y volumenAgua distintas del control a la vez: dos variables cambiadas.
        viewModel.cambiarVariablePrueba("sal", 3)
        viewModel.cambiarVariablePrueba("volumenAgua", 500)
        compose.waitForIdle()

        compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
        compose.waitForIdle()
        esperarResultado(viewModel)

        assertFalse("cambiar dos variables a la vez no es una prueba justa", viewModel.estado.value.ultimoResultado!!.fueJusta)
        compose.onNodeWithText("Eso cambió más de una cosa", substring = true).assertIsDisplayed()

        compose.onNodeWithText("Volver a intentar").performScrollTo().performClick()
        compose.waitForIdle()

        assertEquals("no debe avanzar de reto si la prueba no fue justa", 0, viewModel.estado.value.indiceRetoActual)
        assertTrue("el resultado debe cerrarse para poder tantear de nuevo", viewModel.estado.value.ultimoResultado == null)
        assertEquals("el montaje de prueba debe volver al control (sal=0)", 0, viewModel.estado.value.prueba.valorDe("sal"))
        compose.onNodeWithText("Correr la prueba").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun `correr la prueba sin cambiar ninguna variable explica que no cambio nada, no que cambio mas de una`() {
        val (viewModel, _) = cargarIsla("isla_marea")

        // Ninguna llamada a cambiarVariablePrueba: la Prueba arranca igual al Control,
        // igual que la primera vez que se juega cada uno de los 27 retos — el tutorial
        // de la Tarea 10 dice "cuando estés listo, corre la prueba" sin instruir a
        // cambiar nada antes.
        compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
        compose.waitForIdle()
        esperarResultado(viewModel)

        assertFalse("cero variables distintas no es una prueba justa", viewModel.estado.value.ultimoResultado!!.fueJusta)
        assertTrue(
            "no debe haber ninguna variable distinta si no se tocó nada",
            viewModel.estado.value.ultimoResultado!!.variablesDistintas.isEmpty(),
        )

        // El bug real: con variablesDistintas vacía, IslaScreen reutilizaba el título y
        // la explicación de "más de una cosa" (falsos con cero cambios) y la
        // interpolación de isla_prueba_injusta_explicacion dejaba un hueco en blanco
        // ("Cambiaste  a la vez..."). Debe verse el mensaje real de "no cambiaste nada",
        // no el de "más de una" ni el texto roto con el hueco vacío.
        compose.onNodeWithText("Todavía no cambiaste nada", substring = true).assertIsDisplayed()
        compose.onNodeWithText("Eso cambió más de una cosa", substring = true).assertDoesNotExist()
        compose.onNodeWithText("Cambiaste  a la vez", substring = true).assertDoesNotExist()

        compose.onNodeWithText("Volver a intentar").performScrollTo().performClick()
        compose.waitForIdle()

        assertEquals("no debe avanzar de reto si la prueba no fue justa", 0, viewModel.estado.value.indiceRetoActual)
        assertTrue("el resultado debe cerrarse para poder tantear de nuevo", viewModel.estado.value.ultimoResultado == null)
    }

    @Test
    fun `una prueba justa muestra el resultado y la tarjeta sabias que, y Continuar avanza al siguiente reto`() {
        val (viewModel, _) = cargarIsla("isla_marea")
        val indiceInicial = viewModel.estado.value.indiceRetoActual
        val datoDelRetoFacil = SemillaRetos.retos.first { it.idReto == "reto_marea_facil" }.datoCientifico

        viewModel.cambiarVariablePrueba("sal", 3)
        compose.waitForIdle()
        compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
        compose.waitForIdle()
        esperarResultado(viewModel)

        assertTrue("cambiar solo sal debe ser una prueba justa", viewModel.estado.value.ultimoResultado!!.fueJusta)
        compose.onNodeWithText("¡Prueba justa!", substring = true).assertIsDisplayed()

        compose.onNodeWithText("Continuar").performScrollTo().performClick()
        compose.waitForIdle()

        assertEquals(
            "una prueba justa debe entregar la tarjeta con el dato propio de ese reto",
            datoDelRetoFacil,
            viewModel.estado.value.tarjetaSabiasQue,
        )
        compose.onNodeWithText(datoDelRetoFacil, substring = true).assertIsDisplayed()

        // La tarjeta es un overlay fuera de la Column con scroll — su botón "Continuar"
        // no admite performScrollTo().
        compose.onNodeWithText("Continuar").performClick()
        compose.waitForIdle()

        assertTrue("cerrar la tarjeta debe quitarla del estado", viewModel.estado.value.tarjetaSabiasQue == null)
        assertEquals(
            "Continuar debe avanzar al siguiente reto cuando la prueba fue justa",
            indiceInicial + 1,
            viewModel.estado.value.indiceRetoActual,
        )
    }

    @Test
    fun `completar el ultimo reto de la isla con una prueba justa muestra la pregunta de tendencia`() {
        val (viewModel, _) = cargarIsla("isla_marea")

        // Los tres retos de la Isla de la Marea (facil, medio, dificil) cambian "sal".
        repeat(viewModel.estado.value.retos.size) {
            viewModel.cambiarVariablePrueba("sal", 3)
            compose.waitForIdle()
            compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
            compose.waitForIdle()
            esperarResultado(viewModel)
            assertTrue("cambiar solo sal debe ser siempre una prueba justa", viewModel.estado.value.ultimoResultado!!.fueJusta)

            compose.onNodeWithText("Continuar").performScrollTo().performClick()
            compose.waitForIdle()
            cerrarTarjetaSiHayUna(viewModel)
        }

        assertTrue("tras el ultimo reto debe mostrarse la pregunta de tendencia", viewModel.estado.value.mostrarPreguntaTendencia)
        compose.onNodeWithText("¿Qué muestran tus datos?").assertIsDisplayed()
    }

    @Test
    fun `elegir la tendencia correcta en la pregunta final confirma la pieza de Chirimbolo`() {
        val (viewModel, _) = cargarIsla("isla_marea")

        repeat(viewModel.estado.value.retos.size) {
            viewModel.cambiarVariablePrueba("sal", 3)
            compose.waitForIdle()
            compose.onNodeWithText("Correr la prueba").performScrollTo().performClick()
            compose.waitForIdle()
            esperarResultado(viewModel)
            compose.onNodeWithText("Continuar").performScrollTo().performClick()
            compose.waitForIdle()
            cerrarTarjetaSiHayUna(viewModel)
        }
        assertTrue(viewModel.estado.value.mostrarPreguntaTendencia)

        // Cada reto de este flujo solo aporta un dato real a su propio idReto — con un
        // solo punto, MotorCuadernoDatos.tendenciaReal (necesita al menos dos para ver
        // una tendencia) siempre concluye NO_CAMBIA para el ultimo reto.
        viewModel.elegirTendencia(Tendencia.NO_CAMBIA)
        compose.waitForIdle()

        var pasadas = 0
        while (!viewModel.estado.value.piezaConfirmada && pasadas < 100) {
            Thread.sleep(50)
            compose.waitForIdle()
            pasadas++
        }
        assertTrue("elegir la tendencia real debe confirmar la pieza", viewModel.estado.value.piezaConfirmada)
        compose.onNodeWithText("Chirimbolo tiene una pieza más", substring = true).assertIsDisplayed()
    }

    // `PerillaGiratoria` recorre cualquier rango con la MISMA distancia de arrastre
    // (`DISTANCIA_RANGO_COMPLETO` en PerillaGiratoria.kt) — un rango angosto (sal,
    // 0-20) y uno ancho (altura, 0-60) deben sentirse igual de jugables con el mismo
    // largo de arrastre. `MesaDeTanteo` ahora muestra TODAS las variables de la isla a
    // la vez (no solo la del reto activo), así que no hace falta avanzar de reto para
    // llegar a "altura": ya está en la mesa desde el primer reto de la Isla del Viento.
    private val distanciaComodaDeArrastre get() = with(compose.density) { 400.dp.toPx() }

    @Test
    fun `un arrastre comodo en la mesa de tanteo real completa un rango angosto (sal, 0-20)`() {
        val (viewModel, _) = cargarIsla("isla_marea")

        compose.onNodeWithContentDescription("sal:", substring = true).performScrollTo().performTouchInput {
            swipeUp(startY = bottom, endY = bottom - distanciaComodaDeArrastre, durationMillis = 300)
        }
        compose.waitForIdle()

        assertEquals(20, viewModel.estado.value.prueba.valorDe("sal"))
    }

    @Test
    fun `el mismo arrastre comodo en la mesa de tanteo real tambien completa un rango ancho (altura, 0-60)`() {
        val (viewModel, _) = cargarIsla("isla_viento")

        compose.onNodeWithContentDescription("altura:", substring = true).performScrollTo().performTouchInput {
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
