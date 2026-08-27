package pe.appmobile.pruebayveras.ui.screens.cuaderno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity
import pe.appmobile.pruebayveras.data.seed.SemillaIslas
import pe.appmobile.pruebayveras.data.seed.SemillaRetos
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
                PaginaDeCuaderno(pagina = paginas[indice])
            }
        }

        IconButton(onClick = onVolver, modifier = Modifier.padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_volver))
        }
    }
}

@Composable
private fun PaginaDeCuaderno(pagina: PaginaCuadernoEntity) {
    val reto = remember(pagina.idReto) { SemillaRetos.retos.firstOrNull { it.idReto == pagina.idReto } }
    val nombreIsla = remember(reto) {
        SemillaIslas.islas.firstOrNull { it.idIsla == reto?.idIsla }?.nombre ?: pagina.idReto
    }
    val etiquetaDificultad = when (reto?.dificultad) {
        "FACIL" -> stringResource(R.string.cuaderno_dificultad_facil)
        "MEDIO" -> stringResource(R.string.cuaderno_dificultad_medio)
        "DIFICIL" -> stringResource(R.string.cuaderno_dificultad_dificil)
        else -> null
    }
    val etiquetaTendencia = when (pagina.tendenciaElegida) {
        "SUBE" -> stringResource(R.string.isla_tendencia_sube)
        "BAJA" -> stringResource(R.string.isla_tendencia_baja)
        else -> stringResource(R.string.isla_tendencia_no_cambia)
    }

    Box(
        modifier = Modifier
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.surface, EtiquetaFrascoShape)
            .padding(20.dp),
    ) {
        Column(modifier = Modifier.width(260.dp)) {
            Text(
                text = nombreIsla,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (etiquetaDificultad != null) {
                Text(
                    text = etiquetaDificultad,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = stringResource(R.string.cuaderno_pagina_tu_respuesta, etiquetaTendencia),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = if (pagina.tendenciaCorrecta) {
                    stringResource(R.string.cuaderno_pagina_logrado)
                } else {
                    stringResource(R.string.cuaderno_pagina_no_logrado)
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
