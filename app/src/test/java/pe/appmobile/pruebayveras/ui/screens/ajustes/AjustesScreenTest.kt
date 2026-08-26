package pe.appmobile.pruebayveras.ui.screens.ajustes

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class AjustesScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `ajustes no revienta la app`() {
        compose.setContent {
            PruebaYVerasTheme { AjustesScreen() }
        }
        compose.waitForIdle()
    }
}
