package pe.appmobile.pruebayveras.ui.screens.cobertizo

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.repository.CienciaLabRepository
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class CobertizoScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()
    private var dbAbierta: AppDatabase? = null

    @After
    fun cerrarViewModel() {
        store.clear()
        dbAbierta?.close()
    }

    @Test
    fun `el cobertizo no revienta la app, incluso vacio`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        val viewModel = viewModelDeTest(store, CobertizoViewModel::class.java) { CobertizoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CobertizoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }

    @Test
    fun `el boton de volver dispara su callback real`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        val viewModel = viewModelDeTest(store, CobertizoViewModel::class.java) { CobertizoViewModel(db) }
        var volvio = false

        compose.setContent {
            PruebaYVerasTheme { CobertizoScreen(viewModel = viewModel, onVolver = { volvio = true }) }
        }
        compose.waitForIdle()

        compose.onNodeWithContentDescription("Volver").performClick()
        assertTrue(volvio)
    }

    @Test
    fun `el cobertizo no revienta la app con las 9 piezas sembradas`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        kotlinx.coroutines.runBlocking { CienciaLabRepository(db).sembrarSiEsPrimeraVez() }
        val viewModel = viewModelDeTest(store, CobertizoViewModel::class.java) { CobertizoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CobertizoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }
}
