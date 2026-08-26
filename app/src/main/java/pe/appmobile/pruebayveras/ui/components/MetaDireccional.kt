package pe.appmobile.pruebayveras.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.appmobile.pruebayveras.R

/** La meta que se ve ANTES de tocar nada: direccional ("¿sube o baja?"), nunca un
 * número exacto que cazar (ficha, "Mecánica principal"). */
@Composable
fun MetaDireccional(direccionEsperada: String) {
    val texto = when (direccionEsperada) {
        "SUBE" -> stringResource(R.string.isla_meta_sube)
        "BAJA" -> stringResource(R.string.isla_meta_baja)
        else -> stringResource(R.string.isla_meta_no_cambia)
    }
    PanelLegible { Text(texto, style = MaterialTheme.typography.bodyLarge) }
}
