package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R

/**
 * Qué pasó al tocar "¡Pruébalo!": si el resultado real cayó dentro de la zona
 * objetivo del reto — el logro que hacía falta, sección 1 del prompt maestro. Fondo
 * propio (no la imagen de la isla) para que se lea de verdad, igual que el globo de
 * Chirimbolo.
 */
@Composable
fun PanelResultado(
    resultadoReal: Float,
    valorObjetivo: Float,
    margenObjetivo: Float,
    logrado: Boolean,
    onContinuar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (logrado) stringResource(R.string.isla_logro_titulo) else stringResource(R.string.isla_logro_casi),
            style = MaterialTheme.typography.titleLarge,
            color = if (logrado) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface,
        )
        MedidorAcercamiento(resultadoReal = resultadoReal, valorObjetivo = valorObjetivo, margenObjetivo = margenObjetivo)
        Button(onClick = onContinuar) {
            Text(
                if (logrado) stringResource(R.string.isla_resultado_continuar) else stringResource(R.string.isla_logro_reintentar),
            )
        }
    }
}

/**
 * Una franja con la zona objetivo marcada y un punto donde cayó el resultado real —
 * no necesita saber nada del motor de la isla, solo compara dos números ya
 * calculados, así que sirve igual para las nueve islas sin casos especiales.
 */
@Composable
private fun MedidorAcercamiento(resultadoReal: Float, valorObjetivo: Float, margenObjetivo: Float, modifier: Modifier = Modifier) {
    val colorZona = MaterialTheme.colorScheme.tertiary
    val colorPunto = MaterialTheme.colorScheme.secondary
    val colorFranja = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .semantics {
                contentDescription = "Resultado $resultadoReal, meta $valorObjetivo más menos $margenObjetivo"
            },
    ) {
        // Ventana visible generosa alrededor de la meta, para que el punto siempre
        // quepa aunque el resultado real haya quedado lejos.
        val medioAncho = (margenObjetivo * 4f).coerceAtLeast(0.5f)
        val minimo = valorObjetivo - medioAncho
        val maximo = valorObjetivo + medioAncho
        val ancho = (maximo - minimo).coerceAtLeast(0.01f)

        fun posicionX(valor: Float): Float {
            val fraccion = ((valor - minimo) / ancho).coerceIn(0f, 1f)
            return fraccion * size.width
        }

        val alturaFranja = size.height * 0.4f
        val y = size.height / 2f

        drawLine(
            color = colorFranja.copy(alpha = 0.25f),
            start = Offset(0f, y), end = Offset(size.width, y),
            strokeWidth = alturaFranja,
        )
        drawLine(
            color = colorZona,
            start = Offset(posicionX(valorObjetivo - margenObjetivo), y),
            end = Offset(posicionX(valorObjetivo + margenObjetivo), y),
            strokeWidth = alturaFranja,
        )
        drawCircle(
            color = colorPunto,
            radius = size.height * 0.45f,
            center = Offset(posicionX(resultadoReal), y),
        )
    }
}
