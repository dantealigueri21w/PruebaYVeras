package pe.appmobile.pruebayveras.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.ui.shapes.GloboDialogoShape

/** El diálogo de Chirimbolo, con forma propia de globo — nunca un AlertDialog genérico. */
@Composable
fun GloboDialogoChirimbolo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, GloboDialogoShape)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    )
}
