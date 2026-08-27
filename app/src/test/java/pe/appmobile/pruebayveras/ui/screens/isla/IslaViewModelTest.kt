package pe.appmobile.pruebayveras.ui.screens.isla

import android.os.Looper
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.testutil.viewModelDeTest

@RunWith(RobolectricTestRunner::class)
class IslaViewModelTest {

    private val store = ViewModelStore()
    private var dbAbierta: AppDatabase? = null

    @After
    fun cerrarViewModel() {
        store.clear()
        dbAbierta?.close()
    }

    private fun esperarCarga(viewModel: IslaViewModel) {
        var pasadas = 0
        while (viewModel.estado.value.retos.isEmpty() && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(50)
            pasadas++
        }
    }

    @Test
    fun `correr la prueba cambiando dos variables no bloquea, y marca la prueba como injusta`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_marea") }
        esperarCarga(viewModel)

        viewModel.cambiarVariablePrueba("sal", 3)
        viewModel.cambiarVariablePrueba("volumenAgua", 500)
        viewModel.ejecutarPrueba()

        var pasadas = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(50)
            pasadas++
        }

        val resultado = viewModel.estado.value.ultimoResultado
        assertTrue("debe haber un resultado", resultado != null)
        assertFalse("dos variables cambiadas no es una prueba justa", resultado!!.fueJusta)
    }

    @Test
    fun `correr la prueba cambiando una sola variable es justa y guarda el intento`() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build().also { dbAbierta = it }
        val viewModel = viewModelDeTest(store, IslaViewModel::class.java) { IslaViewModel(db, "isla_marea") }
        esperarCarga(viewModel)

        viewModel.cambiarVariablePrueba("sal", 3)
        viewModel.ejecutarPrueba()

        var pasadas = 0
        while (viewModel.estado.value.ultimoResultado == null && pasadas < 100) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(50)
            pasadas++
        }

        assertTrue(viewModel.estado.value.ultimoResultado!!.fueJusta)
        val idRetoActual = viewModel.estado.value.retoActual!!.idReto
        val intentos = db.intentoDao().observarPorReto(idRetoActual).first()
        assertTrue(intentos.isNotEmpty())
    }
}
