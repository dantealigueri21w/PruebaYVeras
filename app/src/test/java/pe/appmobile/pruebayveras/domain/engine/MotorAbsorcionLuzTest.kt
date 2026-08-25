package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorAbsorcionLuzTest {

    @Test
    fun `la tela oscura se calienta mas que la clara, mismo tiempo al sol`() {
        val oscura = MotorAbsorcionLuz.temperaturaGanada(esOscuro = true, minutosAlSol = 10)
        val clara = MotorAbsorcionLuz.temperaturaGanada(esOscuro = false, minutosAlSol = 10)
        assertTrue(oscura > clara)
    }

    @Test
    fun `mas tiempo al sol da mas temperatura ganada, mismo color`() {
        val poco = MotorAbsorcionLuz.temperaturaGanada(esOscuro = true, minutosAlSol = 3)
        val mucho = MotorAbsorcionLuz.temperaturaGanada(esOscuro = true, minutosAlSol = 20)
        assertTrue(mucho > poco)
    }

    @Test
    fun `cero minutos al sol no gana temperatura`() {
        val resultado = MotorAbsorcionLuz.temperaturaGanada(esOscuro = true, minutosAlSol = 0)
        assertEquals(0f, resultado, 0.001f)
    }

    @Test
    fun `minutos negativos se tratan como cero`() {
        val resultado = MotorAbsorcionLuz.temperaturaGanada(esOscuro = true, minutosAlSol = -5)
        assertEquals(0f, resultado, 0.001f)
    }

    @Test
    fun `a 10 minutos la tela oscura gana 8 grados segun la formula`() {
        val resultado = MotorAbsorcionLuz.temperaturaGanada(esOscuro = true, minutosAlSol = 10)
        assertEquals(8f, resultado, 0.001f)
    }
}
