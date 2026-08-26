package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorEstaticaTest {

    @Test
    fun `sin frotar no atrae ningun papelito`() {
        assertEquals(0, MotorEstatica.papelitosAtraidos(frotadas = 0))
    }

    @Test
    fun `mas frotadas atrae mas papelitos`() {
        val pocas = MotorEstatica.papelitosAtraidos(frotadas = 3)
        val muchas = MotorEstatica.papelitosAtraidos(frotadas = 15)
        assertTrue(muchas > pocas)
    }

    @Test
    fun `el numero de papelitos atraidos tiene un tope de 10`() {
        val resultado = MotorEstatica.papelitosAtraidos(frotadas = 500)
        assertEquals(10, resultado)
    }

    @Test
    fun `frotadas negativas se tratan como cero`() {
        assertEquals(0, MotorEstatica.papelitosAtraidos(frotadas = -5))
    }

    @Test
    fun `nueve frotadas atraen tres papelitos segun la formula`() {
        assertEquals(3, MotorEstatica.papelitosAtraidos(frotadas = 9))
    }

    @Test
    fun `mas distancia atrae menos papelitos, con las mismas frotadas`() {
        val cerca = MotorEstatica.papelitosAtraidos(frotadas = 9, distanciaCm = 5)
        val lejos = MotorEstatica.papelitosAtraidos(frotadas = 9, distanciaCm = 15)
        assertTrue(lejos < cerca)
    }
}
