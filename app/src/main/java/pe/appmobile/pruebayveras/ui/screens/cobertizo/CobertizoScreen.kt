package pe.appmobile.pruebayveras.ui.screens.cobertizo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.ui.theme.piezaDeChirimbolo

/** Cuadrícula armada a mano con `chunked`, nunca `LazyVerticalGrid` — son 9 piezas,
 * tamaño acotado (sección 7.1, punto 6). */
@Composable
fun CobertizoScreen(viewModel: CobertizoViewModel, onVolver: () -> Unit = {}) {
    val piezas by viewModel.piezas.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_volver))
        }
        Text(stringResource(R.string.cobertizo_titulo))
        piezas.chunked(3).forEach { fila ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fila.forEach { pieza ->
                    Column {
                        Image(
                            painter = painterResource(piezaDeChirimbolo(pieza.idPieza, pieza.confirmada)),
                            contentDescription = pieza.nombre,
                            modifier = Modifier.size(96.dp),
                        )
                        Text(
                            text = if (pieza.confirmada) stringResource(R.string.cobertizo_pieza_confirmada)
                            else stringResource(R.string.cobertizo_pieza_dudosa),
                        )
                    }
                }
            }
        }
    }
}
