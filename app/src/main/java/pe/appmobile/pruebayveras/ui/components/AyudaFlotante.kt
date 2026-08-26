package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import pe.appmobile.pruebayveras.data.entity.RetoEntity

/** Un botón de ayuda siempre visible en pantalla de isla — no repite el tutorial
 * completo, da una pista concreta del reto actual sin decir la respuesta (ficha,
 * "Ayuda, siempre a mano"). */
@Composable
fun AyudaFlotante(reto: RetoEntity?, modifier: Modifier = Modifier) {
    var abierta by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        IconButton(
            onClick = { abierta = true },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface, CircleShape),
        ) {
            Icon(Icons.Filled.QuestionMark, contentDescription = stringResource(R.string.cd_ayuda))
        }
        if (abierta && reto != null) {
            Column(
                modifier = Modifier.padding(top = 8.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)).padding(16.dp),
            ) {
                Text(stringResource(R.string.isla_ayuda_titulo), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.isla_ayuda_texto, reto.variableIndependiente))
                Button(onClick = { abierta = false }, modifier = Modifier.padding(top = 8.dp)) {
                    Text(stringResource(R.string.isla_resultado_continuar))
                }
            }
        }
    }
}
