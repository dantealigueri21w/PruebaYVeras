package pe.appmobile.pruebayveras.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.pruebayveras.data.seed.SemillaFrases
import pe.appmobile.pruebayveras.data.seed.SemillaInsignias
import pe.appmobile.pruebayveras.data.seed.SemillaIslas
import pe.appmobile.pruebayveras.data.seed.SemillaPiezas
import pe.appmobile.pruebayveras.data.seed.SemillaRetos

class SemillaContractTest {

    @Test
    fun `hay exactamente 9 islas`() {
        assertEquals(9, SemillaIslas.islas.size)
    }

    @Test
    fun `hay exactamente 27 retos, 3 por isla`() {
        assertEquals(27, SemillaRetos.retos.size)
        SemillaIslas.islas.forEach { isla ->
            val retosDeEstaIsla = SemillaRetos.retos.filter { it.idIsla == isla.idIsla }
            assertEquals("la isla ${isla.idIsla} debe tener 3 retos", 3, retosDeEstaIsla.size)
        }
    }

    @Test
    fun `cada reto declara una variable independiente no vacia`() {
        SemillaRetos.retos.forEach { reto ->
            assertTrue("el reto ${reto.idReto} no declara variable", reto.variableIndependiente.isNotBlank())
        }
    }

    @Test
    fun `los 27 retos tienen direccion esperada valida y dato cientifico no vacio`() {
        val direccionesValidas = setOf("SUBE", "BAJA", "NO_CAMBIA")
        SemillaRetos.retos.forEach { reto ->
            assertTrue("${reto.idReto} tiene direccionEsperada inválida", reto.direccionEsperada in direccionesValidas)
            assertTrue("${reto.idReto} no tiene datoCientifico", reto.datoCientifico.isNotBlank())
        }
    }

    @Test
    fun `dos islas empiezan sin requisito de desbloqueo, marea y viento`() {
        val sinRequisito = SemillaIslas.islas.filter { it.requisitoDesbloqueo == null }
        assertEquals(2, sinRequisito.size)
        assertTrue(sinRequisito.any { it.idIsla == "isla_marea" })
        assertTrue(sinRequisito.any { it.idIsla == "isla_viento" })
    }

    @Test
    fun `todos los ids de reto son unicos`() {
        val ids = SemillaRetos.retos.map { it.idReto }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `hay exactamente 9 piezas de chirimbolo, una por isla`() {
        assertEquals(9, SemillaPiezas.piezas.size)
        val idsIslasConPieza = SemillaPiezas.piezas.map { it.idIsla }.toSet()
        assertEquals(SemillaIslas.islas.map { it.idIsla }.toSet(), idsIslasConPieza)
    }

    @Test
    fun `hay exactamente 12 insignias`() {
        assertEquals(12, SemillaInsignias.insignias.size)
    }

    @Test
    fun `hay exactamente 30 frases de chirimbolo`() {
        assertEquals(30, SemillaFrases.frases.size)
    }
}
