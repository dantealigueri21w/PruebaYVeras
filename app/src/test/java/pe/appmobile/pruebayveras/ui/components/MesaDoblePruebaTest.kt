package pe.appmobile.pruebayveras.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class MesaDoblePruebaTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `ejecuta cuando el montaje es una prueba justa`() {
        var ejecutado = false
        var avisoInjusto = false
        compose.setContent {
            PruebaYVerasTheme {
                MesaDoblePrueba(
                    control = Montaje(listOf(Variable("sal", 0))),
                    prueba = Montaje(listOf(Variable("sal", 3))),
                    onEjecutar = { ejecutado = true },
                    contenidoVariable = { _, _, _ -> },
                    onPruebaInjusta = { avisoInjusto = true },
                )
            }
        }
        compose.onNodeWithText("Correr la prueba").performClick()

        assertTrue(ejecutado)
        assertFalse(avisoInjusto)
    }

    @Test
    fun `avisa cuando el montaje no es una prueba justa`() {
        var ejecutado = false
        var avisoInjusto = false
        compose.setContent {
            PruebaYVerasTheme {
                MesaDoblePrueba(
                    control = Montaje(listOf(Variable("sal", 0), Variable("volumenAgua", 200))),
                    prueba = Montaje(listOf(Variable("sal", 3), Variable("volumenAgua", 250))),
                    onEjecutar = { ejecutado = true },
                    contenidoVariable = { _, _, _ -> },
                    onPruebaInjusta = { avisoInjusto = true },
                )
            }
        }
        compose.onNodeWithText("Correr la prueba").performClick()

        assertFalse(ejecutado)
        assertTrue(avisoInjusto)
    }
}
