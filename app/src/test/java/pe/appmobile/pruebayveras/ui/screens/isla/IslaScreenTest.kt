package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class IslaScreenTest {

    @get:Rule
    val compose = createComposeRule()

    /** `setContent` solo puede llamarse una vez por test — de ahí un test por isla,
     * en vez de un bucle dentro de un solo test (sección 10.1: cada pantalla alcanzable
     * necesita su propio render real).
     *
     * Nota real encontrada: `db.close()` justo después de `waitForIdle()` es una
     * carrera — el `init` del ViewModel siembra y lee en una corrutina que usa el
     * executor propio de Room (un hilo de fondo real, no el reloj de Compose que
     * `waitForIdle()` sincroniza), así que cerrar la base ahí puede alcanzar a una
     * consulta todavía en vuelo (`SQLException: connection is closed`, visto en la
     * Isla del Risco). La base en memoria no necesita cerrarse a mano en un test: se
     * descarta con el resto del contexto de Robolectric al terminar el test. */
    private fun rendear(idIsla: String) {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = IslaViewModel(db, idIsla)

        compose.setContent {
            PruebaYVerasTheme { IslaScreen(viewModel = viewModel, onVolver = {}) }
        }
        compose.waitForIdle()
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
