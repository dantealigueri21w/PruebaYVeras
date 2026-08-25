package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorGerminacionTest {

    @Test
    fun `sin agua no germina aunque tenga luz`() {
        val crecimiento = MotorGerminacion.calcular(tieneAgua = false, tieneLuz = true, dias = 10)
        assertEquals(0f, crecimiento, 0.001f)
    }

    @Test
    fun `con agua y sin luz crece, pero menos que con luz`() {
        val sinLuz = MotorGerminacion.calcular(tieneAgua = true, tieneLuz = false, dias = 5)
        val conLuz = MotorGerminacion.calcular(tieneAgua = true, tieneLuz = true, dias = 5)
        assertTrue(sinLuz > 0f)
        assertTrue(conLuz > sinLuz)
    }

    @Test
    fun `a mas dias mayor crecimiento, con las mismas condiciones`() {
        val pocos = MotorGerminacion.calcular(tieneAgua = true, tieneLuz = true, dias = 2)
        val muchos = MotorGerminacion.calcular(tieneAgua = true, tieneLuz = true, dias = 8)
        assertTrue(muchos > pocos)
    }

    @Test
    fun `cero dias da crecimiento cero sin importar agua o luz`() {
        val crecimiento = MotorGerminacion.calcular(tieneAgua = true, tieneLuz = true, dias = 0)
        assertEquals(0f, crecimiento, 0.001f)
    }

    @Test
    fun `dias negativos se tratan como cero`() {
        val crecimiento = MotorGerminacion.calcular(tieneAgua = true, tieneLuz = true, dias = -3)
        assertEquals(0f, crecimiento, 0.001f)
    }

    @Test
    fun `sin agua y sin luz tampoco germina`() {
        val crecimiento = MotorGerminacion.calcular(tieneAgua = false, tieneLuz = false, dias = 10)
        assertEquals(0f, crecimiento, 0.001f)
    }
}
