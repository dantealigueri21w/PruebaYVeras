package pe.appmobile.pruebayveras.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.entity.RetoEntity
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class AyudaFlotanteTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun `tocar el boton de ayuda muestra una pista con la variable del reto`() {
        val reto = RetoEntity("r1", "isla_marea", "FACIL", "texto", "sal", "SUBE", "dato", false)
        compose.setContent { PruebaYVerasTheme { AyudaFlotante(reto = reto) } }
        compose.onNodeWithText("Una pista").assertDoesNotExist()
        compose.onNodeWithContentDescription("Pedir ayuda").performClick()
        compose.onNodeWithText("Una pista").assertExists()
        compose.onNodeWithText("Prueba a cambiar solo sal esta vez, y deja todo lo demás exactamente igual que en el Control.").assertExists()
    }
}
