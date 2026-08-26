package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.domain.model.Montaje

/**
 * Dos mesas de montaje lado a lado, control y prueba, cada una con TODAS sus
 * variables visibles y tocables del lado de prueba — a diferencia de la mesa
 * anterior, aquí "correr la prueba" nunca bloquea: siempre ejecuta, sea o no una
 * prueba justa. Es tarea de quien la usa (IslaViewModel) decidir qué mostrar
 * después según MotorPruebaJusta. Cada bloque de texto va sobre [PanelLegible]
 * (riesgo técnico 3.1 de la ficha).
 */
@Composable
fun MesaDeTanteo(
    control: Montaje,
    prueba: Montaje,
    contenidoVariable: @Composable (nombre: String, montaje: Montaje, esControl: Boolean) -> Unit,
    onEjecutar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().padding(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column {
                PanelLegible { Text(stringResource(R.string.isla_montaje_control), style = MaterialTheme.typography.labelLarge) }
                control.variables.forEach { variable -> contenidoVariable(variable.nombre, control, true) }
            }
            Column {
                PanelLegible { Text(stringResource(R.string.isla_montaje_prueba), style = MaterialTheme.typography.labelLarge) }
                prueba.variables.forEach { variable -> contenidoVariable(variable.nombre, prueba, false) }
            }
        }

        Button(onClick = onEjecutar, modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(R.string.isla_boton_correr))
        }
    }
}
