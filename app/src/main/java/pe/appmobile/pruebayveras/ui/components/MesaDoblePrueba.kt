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
import pe.appmobile.pruebayveras.domain.engine.MotorPruebaJusta
import pe.appmobile.pruebayveras.domain.model.Montaje

/**
 * Dos mesas de montaje, lado a lado: control y prueba. Antes de "correr", valida con
 * MotorPruebaJusta que solo difieran en una variable. Es la pieza reutilizable que
 * usan las nueve islas (sección 3.1: navegación y mecánica propias; el botón de
 * ejecutar sí puede ser un Button de Material porque es una acción de confirmación,
 * no la mecánica en sí).
 */
@Composable
fun MesaDoblePrueba(
    control: Montaje,
    prueba: Montaje,
    contenidoVariable: @Composable (nombre: String, montaje: Montaje, esControl: Boolean) -> Unit,
    onEjecutar: () -> Unit,
    onPruebaInjusta: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().padding(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column {
                Text(stringResource(R.string.isla_montaje_control), style = MaterialTheme.typography.labelLarge)
                control.variables.forEach { variable -> contenidoVariable(variable.nombre, control, true) }
            }
            Column {
                Text(stringResource(R.string.isla_montaje_prueba), style = MaterialTheme.typography.labelLarge)
                prueba.variables.forEach { variable -> contenidoVariable(variable.nombre, prueba, false) }
            }
        }

        Button(
            onClick = {
                val resultado = MotorPruebaJusta.evaluar(control, prueba)
                if (resultado.esJusta) onEjecutar() else onPruebaInjusta()
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(stringResource(R.string.isla_boton_correr))
        }
    }
}
