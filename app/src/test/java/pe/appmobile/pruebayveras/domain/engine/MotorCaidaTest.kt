package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.sqrt

class MotorCaidaTest {

    @Test
    fun `sin paracaidas el tiempo sigue la formula de caida libre`() {
        val tiempo = MotorCaida.calcular(alturaMetros = 20f, tieneParacaidas = false)
        val esperado = sqrt(2 * 20f / 9.8f)
        assertEquals(esperado, tiempo, 0.01f)
    }

    @Test
    fun `con paracaidas tarda mas que sin el, misma altura`() {
        val sinParacaidas = MotorCaida.calcular(alturaMetros = 20f, tieneParacaidas = false)
        val conParacaidas = MotorCaida.calcular(alturaMetros = 20f, tieneParacaidas = true)
        assertTrue(conParacaidas > sinParacaidas)
    }

    @Test
    fun `a mayor altura mayor tiempo de caida, con paracaidas igual`() {
        val bajo = MotorCaida.calcular(alturaMetros = 5f, tieneParacaidas = true)
        val alto = MotorCaida.calcular(alturaMetros = 40f, tieneParacaidas = true)
        assertTrue(alto > bajo)
    }

    @Test
    fun `altura cero no lanza excepcion y da tiempo cero`() {
        val tiempo = MotorCaida.calcular(alturaMetros = 0f, tieneParacaidas = false)
        assertEquals(0f, tiempo, 0.001f)
    }

    @Test
    fun `altura negativa se trata como cero`() {
        val tiempo = MotorCaida.calcular(alturaMetros = -10f, tieneParacaidas = false)
        assertEquals(0f, tiempo, 0.001f)
    }
}
