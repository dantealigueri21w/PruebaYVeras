package pe.appmobile.pruebayveras.ui.screens.archipielago

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
class ArchipielagoScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `el archipielago no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = ArchipielagoViewModel(db)

        compose.setContent {
            PruebaYVerasTheme { ArchipielagoScreen(viewModel = viewModel, onAbrirIsla = {}) }
        }
        compose.waitForIdle()
    }
}
