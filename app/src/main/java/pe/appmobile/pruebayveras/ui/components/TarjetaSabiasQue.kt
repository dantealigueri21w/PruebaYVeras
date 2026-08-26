package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

/** El premio de cada uno de los 27 retos: un dato científico real que se queda,
 * coleccionable — no solo 9 al final (ficha, "¿Por qué vuelve mañana?"). */
@Composable
fun TarjetaSabiasQue(dato: String, onCerrar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(24.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(R.string.isla_sabias_que_titulo), style = MaterialTheme.typography.titleLarge)
            Text(dato, modifier = Modifier.padding(top = 12.dp, bottom = 16.dp))
            Button(onClick = onCerrar) { Text(stringResource(R.string.isla_resultado_continuar)) }
        }
    }
}
