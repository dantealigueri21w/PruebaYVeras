package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class IslaScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()
    private var dbAbierta: AppDatabase? = null

    /** Cierra el ViewModel de este test antes del siguiente — sin esto, la corrutina
     * de `init` (lanzada en `viewModelScope`) puede seguir viva y colarse en el
     * siguiente test cuando toda la suite corre junta, tocando una base de datos en
     * memoria que ya no existe (`IllegalStateException` real visto al correr
     * `testDebugUnitTest` completo). También cierra la base en memoria: el
     * "Invalidation Tracker" propio de Room sigue vivo en un hilo de fondo compartido
     * y, sin cerrarla, puede chocar contra una conexión de Robolectric ya reciclada
     * por el siguiente test (`IllegalStateException: Illegal connection pointer`). */
    @After
    fun cerrarViewModel() {
        store.clear()
        dbAbierta?.close()
    }

    /** Carga el ViewModel de la isla y renderiza `IslaScreen` de verdad, devolviendo el
     * ViewModel para que el test pueda seguir interactuando con él (por ejemplo, correr
     * la prueba y esperar el resultado) en vez de solo confirmar que la pantalla no
     * revienta.
     *
     * El sembrado y la carga de retos corren en el executor propio de Room (un hilo de
     * fondo real), no en el reloj de Compose que sincroniza `waitForIdle()` — se espera
     * la condición real (sondeando con tiempo real) antes de confirmar que la pantalla
     * de verdad se dibujó con contenido, no solo que "no reventó" con la pantalla vacía.
     *
     * Con la mesa de tanteo (`MesaDeTanteo`), "Correr la prueba" está siempre visible
     * apenas se carga el reto — nunca bloqueada, a diferencia del botón "¡Pruébalo!" de
     * la mecánica anterior. */
    private fun cargarViewModel(idIsla: String): IslaViewModel {
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
        assertTrue("los retos de $idIsla deben haberse cargado", viewModel.estado.value.retos.isNotEmpty())
        compose.onNodeWithText("Correr la prueba").performScrollTo().assertIsDisplayed()
        return viewModel
    }

    /** [cargarViewModel] descartando el ViewModel — un test por isla (sección 10.1:
     * cada pantalla alcanzable necesita su propio render real; `setContent` solo puede
     * llamarse una vez por test, así que no hay forma de recorrerlas en un bucle). */
    private fun rendear(idIsla: String) {
        cargarViewModel(idIsla)
    }

    @Test
    fun `isla de la marea no revienta la app`() = rendear("isla_marea")

    @Test
    fun `isla del viento no revienta la app`() = rendear("isla_viento")

    @Test
    fun `isla del jardin no revienta la app`() = rendear("isla_jardin")

    @Test
    fun `isla de la cueva no revienta la app`() = rendear("isla_cueva")

    @Test
    fun `isla del faro no revienta la app`() = rendear("isla_faro")

    @Test
    fun `isla de las olas no revienta la app`() = rendear("isla_olas")

    @Test
    fun `isla del risco no revienta la app`() = rendear("isla_risco")

    @Test
    fun `isla del iman no revienta la app`() = rendear("isla_iman")

    @Test
    fun `isla del reflejo no revienta la app`() = rendear("isla_reflejo")

    @Test
    fun `el tutorial jugado solo aparece en el primer reto de la isla de la marea`() {
        val viewModel = cargarViewModel("isla_marea")
        // No assertIsDisplayed(): cargarViewModel ya hizo scroll hasta "Correr la prueba",
        // y el aviso del tutorial queda antes en la misma columna — sigue en el árbol,
        // pero fuera del viewport visible. Lo que importa aquí es que exista.
        compose.onNodeWithText("Chirimbolo señala", substring = true).assertExists()

        // Mismo motivo que el sondeo de cargarViewModel: ejecutarPrueba() guarda el
        // intento en Room desde una corrutina de viewModelScope, en un hilo de fondo
        // real ajeno al reloj de Compose — se espera la condición real, no waitForIdle().
        viewModel.ejecutarPrueba()
        var pasadas = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadas < 100) {
            Thread.sleep(50)
            compose.waitForIdle()
            pasadas++
        }
        assertTrue("la prueba debio arrojar un resultado", viewModel.estado.value.ultimoResultado != null)
        compose.waitForIdle()
        compose.onNodeWithText("Chirimbolo señala", substring = true).assertDoesNotExist()
    }

    @Test
    fun `el tutorial jugado no aparece en una isla distinta a la marea`() {
        cargarViewModel("isla_viento")
        compose.onNodeWithText("Chirimbolo señala", substring = true).assertDoesNotExist()
    }
}
