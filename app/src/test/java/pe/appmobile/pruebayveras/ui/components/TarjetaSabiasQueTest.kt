package pe.appmobile.pruebayveras.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class TarjetaSabiasQueTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun `cerrar la tarjeta dispara el callback real`() {
        var cerrada = false
        compose.setContent { PruebaYVerasTheme { TarjetaSabiasQue(dato = "dato de prueba", onCerrar = { cerrada = true }) } }
        compose.onNodeWithText("dato de prueba").assertExists()
        compose.onNodeWithText("Continuar").performClick()
        assertTrue(cerrada)
    }
}
