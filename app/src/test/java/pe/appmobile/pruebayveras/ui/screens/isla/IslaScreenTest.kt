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

    /** Cierra el ViewModel de este test antes del siguiente — sin esto, la corrutina
     * de `init` (lanzada en `viewModelScope`) puede seguir viva y colarse en el
     * siguiente test cuando toda la suite corre junta, tocando una base de datos en
     * memoria que ya no existe (`IllegalStateException` real visto al correr
     * `testDebugUnitTest` completo). */
    @After
    fun cerrarViewModel() {
        store.clear()
    }

    /** `setContent` solo puede llamarse una vez por test — de ahí un test por isla,
     * en vez de un bucle dentro de un solo test (sección 10.1: cada pantalla alcanzable
     * necesita su propio render real).
     *
     * El sembrado y la carga de retos corren en el executor propio de Room (un hilo de
     * fondo real), no en el reloj de Compose que sincroniza `waitForIdle()` — se espera
     * la condición real (sondeando con tiempo real) antes de confirmar que la pantalla
     * de verdad se dibujó con contenido, no solo que "no reventó" con la pantalla vacía.
     *
     * Con la mesa de tanteo (`MesaDeTanteo`), "Correr la prueba" está siempre visible
     * apenas se carga el reto — nunca bloqueada, a diferencia del botón "¡Pruébalo!" de
     * la mecánica anterior. */
    private fun rendear(idIsla: String) {
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
        assertTrue("los retos de $idIsla deben haberse cargado", viewModel.estado.value.retos.isNotEmpty())
        compose.onNodeWithText("Correr la prueba").performScrollTo().assertIsDisplayed()
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
}
