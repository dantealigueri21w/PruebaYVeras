package pe.appmobile.pruebayveras.ui.screens.cobertizo

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.repository.CienciaLabRepository
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class CobertizoScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `el cobertizo no revienta la app, incluso vacio`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = CobertizoViewModel(db)

        compose.setContent {
            PruebaYVerasTheme { CobertizoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }

    @Test
    fun `el cobertizo no revienta la app con las 9 piezas sembradas`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        kotlinx.coroutines.runBlocking { CienciaLabRepository(db).sembrarSiEsPrimeraVez() }
        val viewModel = CobertizoViewModel(db)

        compose.setContent {
            PruebaYVerasTheme { CobertizoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }
}
