package pe.appmobile.pruebayveras.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class MesaDeTanteoTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `correr la prueba siempre ejecuta el callback, incluso con dos variables cambiadas`() {
        var ejecutado = false
        val control = Montaje(listOf(Variable("sal", 0), Variable("volumenAgua", 250)))
        val prueba = Montaje(listOf(Variable("sal", 3), Variable("volumenAgua", 500)))

        compose.setContent {
            PruebaYVerasTheme {
                MesaDeTanteo(
                    control = control,
                    prueba = prueba,
                    contenidoVariable = { _, _, _ -> },
                    onEjecutar = { ejecutado = true },
                )
            }
        }
        compose.onNodeWithText("Correr la prueba").performClick()
        assertTrue(ejecutado)
    }
}
