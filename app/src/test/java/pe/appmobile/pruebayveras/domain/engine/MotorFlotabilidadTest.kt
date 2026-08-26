package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorFlotabilidadTest {

    @Test
    fun `sin sal el huevo se hunde`() {
        val resultado = MotorFlotabilidad.calcular(cucharadasDeSal = 0)
        assertFalse(resultado.flota)
        assertEquals(0f, resultado.alturaFlotacion, 0.001f)
    }

    @Test
    fun `con 3 cucharadas el huevo flota`() {
        val resultado = MotorFlotabilidad.calcular(cucharadasDeSal = 3)
        assertTrue(resultado.flota)
    }

    @Test
    fun `con 1 cucharada todavia no alcanza a flotar`() {
        val resultado = MotorFlotabilidad.calcular(cucharadasDeSal = 1)
        assertFalse(resultado.flota)
    }

    @Test
    fun `mas sal produce mayor altura de flotacion`() {
        val conPoca = MotorFlotabilidad.calcular(cucharadasDeSal = 3)
        val conMucha = MotorFlotabilidad.calcular(cucharadasDeSal = 8)
        assertTrue(conMucha.alturaFlotacion > conPoca.alturaFlotacion)
    }

    @Test
    fun `no admite cantidades negativas de sal`() {
        val resultado = MotorFlotabilidad.calcular(cucharadasDeSal = -2)
        assertEquals(0f, resultado.alturaFlotacion, 0.001f)
        assertFalse(resultado.flota)
    }

    @Test
    fun `la densidad del agua calculada es correcta`() {
        val densidad = MotorFlotabilidad.densidadAgua(cucharadasDeSal = 5)
        assertEquals(1.05f, densidad, 0.001f)
    }

    @Test
    fun `el volumen de agua no cambia la densidad ni la altura de flotacion`() {
        val conVolumenChico = MotorFlotabilidad.calcular(cucharadasDeSal = 3, volumenAguaMl = 150)
        val conVolumenGrande = MotorFlotabilidad.calcular(cucharadasDeSal = 3, volumenAguaMl = 500)
        assertEquals(conVolumenChico.alturaFlotacion, conVolumenGrande.alturaFlotacion, 0.001f)
    }
}
