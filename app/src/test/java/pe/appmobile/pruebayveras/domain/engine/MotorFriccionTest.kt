package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.pruebayveras.domain.model.TipoSuperficie

class MotorFriccionTest {

    @Test
    fun `una rampa lisa deja recorrer mas distancia que una rugosa, misma altura`() {
        val lisa = MotorFriccion.distanciaCm(alturaCm = 30f, superficie = TipoSuperficie.LISA)
        val rugosa = MotorFriccion.distanciaCm(alturaCm = 30f, superficie = TipoSuperficie.RUGOSA)
        assertTrue(lisa > rugosa)
    }

    @Test
    fun `a mayor altura mayor distancia, con la misma superficie`() {
        val baja = MotorFriccion.distanciaCm(alturaCm = 10f, superficie = TipoSuperficie.LISA)
        val alta = MotorFriccion.distanciaCm(alturaCm = 50f, superficie = TipoSuperficie.LISA)
        assertTrue(alta > baja)
    }

    @Test
    fun `altura cero da distancia cero en cualquier superficie`() {
        assertEquals(0f, MotorFriccion.distanciaCm(alturaCm = 0f, superficie = TipoSuperficie.LISA), 0.001f)
        assertEquals(0f, MotorFriccion.distanciaCm(alturaCm = 0f, superficie = TipoSuperficie.RUGOSA), 0.001f)
    }

    @Test
    fun `altura negativa se trata como cero`() {
        val distancia = MotorFriccion.distanciaCm(alturaCm = -20f, superficie = TipoSuperficie.LISA)
        assertEquals(0f, distancia, 0.001f)
    }

    @Test
    fun `el coeficiente de friccion de cada superficie es el esperado`() {
        assertEquals(0.1f, MotorFriccion.coeficiente(TipoSuperficie.LISA), 0.001f)
        assertEquals(0.6f, MotorFriccion.coeficiente(TipoSuperficie.RUGOSA), 0.001f)
    }

    @Test
    fun `la diferencia entre lisa y rugosa se mantiene en distintas alturas`() {
        val alturas = listOf(10f, 20f, 40f)
        alturas.forEach { altura ->
            val lisa = MotorFriccion.distanciaCm(altura, TipoSuperficie.LISA)
            val rugosa = MotorFriccion.distanciaCm(altura, TipoSuperficie.RUGOSA)
            assertTrue("a $altura cm lisa debe superar a rugosa", lisa > rugosa)
        }
    }
}
