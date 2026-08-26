package pe.appmobile.pruebayveras.ui.screens.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R

/** Pantalla secundaria: aquí sí es correcto usar Switch/Text de Material puro
 * (sección 3.1 lo permite explícitamente para ajustes). */
@Composable
fun AjustesScreen(onVolver: () -> Unit = {}) {
    var sonidoActivado by remember { mutableStateOf(true) }
    var vibracionActivada by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_volver))
        }
        Text(stringResource(R.string.ajustes_titulo))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.ajustes_sonido))
            Switch(checked = sonidoActivado, onCheckedChange = { sonidoActivado = it })
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.ajustes_vibracion))
            Switch(checked = vibracionActivada, onCheckedChange = { vibracionActivada = it })
        }
    }
}
