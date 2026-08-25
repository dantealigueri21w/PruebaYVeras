package pe.appmobile.pruebayveras.ui.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FormasPropiasTest {

    private val densidad = Density(1f)

    @Test
    fun `NotaRotaShape produce un contorno con puntos`() {
        val outline = NotaRotaShape.createOutline(Size(200f, 120f), LayoutDirection.Ltr, densidad)
        assertTrue(outline is Outline.Generic)
    }

    @Test
    fun `GloboDialogoShape produce un contorno con puntos`() {
        val outline = GloboDialogoShape.createOutline(Size(200f, 120f), LayoutDirection.Ltr, densidad)
        assertTrue(outline is Outline.Generic)
    }

    @Test
    fun `EtiquetaFrascoShape produce un contorno con puntos`() {
        val outline = EtiquetaFrascoShape.createOutline(Size(160f, 80f), LayoutDirection.Ltr, densidad)
        assertTrue(outline is Outline.Generic)
    }
}
