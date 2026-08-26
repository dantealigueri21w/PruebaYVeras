package pe.appmobile.pruebayveras.ui.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.ui.theme.avatarDrawable

@Composable
fun PerfilScreen(viewModel: PerfilViewModel, onVolver: () -> Unit = {}) {
    var alias by remember { mutableStateOf("") }
    var avatarElegido by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_volver))
        }
        Text(stringResource(R.string.perfil_titulo))
        OutlinedTextField(
            value = alias,
            onValueChange = {
                alias = it
                viewModel.guardar(alias, avatarElegido)
            },
            label = { Text(stringResource(R.string.perfil_alias_hint)) },
        )
        Text(stringResource(R.string.perfil_elige_avatar))
        (0 until 12).chunked(4).forEach { fila ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fila.forEach { numero ->
                    Image(
                        painter = painterResource(avatarDrawable(numero)),
                        contentDescription = "Avatar $numero",
                        modifier = Modifier
                            .size(64.dp)
                            .border(
                                width = if (avatarElegido == numero) 3.dp else 0.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape,
                            )
                            .clickable {
                                avatarElegido = numero
                                viewModel.guardar(alias, avatarElegido)
                            },
                    )
                }
            }
        }
    }
}
