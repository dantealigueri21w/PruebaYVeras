package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import java.util.Locale

/**
 * Qué pasó al correr la prueba: los dos números y hacia dónde se movió, con el mismo
 * vocabulario (Sube/Baja/No cambia) que la pregunta de tendencia final — antes de esto
 * "Correr la prueba" avanzaba en silencio al siguiente reto y no parecía haber pasado
 * nada. Fondo propio (no la imagen de la isla) para que se lea de verdad, igual que el
 * globo de Chirimbolo.
 */
@Composable
fun PanelResultado(
    resultadoControl: Float,
    resultadoPrueba: Float,
    onContinuar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tendencia = when {
        resultadoPrueba > resultadoControl -> stringResource(R.string.isla_tendencia_sube)
        resultadoPrueba < resultadoControl -> stringResource(R.string.isla_tendencia_baja)
        else -> stringResource(R.string.isla_tendencia_no_cambia)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.isla_resultado_titulo),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "${stringResource(R.string.isla_resultado_control)}: ${formatearResultado(resultadoControl)}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "${stringResource(R.string.isla_resultado_prueba)}: ${formatearResultado(resultadoPrueba)}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "${stringResource(R.string.isla_resultado_etiqueta_tendencia)} $tendencia",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Button(onClick = onContinuar) {
            Text(stringResource(R.string.isla_resultado_continuar))
        }
    }
}

private fun formatearResultado(valor: Float) = String.format(Locale.US, "%.1f", valor)
