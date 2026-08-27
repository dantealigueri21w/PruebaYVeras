package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class CuadernoScreenTest {

    @get:Rule
    val compose = createComposeRule()

    private val store = ViewModelStore()

    @After
    fun cerrarViewModel() {
        store.clear()
    }

    @Test
    fun `el cuaderno vacio no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = viewModelDeTest(store, CuadernoViewModel::class.java) { CuadernoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CuadernoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }

    @Test
    fun `el cuaderno con una pagina real no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        kotlinx.coroutines.runBlocking {
            db.paginaCuadernoDao().guardar(
                PaginaCuadernoEntity(
                    idReto = "reto_marea_facil",
                    tendenciaElegida = "SUBE",
                    tendenciaCorrecta = true,
                    timestamp = 1000L,
                ),
            )
        }
        val viewModel = viewModelDeTest(store, CuadernoViewModel::class.java) { CuadernoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CuadernoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }

    @Test
    fun `una pagina muestra el nombre real de la isla, no el id interno del reto`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        kotlinx.coroutines.runBlocking {
            db.paginaCuadernoDao().guardar(
                PaginaCuadernoEntity(
                    idReto = "reto_marea_facil",
                    tendenciaElegida = "SUBE",
                    tendenciaCorrecta = true,
                    timestamp = 1000L,
                ),
            )
        }
        val viewModel = viewModelDeTest(store, CuadernoViewModel::class.java) { CuadernoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CuadernoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()

        // Antes de este arreglo, la pagina mostraba literalmente el id interno
        // ("reto_marea_facil") en vez de un nombre que un niño pueda leer.
        compose.onNodeWithText("Isla de la Marea").assertIsDisplayed()
    }
}
