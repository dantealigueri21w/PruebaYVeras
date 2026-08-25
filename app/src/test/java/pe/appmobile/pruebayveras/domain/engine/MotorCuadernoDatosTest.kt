package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class MotorCuadernoDatosTest {

    @Test
    fun `detecta tendencia ascendente cuando cada dato es mayor al anterior`() {
        val datos = listOf(2f, 5f, 9f)
        assertEquals(Tendencia.SUBE, MotorCuadernoDatos.tendenciaReal(datos))
    }

    @Test
    fun `detecta tendencia descendente cuando cada dato es menor al anterior`() {
        val datos = listOf(9f, 5f, 2f)
        assertEquals(Tendencia.BAJA, MotorCuadernoDatos.tendenciaReal(datos))
    }

    @Test
    fun `detecta que no cambia cuando los datos son casi iguales`() {
        val datos = listOf(5.0f, 5.1f, 4.9f)
        assertEquals(Tendencia.NO_CAMBIA, MotorCuadernoDatos.tendenciaReal(datos))
    }

    @Test
    fun `con un solo dato no hay tendencia que armar`() {
        assertEquals(Tendencia.NO_CAMBIA, MotorCuadernoDatos.tendenciaReal(listOf(5f)))
    }

    @Test
    fun `con lista vacia no hay tendencia que armar`() {
        assertEquals(Tendencia.NO_CAMBIA, MotorCuadernoDatos.tendenciaReal(emptyList()))
    }

    @Test
    fun `la conclusion del nino es correcta si coincide con la tendencia real`() {
        val datos = listOf(1f, 4f, 8f)
        assertEquals(true, MotorCuadernoDatos.conclusionEsCorrecta(datos, Tendencia.SUBE))
        assertEquals(false, MotorCuadernoDatos.conclusionEsCorrecta(datos, Tendencia.BAJA))
    }
}
