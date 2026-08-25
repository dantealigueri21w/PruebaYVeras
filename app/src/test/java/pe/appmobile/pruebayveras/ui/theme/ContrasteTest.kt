package pe.appmobile.pruebayveras.ui.theme

import org.junit.Assert.assertTrue
import org.junit.Test

class ContrasteTest {

    private fun luminancia(hex: String): Double {
        val c = hex.removePrefix("#")
        val r = c.substring(0, 2).toInt(16) / 255.0
        val g = c.substring(2, 4).toInt(16) / 255.0
        val b = c.substring(4, 6).toInt(16) / 255.0
        fun f(x: Double) = if (x <= 0.03928) x / 12.92 else Math.pow((x + 0.055) / 1.055, 2.4)
        return 0.2126 * f(r) + 0.7152 * f(g) + 0.0722 * f(b)
    }

    private fun contraste(c1: String, c2: String): Double {
        val l1 = luminancia(c1)
        val l2 = luminancia(c2)
        val (lMax, lMin) = if (l1 > l2) l1 to l2 else l2 to l1
        return (lMax + 0.05) / (lMin + 0.05)
    }

    @Test
    fun `todos los pares del tema claro pasan 4,5 a 1`() {
        assertTrue(contraste("#F4EDE0", "#1B2E35") >= 4.5)
        assertTrue(contraste("#FBF6EE", "#1B2E35") >= 4.5)
        assertTrue(contraste("#1E6E8C", "#FFFFFF") >= 4.5)
        assertTrue(contraste("#D97A3D", "#1B2E35") >= 4.5)
        assertTrue(contraste("#8FB85C", "#1B2E35") >= 4.5)
        assertTrue(contraste("#B3413F", "#FFFFFF") >= 4.5)
    }

    @Test
    fun `todos los pares del tema oscuro pasan 4,5 a 1`() {
        assertTrue(contraste("#12232A", "#F4EDE0") >= 4.5)
        assertTrue(contraste("#1C333C", "#F4EDE0") >= 4.5)
        assertTrue(contraste("#6FB2D6", "#12232A") >= 4.5)
        assertTrue(contraste("#E8A46E", "#12232A") >= 4.5)
        assertTrue(contraste("#B7D98C", "#12232A") >= 4.5)
        assertTrue(contraste("#E6A3A0", "#3B0D0C") >= 4.5)
    }

    @Test
    fun `texto blanco sobre naranja secundario no cumple, por eso no se usa`() {
        assertTrue(contraste("#D97A3D", "#FFFFFF") < 4.5)
    }
}
