package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.pruebayveras.domain.model.TipoObstaculo

class MotorMagnetismoTest {

    @Test
    fun `con poco grosor el clip se mueve`() {
        val resultado = MotorMagnetismo.calcular(grosorMm = 2, obstaculo = TipoObstaculo.CARTON)
        assertTrue(resultado.clipSeMueve)
    }

    @Test
    fun `con mucho grosor el clip deja de moverse`() {
        val resultado = MotorMagnetismo.calcular(grosorMm = 8, obstaculo = TipoObstaculo.CARTON)
        assertFalse(resultado.clipSeMueve)
    }

    @Test
    fun `el grosor limite exacto es 5 mm, todavia se mueve`() {
        val resultado = MotorMagnetismo.calcular(grosorMm = 5, obstaculo = TipoObstaculo.CARTON)
        assertTrue(resultado.clipSeMueve)
    }

    @Test
    fun `el tipo de material casi no cambia el resultado, solo el grosor`() {
        val conCarton = MotorMagnetismo.calcular(grosorMm = 3, obstaculo = TipoObstaculo.CARTON)
        val conMetal = MotorMagnetismo.calcular(grosorMm = 3, obstaculo = TipoObstaculo.METAL_GRUESO)
        assertEquals(conCarton.clipSeMueve, conMetal.clipSeMueve)
    }

    @Test
    fun `grosor negativo se trata como cero`() {
        val resultado = MotorMagnetismo.calcular(grosorMm = -3, obstaculo = TipoObstaculo.CARTON)
        assertTrue(resultado.clipSeMueve)
    }
}
