package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.domain.model.TipoObstaculo
import pe.appmobile.pruebayveras.domain.model.TipoSuperficie
import pe.appmobile.pruebayveras.ui.components.GloboDialogoChirimbolo
import pe.appmobile.pruebayveras.ui.components.InterruptorDiegetico
import pe.appmobile.pruebayveras.ui.components.OpcionBinaria
import pe.appmobile.pruebayveras.ui.components.PanelLegible
import pe.appmobile.pruebayveras.ui.components.PanelResultado
import pe.appmobile.pruebayveras.ui.components.PerillaGiratoria
import pe.appmobile.pruebayveras.ui.components.SelectorBinario
import pe.appmobile.pruebayveras.ui.theme.chirimboloPose
import pe.appmobile.pruebayveras.ui.theme.fondoDeIsla

@Composable
fun IslaScreen(viewModel: IslaViewModel, onVolver: () -> Unit) {
    val estado by viewModel.estado.collectAsState()
    if (estado.retos.isEmpty()) return

    val reto = estado.retoActual

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(fondoDeIsla(estado.idIsla)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        IconButton(onClick = onVolver, modifier = Modifier.padding(8.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_volver),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 56.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (estado.piezaConfirmada) {
                PanelLegible {
                    Text(
                        stringResource(R.string.isla_completada_titulo),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Button(onClick = onVolver, modifier = Modifier.padding(top = 12.dp)) {
                        Text(stringResource(R.string.isla_completada_volver))
                    }
                }
            } else if (reto != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(chirimboloPose("explica")),
                        contentDescription = stringResource(R.string.cd_chirimbolo),
                        modifier = Modifier.size(80.dp),
                    )
                    GloboDialogoChirimbolo(texto = reto.textoCorazonada)
                }

                val resultado = estado.ultimoResultado
                if (resultado != null) {
                    PanelResultado(
                        resultadoReal = resultado.resultadoReal,
                        valorObjetivo = reto.valorObjetivo,
                        margenObjetivo = reto.margenObjetivo,
                        logrado = resultado.logrado,
                        onContinuar = viewModel::continuarTrasResultado,
                    )
                } else {
                    val valor = estado.prueba.valorDe(reto.variableIndependiente)
                    val nombre = reto.variableIndependiente

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (valor) {
                            is Int -> PerillaGiratoria(
                                valor = valor, rango = 0..20, etiqueta = nombre,
                                onValorCambia = { viewModel.cambiarVariablePrueba(nombre, it) },
                            )
                            is Float -> PerillaGiratoria(
                                valor = valor.toInt(), rango = rangoDeVariableContinua(nombre), etiqueta = nombre,
                                onValorCambia = { viewModel.cambiarVariablePrueba(nombre, it.toFloat()) },
                            )
                            is Boolean -> InterruptorDiegetico(
                                activado = valor,
                                onCambia = { viewModel.cambiarVariablePrueba(nombre, it) },
                                iconoActivado = R.drawable.chirimbolo_celebra,
                                iconoDesactivado = R.drawable.chirimbolo_confundido,
                                descripcionActivado = "$nombre activado",
                                descripcionDesactivado = "$nombre desactivado",
                            )
                            is TipoSuperficie -> SelectorBinario(
                                seleccionado = valor,
                                opciones = OpcionBinaria(TipoSuperficie.LISA, R.drawable.objeto_carrito, stringResource(R.string.isla_superficie_lisa)) to
                                    OpcionBinaria(TipoSuperficie.RUGOSA, R.drawable.objeto_carrito, stringResource(R.string.isla_superficie_rugosa)),
                                onSeleccion = { viewModel.cambiarVariablePrueba(nombre, it) },
                            )
                            is TipoObstaculo -> SelectorBinario(
                                seleccionado = valor,
                                opciones = OpcionBinaria(TipoObstaculo.CARTON, R.drawable.objeto_iman, stringResource(R.string.isla_obstaculo_carton)) to
                                    OpcionBinaria(TipoObstaculo.METAL_GRUESO, R.drawable.objeto_iman, stringResource(R.string.isla_obstaculo_metal_grueso)),
                                onSeleccion = { viewModel.cambiarVariablePrueba(nombre, it) },
                            )
                            else -> Unit
                        }

                        Button(onClick = viewModel::probar, modifier = Modifier.padding(top = 16.dp)) {
                            Text(stringResource(R.string.isla_boton_probar))
                        }
                    }
                }
            }
        }
    }
}

/**
 * "distancia" (Isla de la Cueva) se aplana en 0 a partir de los 12.5 m reales
 * (`MotorEco`) — con el rango genérico de 0..60 casi todo el arrastre no cambiaba
 * nada. Las demás variables continuas (altura) escalan suave en todo 0..60, sin zona
 * muerta, así que no necesitan un rango propio.
 */
private fun rangoDeVariableContinua(nombre: String): IntRange = when (nombre) {
    "distancia" -> 0..15
    else -> 0..60
}
