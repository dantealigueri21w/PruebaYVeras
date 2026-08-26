package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorDisolucionTest {

    @Test
    fun `el agua tibia disuelve el azucar mas rapido que el agua fria`() {
        val fria = MotorDisolucion.tiempoSegundos(temperaturaC = 5)
        val tibia = MotorDisolucion.tiempoSegundos(temperaturaC = 45)
        assertTrue(tibia < fria)
    }

    @Test
    fun `el tiempo nunca baja de 5 segundos, ni con agua muy caliente`() {
        val tiempo = MotorDisolucion.tiempoSegundos(temperaturaC = 90)
        assertEquals(5, tiempo)
    }

    @Test
    fun `a 5 grados el tiempo es de 55 segundos segun la formula`() {
        assertEquals(55, MotorDisolucion.tiempoSegundos(temperaturaC = 5))
    }

    @Test
    fun `temperaturas negativas se tratan como cero grados`() {
        val aCero = MotorDisolucion.tiempoSegundos(temperaturaC = 0)
        val aNegativa = MotorDisolucion.tiempoSegundos(temperaturaC = -10)
        assertEquals(aCero, aNegativa)
    }

    @Test
    fun `mas azucar tarda mas en disolverse, a la misma temperatura`() {
        val pocaAzucar = MotorDisolucion.tiempoSegundos(temperaturaC = 20, cantidadAzucarG = 10)
        val muchaAzucar = MotorDisolucion.tiempoSegundos(temperaturaC = 20, cantidadAzucarG = 30)
        assertTrue(muchaAzucar > pocaAzucar)
    }
}
