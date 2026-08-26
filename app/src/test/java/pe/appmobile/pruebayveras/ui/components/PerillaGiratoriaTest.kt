package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

/**
 * Reproduce el reporte real: "el medidor no se logra mover". Arrastra la perilla dos
 * veces seguidas (dos gestos separados, como haría un dedo real que suelta y vuelve a
 * arrastrar) y comprueba que el segundo arrastre sigue subiendo el valor desde donde
 * quedó, no desde cero.
 */
@RunWith(RobolectricTestRunner::class)
class PerillaGiratoriaTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `un segundo arrastre sigue subiendo el valor desde donde quedo`() {
        var valorGuardado = 0
        compose.setContent {
            var valor by remember { mutableIntStateOf(0) }
            valorGuardado = valor
            PruebaYVerasTheme {
                Box {
                    PerillaGiratoria(
                        valor = valor,
                        rango = 0..20,
                        etiqueta = "sal",
                        onValorCambia = { valor = it },
                    )
                }
            }
        }

        val perilla = compose.onNodeWithContentDescription("sal:", substring = true)

        perilla.performTouchInput { swipeUp(startY = centerY, endY = centerY - 600f) }
        compose.waitForIdle()
        val trasPrimerArrastre = valorGuardado
        assertTrue("el primer arrastre debe subir el valor por encima de 0", trasPrimerArrastre > 0)

        perilla.performTouchInput { swipeUp(startY = centerY, endY = centerY - 600f) }
        compose.waitForIdle()
        assertTrue(
            "el segundo arrastre debe seguir subiendo el valor desde $trasPrimerArrastre, " +
                "no quedarse pegado ahi (era $valorGuardado)",
            valorGuardado > trasPrimerArrastre,
        )
    }
}
