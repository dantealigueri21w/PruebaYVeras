package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.ui.shapes.EtiquetaFrascoShape

/** `HorizontalPager` en vez de una lista con scroll (sección 3.1): el Cuaderno se hojea. */
@Composable
fun CuadernoScreen(viewModel: CuadernoViewModel, onVolver: () -> Unit = {}) {
    val paginas by viewModel.paginas.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (paginas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.cuaderno_vacio))
            }
        } else {
            val estadoPager = rememberPagerState(pageCount = { paginas.size })
            HorizontalPager(state = estadoPager, modifier = Modifier.fillMaxSize()) { indice ->
                val pagina = paginas[indice]
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .background(MaterialTheme.colorScheme.surface, EtiquetaFrascoShape)
                        .padding(20.dp),
                ) {
                    Text(text = "${pagina.idReto} — ${pagina.tendenciaElegida} (${if (pagina.tendenciaCorrecta) "✓" else "✗"})")
                }
            }
        }

        IconButton(onClick = onVolver, modifier = Modifier.padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_volver))
        }
    }
}
