package pe.appmobile.pruebayveras.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorProgresoTest {

    @Test
    fun `una isla se desbloquea cuando su isla previa esta en la lista de completadas`() {
        val completadas = setOf("isla_marea")
        assertTrue(MotorProgreso.islaDesbloqueada("isla_jardin", requisito = "isla_marea", completadas))
    }

    @Test
    fun `una isla sin requisito siempre esta desbloqueada`() {
        assertTrue(MotorProgreso.islaDesbloqueada("isla_marea", requisito = null, completadas = emptySet()))
    }

    @Test
    fun `una isla con requisito no cumplido permanece bloqueada`() {
        assertFalse(MotorProgreso.islaDesbloqueada("isla_jardin", requisito = "isla_marea", completadas = emptySet()))
    }

    @Test
    fun `chirimbolo completo cuando las 9 piezas estan confirmadas`() {
        val piezasConfirmadas = (1..9).map { "pieza_$it" }.toSet()
        assertTrue(MotorProgreso.chirimboloCompleto(piezasConfirmadas))
    }

    @Test
    fun `chirimbolo no esta completo con menos de 9 piezas`() {
        val piezasConfirmadas = (1..5).map { "pieza_$it" }.toSet()
        assertFalse(MotorProgreso.chirimboloCompleto(piezasConfirmadas))
    }

    @Test
    fun `la racha aumenta si se jugo hoy y tambien ayer`() {
        val nuevaRacha = MotorProgreso.actualizarRacha(rachaActual = 4, diasDesdeUltimoJuego = 1)
        assertEquals(5, nuevaRacha)
    }

    @Test
    fun `la racha se reinicia a 1 si paso mas de un dia`() {
        val nuevaRacha = MotorProgreso.actualizarRacha(rachaActual = 4, diasDesdeUltimoJuego = 3)
        assertEquals(1, nuevaRacha)
    }

    @Test
    fun `jugar dos veces el mismo dia no cambia la racha`() {
        val nuevaRacha = MotorProgreso.actualizarRacha(rachaActual = 4, diasDesdeUltimoJuego = 0)
        assertEquals(4, nuevaRacha)
    }
}
