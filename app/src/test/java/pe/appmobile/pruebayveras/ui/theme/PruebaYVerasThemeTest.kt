package pe.appmobile.pruebayveras.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Reproduce el reporte real: "las letras no se logran ver, se ven negras". Sin un
 * Surface que propague el color de contenido del tema, todo Text() sin color explicito
 * queda fijo en el negro por defecto de Material (LocalContentColor), sin importar el
 * fondo — invisible sobre los paneles y el tema oscuro de la propia app.
 */
@RunWith(RobolectricTestRunner::class)
class PruebaYVerasThemeTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `el texto sin color explicito usa el color de contenido del tema oscuro, no negro fijo`() {
        var colorLeido = Color.Unspecified
        compose.setContent {
            PruebaYVerasTheme(darkTheme = true) {
                colorLeido = LocalContentColor.current
                Text("hola")
            }
        }
        compose.waitForIdle()

        assertNotEquals(
            "el texto quedaba negro fijo porque el tema nunca envolvia el contenido en un Surface",
            Color.Black,
            colorLeido,
        )
        assertEquals(PapelDeCampo, colorLeido)
    }

    @Test
    fun `el texto sin color explicito usa el color de contenido del tema claro, no negro fijo por accidente`() {
        var colorLeido = Color.Unspecified
        compose.setContent {
            PruebaYVerasTheme(darkTheme = false) {
                colorLeido = LocalContentColor.current
                Text("hola")
            }
        }
        compose.waitForIdle()

        assertEquals(TintaCasiNegra, colorLeido)
    }
}
