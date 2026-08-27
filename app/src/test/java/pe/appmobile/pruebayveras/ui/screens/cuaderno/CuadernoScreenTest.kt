package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
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
    private var dbAbierta: AppDatabase? = null

    @After
    fun cerrarViewModel() {
        store.clear()
        dbAbierta?.close()
    }

    @Test
    fun `el cuaderno vacio no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        val viewModel = viewModelDeTest(store, CuadernoViewModel::class.java) { CuadernoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CuadernoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }

    @Test
    fun `el cuaderno con una pagina real no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
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
            .allowMainThreadQueries().build().also { dbAbierta = it }
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
        compose.onNode(hasText("Isla de la Marea"), useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `una pagina con tendencia correcta muestra el mensaje de logro`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
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

        // Antes de este arreglo, el Cuaderno mostraba "¡Lo lograste!" sin importar
        // si tendenciaCorrecta era true o false.
        // useUnmergedTree: el texto vive dentro de un HorizontalPager de una sola
        // página, cuyo árbol de semántica a veces queda fusionado de forma distinta
        // en el frame de la aserción — visto de verdad como flake al correr la suite.
        compose.onNode(hasText("¡Lo lograste!"), useUnmergedTree = true).assertExists()
    }

    @Test
    fun `una pagina con tendencia incorrecta muestra el mensaje de aun no logrado`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        kotlinx.coroutines.runBlocking {
            db.paginaCuadernoDao().guardar(
                PaginaCuadernoEntity(
                    idReto = "reto_marea_facil",
                    tendenciaElegida = "BAJA",
                    tendenciaCorrecta = false,
                    timestamp = 1000L,
                ),
            )
        }
        val viewModel = viewModelDeTest(store, CuadernoViewModel::class.java) { CuadernoViewModel(db) }

        compose.setContent {
            PruebaYVerasTheme { CuadernoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()

        // Antes de este arreglo, este mensaje nunca se mostraba: el texto de
        // "logrado" aparecia siempre, sin leer tendenciaCorrecta.
        compose.onNode(hasText("Todavía no… ¡sigue investigando!"), useUnmergedTree = true).assertExists()
    }
}
