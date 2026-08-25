package pe.appmobile.pruebayveras.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Un objeto tocable que representa un booleano: aparece o desaparece un elemento del
 * mundo (el paracaídas puesto o no, la luz encendida o no). Nunca un `Switch` de Material.
 */
@Composable
fun InterruptorDiegetico(
    activado: Boolean,
    onCambia: (Boolean) -> Unit,
    iconoActivado: Int,
    iconoDesactivado: Int,
    descripcionActivado: String,
    descripcionDesactivado: String,
    modifier: Modifier = Modifier,
) {
    val escala by animateFloatAsState(if (activado) 1.1f else 1f, label = "escalaInterruptor")
    val recurso = if (activado) iconoActivado else iconoDesactivado
    val descripcion = if (activado) descripcionActivado else descripcionDesactivado

    Image(
        painter = painterResource(recurso),
        contentDescription = descripcion,
        modifier = modifier
            .size(120.dp)
            .scale(escala)
            .semantics { role = Role.Switch }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onCambia(!activado) },
    )
}
