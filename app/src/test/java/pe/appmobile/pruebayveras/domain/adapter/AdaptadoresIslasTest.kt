package pe.appmobile.pruebayveras.domain.adapter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.TipoObstaculo
import pe.appmobile.pruebayveras.domain.model.TipoSuperficie
import pe.appmobile.pruebayveras.domain.model.Variable

class AdaptadoresIslasTest {

    @Test
    fun `marea calcula la altura de flotacion con 3 cucharadas de sal`() {
        val montaje = Montaje(listOf(Variable("sal", 3), Variable("volumenAgua", 250)))
        assertTrue(AdaptadorMarea.calcular(montaje) >= 0f)
    }

    @Test
    fun `viento calcula mayor tiempo con paracaidas que sin el`() {
        val sinParacaidas = Montaje(listOf(Variable("paracaidas", false), Variable("altura", 20f)))
        val conParacaidas = Montaje(listOf(Variable("paracaidas", true), Variable("altura", 20f)))
        assertTrue(AdaptadorViento.calcular(conParacaidas) > AdaptadorViento.calcular(sinParacaidas))
    }

    @Test
    fun `jardin no germina sin agua`() {
        val montaje = Montaje(listOf(Variable("agua", false), Variable("luz", true), Variable("dias", 10)))
        assertEquals(0f, AdaptadorJardin.calcular(montaje), 0.001f)
    }

    @Test
    fun `cueva da mayor intensidad de eco cerca que lejos`() {
        val cerca = Montaje(listOf(Variable("distancia", 5f), Variable("material", false)))
        val lejos = Montaje(listOf(Variable("distancia", 20f), Variable("material", false)))
        assertTrue(AdaptadorCueva.calcular(cerca) > AdaptadorCueva.calcular(lejos))
    }

    @Test
    fun `faro atrae mas papelitos con mas frotadas`() {
        val pocas = Montaje(listOf(Variable("frotadas", 3), Variable("distancia", 5)))
        val muchas = Montaje(listOf(Variable("frotadas", 15), Variable("distancia", 5)))
        assertTrue(AdaptadorFaro.calcular(muchas) > AdaptadorFaro.calcular(pocas))
    }

    @Test
    fun `olas disuelve mas rapido con agua tibia, tiempo menor`() {
        val fria = Montaje(listOf(Variable("temperatura", 5), Variable("azucar", 10)))
        val tibia = Montaje(listOf(Variable("temperatura", 45), Variable("azucar", 10)))
        assertTrue(AdaptadorOlas.calcular(tibia) < AdaptadorOlas.calcular(fria))
    }

    @Test
    fun `risco recorre mas distancia en superficie lisa que rugosa`() {
        val lisa = Montaje(listOf(Variable("superficie", TipoSuperficie.LISA), Variable("altura", 30f)))
        val rugosa = Montaje(listOf(Variable("superficie", TipoSuperficie.RUGOSA), Variable("altura", 30f)))
        assertTrue(AdaptadorRisco.calcular(lisa) > AdaptadorRisco.calcular(rugosa))
    }

    @Test
    fun `iman deja de mover el clip con mucho grosor`() {
        val delgado = Montaje(listOf(Variable("grosor", 2), Variable("material", TipoObstaculo.CARTON)))
        val grueso = Montaje(listOf(Variable("grosor", 8), Variable("material", TipoObstaculo.CARTON)))
        assertEquals(1f, AdaptadorIman.calcular(delgado), 0.001f)
        assertEquals(0f, AdaptadorIman.calcular(grueso), 0.001f)
    }

    @Test
    fun `reflejo gana mas temperatura con color oscuro`() {
        val oscuro = Montaje(listOf(Variable("color", true), Variable("minutos", 10)))
        val claro = Montaje(listOf(Variable("color", false), Variable("minutos", 10)))
        assertTrue(AdaptadorReflejo.calcular(oscuro) > AdaptadorReflejo.calcular(claro))
    }
}
