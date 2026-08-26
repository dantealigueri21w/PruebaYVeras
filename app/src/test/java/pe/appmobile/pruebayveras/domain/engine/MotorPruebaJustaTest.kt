package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable

class MotorPruebaJustaTest {

    @Test
    fun `montajes que difieren en exactamente una variable son una prueba justa`() {
        val control = Montaje(listOf(Variable("sal", 0), Variable("volumenAgua", 200)))
        val prueba = Montaje(listOf(Variable("sal", 3), Variable("volumenAgua", 200)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertTrue(resultado.esJusta)
        assertEquals(listOf("sal"), resultado.variablesDistintas)
    }

    @Test
    fun `montajes que difieren en dos variables no son una prueba justa`() {
        val control = Montaje(listOf(Variable("sal", 0), Variable("volumenAgua", 200)))
        val prueba = Montaje(listOf(Variable("sal", 3), Variable("volumenAgua", 250)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertFalse(resultado.esJusta)
        assertEquals(setOf("sal", "volumenAgua"), resultado.variablesDistintas.toSet())
    }

    @Test
    fun `montajes identicos no son una prueba justa porque no hay variable a probar`() {
        val control = Montaje(listOf(Variable("sal", 0), Variable("volumenAgua", 200)))
        val prueba = Montaje(listOf(Variable("sal", 0), Variable("volumenAgua", 200)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertFalse(resultado.esJusta)
        assertTrue(resultado.variablesDistintas.isEmpty())
    }

    @Test
    fun `detecta la variable distinta sin importar el orden en que se declaran`() {
        val control = Montaje(listOf(Variable("volumenAgua", 200), Variable("sal", 0)))
        val prueba = Montaje(listOf(Variable("sal", 3), Variable("volumenAgua", 200)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertTrue(resultado.esJusta)
        assertEquals(listOf("sal"), resultado.variablesDistintas)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lanza excepcion si los montajes declaran variables distintas entre si`() {
        val control = Montaje(listOf(Variable("sal", 0)))
        val prueba = Montaje(listOf(Variable("temperatura", 20)))

        MotorPruebaJusta.evaluar(control, prueba)
    }

    @Test
    fun `funciona con un solo par de variables, el caso minimo`() {
        val control = Montaje(listOf(Variable("frotadas", 0)))
        val prueba = Montaje(listOf(Variable("frotadas", 10)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertTrue(resultado.esJusta)
        assertEquals(listOf("frotadas"), resultado.variablesDistintas)
    }

    @Test
    fun `compara valores booleanos correctamente, no solo numeros`() {
        val control = Montaje(listOf(Variable("tieneJabon", false)))
        val prueba = Montaje(listOf(Variable("tieneJabon", true)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertTrue(resultado.esJusta)
    }

    @Test
    fun `compara enums correctamente`() {
        val control = Montaje(listOf(Variable("superficie", pe.appmobile.pruebayveras.domain.model.TipoSuperficie.LISA)))
        val prueba = Montaje(listOf(Variable("superficie", pe.appmobile.pruebayveras.domain.model.TipoSuperficie.RUGOSA)))

        val resultado = MotorPruebaJusta.evaluar(control, prueba)

        assertTrue(resultado.esJusta)
        assertEquals(listOf("superficie"), resultado.variablesDistintas)
    }
}
