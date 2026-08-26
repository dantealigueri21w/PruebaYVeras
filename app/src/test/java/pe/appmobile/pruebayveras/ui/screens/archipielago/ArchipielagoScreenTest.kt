package pe.appmobile.pruebayveras.ui.screens.archipielago

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
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class ArchipielagoScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()

    @After
    fun cerrarViewModel() {
        store.clear()
    }

    @Test
    fun `el archipielago no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, ArchipielagoViewModel::class.java) { ArchipielagoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { ArchipielagoScreen(viewModel = viewModel, onAbrirIsla = {}) }
        }
        compose.waitForIdle()
    }

    @Test
    fun `los cuatro accesos de la esquina disparan su callback real`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, ArchipielagoViewModel::class.java) { ArchipielagoViewModel(db) }

        var abrioCuaderno = false
        var abrioCobertizo = false
        var abrioPerfil = false
        var abrioAjustes = false

        compose.setContent {
            PruebaYVerasTheme {
                ArchipielagoScreen(
                    viewModel = viewModel,
                    onAbrirIsla = {},
                    onAbrirCuaderno = { abrioCuaderno = true },
                    onAbrirCobertizo = { abrioCobertizo = true },
                    onAbrirPerfil = { abrioPerfil = true },
                    onAbrirAjustes = { abrioAjustes = true },
                )
            }
        }
        compose.waitForIdle()

        compose.onNodeWithContentDescription("Abrir el Cuaderno de Campo").performClick()
        compose.onNodeWithContentDescription("Abrir el Cobertizo de Chirimbolo").performClick()
        compose.onNodeWithContentDescription("Abrir tu perfil").performClick()
        compose.onNodeWithContentDescription("Abrir Ajustes").performClick()

        assertTrue(abrioCuaderno)
        assertTrue(abrioCobertizo)
        assertTrue(abrioPerfil)
        assertTrue(abrioAjustes)
    }
}
