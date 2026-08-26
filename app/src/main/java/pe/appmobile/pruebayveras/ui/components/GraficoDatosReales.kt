package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * El gráfico del Cuaderno de Campo: los resultados reales de `intento` (Room), en el
 * orden en que se corrieron — nunca datos de ejemplo. Un punto por corrida, unidos por
 * una línea, escalados a su propio mínimo y máximo.
 */
@Composable
fun GraficoDatosReales(datos: List<Float>, descripcion: String, modifier: Modifier = Modifier) {
    val colorLinea = MaterialTheme.colorScheme.primary
    val colorPunto = MaterialTheme.colorScheme.secondary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .semantics { contentDescription = descripcion },
    ) {
        if (datos.isEmpty()) return@Canvas

        val minimo = datos.min()
        val maximo = datos.max()
        val rango = (maximo - minimo).coerceAtLeast(0.01f)
        val margen = 16f
        val anchoUtil = (size.width - margen * 2).coerceAtLeast(1f)
        val altoUtil = (size.height - margen * 2).coerceAtLeast(1f)

        val puntos = datos.mapIndexed { indice, valor ->
            val x = if (datos.size == 1) size.width / 2f else margen + anchoUtil * indice / (datos.size - 1)
            val y = margen + altoUtil * (1f - (valor - minimo) / rango)
            Offset(x, y)
        }

        for (i in 0 until puntos.size - 1) {
            drawLine(color = colorLinea, start = puntos[i], end = puntos[i + 1], strokeWidth = 5f)
        }
        puntos.forEach { punto ->
            drawCircle(color = colorPunto, radius = 10f, center = punto)
        }
    }
}
