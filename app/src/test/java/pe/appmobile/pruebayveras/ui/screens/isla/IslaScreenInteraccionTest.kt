package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class IslaScreenInteraccionTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `correr la prueba en la Isla de la Marea guarda un intento real`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = IslaViewModel(db, "isla_marea")

        compose.setContent {
            PruebaYVerasTheme { IslaScreen(viewModel = viewModel, onVolver = {}) }
        }
        compose.waitForIdle()

        // El sembrado y la carga de retos corren en el executor propio de Room (un hilo
        // real de fondo, no el reloj de Compose) — un solo waitForIdle() no siempre
        // alcanza a que ese hilo termine y publique el nuevo estado. Se espera la
        // condicion real, sondeando, en vez de confiar en una sola pasada de idle.
        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            Thread.sleep(50)
            compose.waitForIdle()
            pasadas++
        }
        assertTrue("los retos deben haberse cargado antes de 5s", viewModel.estado.value.retos.isNotEmpty())

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
}
