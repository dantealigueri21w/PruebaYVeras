package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

data class OpcionBinaria<T>(val valor: T, val icono: Int, val descripcion: String)

/** Dos objetos del mundo, uno al lado del otro; tocar uno lo selecciona. */
@Composable
fun <T> SelectorBinario(
    seleccionado: T,
    opciones: Pair<OpcionBinaria<T>, OpcionBinaria<T>>,
    onSeleccion: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        listOf(opciones.first, opciones.second).forEach { opcion ->
            val estaElegida = opcion.valor == seleccionado
            Image(
                painter = painterResource(opcion.icono),
                contentDescription = opcion.descripcion,
                modifier = Modifier
                    .padding(8.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        if (estaElegida) MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface,
                    )
                    .semantics { contentDescription = opcion.descripcion }
                    .clickable { onSeleccion(opcion.valor) },
            )
        }
    }
}
