package pe.appmobile.pruebayveras.ui.screens.perfil

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class PerfilScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()

    @After
    fun cerrarViewModel() {
        store.clear()
    }

    @Test
    fun `escribir un alias lo guarda de verdad en Room`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, PerfilViewModel::class.java) { PerfilViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { PerfilScreen(viewModel = viewModel) }
        }

        compose.onNodeWithText("Tu alias de explorador").performTextInput("Exploradora")
        compose.waitForIdle()

        var pasadas = 0
        var perfil = runBlocking { db.perfilDao().observar().first() }
        while (perfil?.alias != "Exploradora" && pasadas < 100) {
            Thread.sleep(50)
            perfil = runBlocking { db.perfilDao().observar().first() }
            pasadas++
        }

        assertEquals("Exploradora", perfil?.alias)
    }
}
