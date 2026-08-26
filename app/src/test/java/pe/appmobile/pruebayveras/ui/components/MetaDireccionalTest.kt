package pe.appmobile.pruebayveras.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class MetaDireccionalTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun `direccion SUBE muestra el texto de subir`() {
        compose.setContent { PruebaYVerasTheme { MetaDireccional(direccionEsperada = "SUBE") } }
        compose.onNodeWithText("Meta: que suba más que el control").assertExists()
    }

    @Test
    fun `direccion BAJA muestra el texto de bajar`() {
        compose.setContent { PruebaYVerasTheme { MetaDireccional(direccionEsperada = "BAJA") } }
        compose.onNodeWithText("Meta: que baje más que el control").assertExists()
    }

    @Test
    fun `direccion NO_CAMBIA muestra el texto de quedar igual`() {
        compose.setContent { PruebaYVerasTheme { MetaDireccional(direccionEsperada = "NO_CAMBIA") } }
        compose.onNodeWithText("Meta: que quede igual que el control").assertExists()
    }
}
