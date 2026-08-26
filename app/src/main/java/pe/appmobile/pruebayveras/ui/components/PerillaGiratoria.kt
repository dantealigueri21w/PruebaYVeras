package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Cuanto hay que arrastrar, siempre, para recorrer un rango completo — sin importar
 * si son 20 pasos o 60. Con esto, cualquier perilla se juega igual de comodo.
 */
private val DISTANCIA_RANGO_COMPLETO = 260.dp

/**
 * Un dial que se gira arrastrando verticalmente: arriba sube el valor, abajo lo baja.
 * Reemplaza a `Slider` de Material (sección 3.1) — es un objeto del mundo (una perilla),
 * no una barra abstracta.
 */
@Composable
fun PerillaGiratoria(
    valor: Int,
    rango: IntRange,
    etiqueta: String,
    onValorCambia: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var arrastreAcumulado by remember { mutableFloatStateOf(0f) }
    val colorPrimario = MaterialTheme.colorScheme.primary
    val colorSecundario = MaterialTheme.colorScheme.secondary

    // pointerInput(rango) solo reinicia el gesto cuando cambia el rango, que es fijo
    // por variable: si el arrastre leyera "valor" y "onValorCambia" directamente, el
    // primer gesto los dejaria congelados para siempre en su valor inicial y la perilla
    // se quedaria pegada en 0 (o en el limite del rango) sin poder moverse de verdad.
    val valorActual = rememberUpdatedState(valor)
    val onValorCambiaActual = rememberUpdatedState(onValorCambia)

    Box(
        modifier = modifier
            .size(120.dp)
            .semantics { contentDescription = "$etiqueta: $valor" }
            .pointerInput(rango) {
                // Antes, 24px por paso era fijo sin importar el ancho del rango: un
                // rango de 0..60 (las variables continuas, ej. altura, distancia)
                // necesitaba un arrastre casi tres veces mas largo que uno de 0..20
                // para llegar al tope, y un arrastre comodo de un solo gesto se
                // quedaba a mitad de camino. Fijar la distancia TOTAL en vez del paso
                // hace que cualquier rango se recorra completo con el mismo largo de
                // arrastre.
                val amplitud = (rango.last - rango.first).coerceAtLeast(1)
                val pixelesPorPaso = DISTANCIA_RANGO_COMPLETO.toPx() / amplitud
                detectDragGestures(
                    onDragStart = { arrastreAcumulado = 0f },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        arrastreAcumulado -= dragAmount.y
                        val pasos = (arrastreAcumulado / pixelesPorPaso).roundToInt()
                        val actual = valorActual.value
                        val nuevo = (actual + pasos).coerceIn(rango.first, rango.last)
                        if (nuevo != actual) {
                            onValorCambiaActual.value(nuevo)
                            arrastreAcumulado = 0f
                        }
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(96.dp)) {
            val progreso = (valor - rango.first).toFloat() / (rango.last - rango.first).coerceAtLeast(1)
            drawArc(
                color = colorPrimario.copy(alpha = 0.25f),
                startAngle = 135f, sweepAngle = 270f, useCenter = false,
                style = Stroke(width = 14f),
            )
            drawArc(
                color = colorSecundario,
                startAngle = 135f, sweepAngle = 270f * progreso, useCenter = false,
                style = Stroke(width = 14f),
            )
        }
        Text(text = "$valor", style = MaterialTheme.typography.titleLarge, color = LocalContentColor.current)
    }
}
