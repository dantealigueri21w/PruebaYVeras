package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorEcoTest {

    @Test
    fun `el retardo crece con la distancia`() {
        val cerca = MotorEco.retardoSegundos(distanciaMetros = 17f)
        val lejos = MotorEco.retardoSegundos(distanciaMetros = 34f)
        assertTrue(lejos > cerca)
    }

    @Test
    fun `el retardo a 17 metros es de aproximadamente 0,1 segundos`() {
        val retardo = MotorEco.retardoSegundos(distanciaMetros = 17f)
        assertEquals(0.1f, retardo, 0.01f)
    }

    @Test
    fun `un material absorbente reduce la intensidad del eco`() {
        val sinAbsorbente = MotorEco.intensidad(distanciaMetros = 10f, materialAbsorbente = false)
        val conAbsorbente = MotorEco.intensidad(distanciaMetros = 10f, materialAbsorbente = true)
        assertTrue(conAbsorbente < sinAbsorbente)
    }

    @Test
    fun `a mayor distancia menor intensidad, mismo material`() {
        val cerca = MotorEco.intensidad(distanciaMetros = 5f, materialAbsorbente = false)
        val lejos = MotorEco.intensidad(distanciaMetros = 15f, materialAbsorbente = false)
        assertTrue(lejos < cerca)
    }

    @Test
    fun `la intensidad nunca es negativa`() {
        val intensidad = MotorEco.intensidad(distanciaMetros = 200f, materialAbsorbente = true)
        assertTrue(intensidad >= 0f)
    }
}
