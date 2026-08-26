package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Un fondo propio (opaco, como el globo de Chirimbolo) detrás de un texto que, si no,
 * caería directo sobre la imagen de la isla — ahí un color de letra "correcto" no
 * alcanza, porque el fondo real es una ilustración con zonas claras y oscuras a la vez.
 */
@Composable
fun PanelLegible(modifier: Modifier = Modifier, contenido: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        contenido()
    }
}
