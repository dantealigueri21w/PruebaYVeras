package pe.appmobile.pruebayveras.ui.screens.archipielago

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.ui.theme.iconoDeIsla

/**
 * Posición relativa (0f a 1f) de cada isla sobre `mapa_archipielago` — medida a ojo
 * sobre la imagen ya generada. Se ajusta si el mapa cambia; ver riesgo técnico
 * documentado en el plan de la Parte 2.
 */
private val posicionesIslas = mapOf(
    "isla_marea" to (0.30f to 0.62f),
    "isla_viento" to (0.65f to 0.55f),
    "isla_jardin" to (0.18f to 0.78f),
    "isla_cueva" to (0.55f to 0.75f),
    "isla_faro" to (0.75f to 0.70f),
    "isla_olas" to (0.40f to 0.85f),
    "isla_risco" to (0.60f to 0.40f),
    "isla_iman" to (0.25f to 0.45f),
    "isla_reflejo" to (0.80f to 0.40f),
)

@Composable
fun ArchipielagoScreen(
    viewModel: ArchipielagoViewModel,
    onAbrirIsla: (String) -> Unit,
    onAbrirCuaderno: () -> Unit = {},
    onAbrirCobertizo: () -> Unit = {},
    onAbrirPerfil: () -> Unit = {},
    onAbrirAjustes: () -> Unit = {},
) {
    val islas by viewModel.islas.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val anchoDp = maxWidth
        val altoDp = maxHeight

        Image(
            painter = painterResource(R.drawable.mapa_archipielago),
            contentDescription = stringResource(R.string.archipielago_descripcion_mapa),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        islas.forEach { isla ->
            val posicion = posicionesIslas[isla.idIsla] ?: return@forEach
            Box(
                modifier = Modifier
                    .offset(x = anchoDp * posicion.first, y = altoDp * posicion.second)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .semantics { contentDescription = isla.nombre }
                    .clickable { onAbrirIsla(isla.idIsla) },
            ) {
                Image(
                    painter = painterResource(iconoDeIsla(isla.idIsla)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
        ) {
            IconButton(onClick = onAbrirCuaderno) {
                Icon(Icons.Filled.Book, contentDescription = stringResource(R.string.cd_abrir_cuaderno))
            }
            IconButton(onClick = onAbrirCobertizo) {
                Icon(Icons.Filled.Handyman, contentDescription = stringResource(R.string.cd_abrir_cobertizo))
            }
            IconButton(onClick = onAbrirPerfil) {
                Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.cd_abrir_perfil))
            }
            IconButton(onClick = onAbrirAjustes) {
                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.cd_abrir_ajustes))
            }
        }
    }
}
