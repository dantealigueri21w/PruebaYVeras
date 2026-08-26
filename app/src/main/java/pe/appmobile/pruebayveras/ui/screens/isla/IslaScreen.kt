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
import java.util.Locale
import pe.appmobile.pruebayveras.R
import pe.appmobile.pruebayveras.domain.adapter.adaptadorDe
import pe.appmobile.pruebayveras.domain.engine.Tendencia
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.TipoObstaculo
import pe.appmobile.pruebayveras.domain.model.TipoSuperficie
import pe.appmobile.pruebayveras.ui.components.AyudaFlotante
import pe.appmobile.pruebayveras.ui.components.GloboDialogoChirimbolo
import pe.appmobile.pruebayveras.ui.components.InterruptorDiegetico
import pe.appmobile.pruebayveras.ui.components.MesaDeTanteo
import pe.appmobile.pruebayveras.ui.components.MetaDireccional
import pe.appmobile.pruebayveras.ui.components.OpcionBinaria
import pe.appmobile.pruebayveras.ui.components.PanelLegible
import pe.appmobile.pruebayveras.ui.components.PerillaGiratoria
import pe.appmobile.pruebayveras.ui.components.SelectorBinario
import pe.appmobile.pruebayveras.ui.components.TarjetaSabiasQue
import pe.appmobile.pruebayveras.ui.theme.chirimboloPose
import pe.appmobile.pruebayveras.ui.theme.fondoDeIsla

@Composable
fun IslaScreen(viewModel: IslaViewModel, onVolver: () -> Unit) {
    val estado by viewModel.estado.collectAsState()
    if (estado.retos.isEmpty()) return

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(fondoDeIsla(estado.idIsla)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        IconButton(onClick = onVolver, modifier = Modifier.padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_volver))
        }
        // Sin reto en curso de verdad (isla ya completada, o esperando la respuesta de
        // tendencia del último reto ya resuelto) no hay pista que dar — el retoActual
        // que quedaría ahí es el del desafío ya cerrado, no uno activo.
        val retoParaAyuda = if (estado.piezaConfirmada || estado.mostrarPreguntaTendencia) null else estado.retoActual
        AyudaFlotante(reto = retoParaAyuda, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp))

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
                    Text(stringResource(R.string.isla_completada_titulo), style = MaterialTheme.typography.titleLarge)
                    Button(onClick = onVolver, modifier = Modifier.padding(top = 12.dp)) {
                        Text(stringResource(R.string.isla_completada_volver))
                    }
                }
            } else if (estado.mostrarPreguntaTendencia) {
                PanelLegible {
                    Text(stringResource(R.string.isla_pregunta_tendencia), style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                        Button(onClick = { viewModel.elegirTendencia(Tendencia.SUBE) }) { Text(stringResource(R.string.isla_tendencia_sube)) }
                        Button(onClick = { viewModel.elegirTendencia(Tendencia.BAJA) }) { Text(stringResource(R.string.isla_tendencia_baja)) }
                        Button(onClick = { viewModel.elegirTendencia(Tendencia.NO_CAMBIA) }) { Text(stringResource(R.string.isla_tendencia_no_cambia)) }
                    }
                }
            } else {
                val reto = estado.retoActual ?: return@Column
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(chirimboloPose("explica")),
                        contentDescription = stringResource(R.string.cd_chirimbolo),
                        modifier = Modifier.size(80.dp),
                    )
                    GloboDialogoChirimbolo(texto = reto.textoCorazonada)
                }

                val resultado = estado.ultimoResultado
                if (resultado == null) {
                    MetaDireccional(direccionEsperada = reto.direccionEsperada)
                    MesaDeTanteo(
                        control = Montaje(adaptadorDe(estado.idIsla).variablesBase),
                        prueba = estado.prueba,
                        onEjecutar = viewModel::ejecutarPrueba,
                        contenidoVariable = { nombre, montaje, esControl ->
                            val valor = montaje.valorDe(nombre)
                            if (esControl) {
                                PanelLegible { Text("$nombre: $valor") }
                            } else {
                                ControlDeVariable(nombre = nombre, valor = valor, onCambia = { viewModel.cambiarVariablePrueba(nombre, it) })
                            }
                        },
                    )
                } else {
                    PanelLegible {
                        Text(
                            if (resultado.fueJusta) stringResource(R.string.isla_prueba_justa_titulo) else stringResource(R.string.isla_prueba_injusta_titulo),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        if (!resultado.fueJusta) {
                            Text(stringResource(R.string.isla_prueba_injusta_explicacion, resultado.variablesDistintas.joinToString(", ")))
                        }
                        Text(
                            stringResource(
                                R.string.isla_resultado_control_prueba,
                                formatearResultado(resultado.resultadoControl),
                                formatearResultado(resultado.resultadoPrueba),
                            ),
                        )
                        Button(onClick = viewModel::continuarTrasResultado, modifier = Modifier.padding(top = 12.dp)) {
                            Text(if (resultado.fueJusta) stringResource(R.string.isla_resultado_continuar) else stringResource(R.string.isla_reintentar))
                        }
                    }
                }
            }
        }

        // Se dibuja al final del Box (no antes de la Column) para quedar realmente
        // encima de todo lo demás — Compose apila los hijos de un Box en el orden en
        // que se declaran, y el overlay con scrim de TarjetaSabiasQue necesita ser el
        // último para no quedar tapado por el contenido del reto.
        estado.tarjetaSabiasQue?.let { dato ->
            TarjetaSabiasQue(dato = dato, onCerrar = viewModel::cerrarTarjetaSabiasQue)
        }
    }
}

@Composable
private fun ControlDeVariable(nombre: String, valor: Any, onCambia: (Any) -> Unit) {
    when (valor) {
        is Int -> PerillaGiratoria(valor = valor, rango = 0..20, etiqueta = nombre, onValorCambia = onCambia)
        is Float -> PerillaGiratoria(
            valor = valor.toInt(), rango = rangoDeVariableContinua(nombre), etiqueta = nombre,
            onValorCambia = { onCambia(it.toFloat()) },
        )
        is Boolean -> InterruptorDiegetico(
            activado = valor, onCambia = onCambia,
            iconoActivado = R.drawable.chirimbolo_celebra, iconoDesactivado = R.drawable.chirimbolo_confundido,
            descripcionActivado = "$nombre activado", descripcionDesactivado = "$nombre desactivado",
        )
        is TipoSuperficie -> SelectorBinario(
            seleccionado = valor,
            opciones = OpcionBinaria(TipoSuperficie.LISA, R.drawable.objeto_carrito, stringResource(R.string.isla_superficie_lisa)) to
                OpcionBinaria(TipoSuperficie.RUGOSA, R.drawable.objeto_carrito, stringResource(R.string.isla_superficie_rugosa)),
            onSeleccion = onCambia,
        )
        is TipoObstaculo -> SelectorBinario(
            seleccionado = valor,
            opciones = OpcionBinaria(TipoObstaculo.CARTON, R.drawable.objeto_iman, stringResource(R.string.isla_obstaculo_carton)) to
                OpcionBinaria(TipoObstaculo.METAL_GRUESO, R.drawable.objeto_iman, stringResource(R.string.isla_obstaculo_metal_grueso)),
            onSeleccion = onCambia,
        )
        else -> Unit
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

/** Un motor de fenómeno puede devolver algo como 12.499998 — se redondea a un
 * decimal para que el panel de resultado se lea pulido, no como un valor crudo. */
private fun formatearResultado(valor: Float): String = String.format(Locale.getDefault(), "%.1f", valor)
