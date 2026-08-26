package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

@RunWith(RobolectricTestRunner::class)
class CuadernoScreenTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `el cuaderno vacio no revienta la app`() {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        val viewModel = CuadernoViewModel(db)

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
                PaginaCuadernoEntity(idReto = "reto_marea_facil", tendenciaElegida = "SUBE", tendenciaCorrecta = true, timestamp = 1000L),
            )
        }
        val viewModel = CuadernoViewModel(db)

        compose.setContent {
            PruebaYVerasTheme { CuadernoScreen(viewModel = viewModel) }
        }
        compose.waitForIdle()
    }
}
