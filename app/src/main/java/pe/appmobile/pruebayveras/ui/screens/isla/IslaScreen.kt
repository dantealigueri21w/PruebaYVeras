package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import pe.appmobile.pruebayveras.domain.adapter.adaptadorDe
import pe.appmobile.pruebayveras.domain.engine.Tendencia
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.TipoObstaculo
import pe.appmobile.pruebayveras.domain.model.TipoSuperficie
import pe.appmobile.pruebayveras.ui.components.GloboDialogoChirimbolo
import pe.appmobile.pruebayveras.ui.components.InterruptorDiegetico
import pe.appmobile.pruebayveras.ui.components.MesaDoblePrueba
import pe.appmobile.pruebayveras.ui.components.OpcionBinaria
import pe.appmobile.pruebayveras.ui.components.PerillaGiratoria
import pe.appmobile.pruebayveras.ui.components.SelectorBinario
import pe.appmobile.pruebayveras.ui.theme.fondoDeIsla

@Composable
fun IslaScreen(viewModel: IslaViewModel, onVolver: () -> Unit) {
    val estado by viewModel.estado.collectAsState()
    if (estado.retos.isEmpty()) return

    val reto = estado.retos.getOrNull(estado.indiceRetoActual)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(fondoDeIsla(estado.idIsla)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (reto != null) {
                GloboDialogoChirimbolo(texto = reto.textoCorazonada)

                MesaDoblePrueba(
                    control = Montaje(adaptadorDe(estado.idIsla).variablesBase),
                    prueba = estado.prueba,
                    onEjecutar = viewModel::ejecutarPrueba,
                    onPruebaInjusta = viewModel::avisarPruebaInjusta,
                    contenidoVariable = { nombre, montaje, esControl ->
                        val valor = montaje.valorDe(nombre)
                        if (!esControl) {
                            when (valor) {
                                is Int -> PerillaGiratoria(
                                    valor = valor, rango = 0..20, etiqueta = nombre,
                                    onValorCambia = { viewModel.cambiarVariablePrueba(nombre, it) },
                                )
                                is Float -> PerillaGiratoria(
                                    valor = valor.toInt(), rango = 0..60, etiqueta = nombre,
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
                                    opciones = OpcionBinaria(TipoSuperficie.LISA, R.drawable.objeto_carrito, "Superficie lisa") to
                                        OpcionBinaria(TipoSuperficie.RUGOSA, R.drawable.objeto_carrito, "Superficie rugosa"),
                                    onSeleccion = { viewModel.cambiarVariablePrueba(nombre, it) },
                                )
                                is TipoObstaculo -> SelectorBinario(
                                    seleccionado = valor,
                                    opciones = OpcionBinaria(TipoObstaculo.CARTON, R.drawable.objeto_iman, "Cartón") to
                                        OpcionBinaria(TipoObstaculo.METAL_GRUESO, R.drawable.objeto_iman, "Metal grueso"),
                                    onSeleccion = { viewModel.cambiarVariablePrueba(nombre, it) },
                                )
                                else -> Unit
                            }
                        } else {
                            Text(text = "$nombre: $valor")
                        }
                    },
                )

                if (estado.ultimoAvisoInjusto) {
                    Text(stringResource(R.string.isla_alerta_no_es_justa))
                }
            }

            if (estado.mostrarPreguntaTendencia) {
                Text(stringResource(R.string.isla_pregunta_tendencia))
                Row {
                    Button(onClick = { viewModel.elegirTendencia(Tendencia.SUBE) }) {
                        Text(stringResource(R.string.isla_tendencia_sube))
                    }
                    Button(onClick = { viewModel.elegirTendencia(Tendencia.BAJA) }) {
                        Text(stringResource(R.string.isla_tendencia_baja))
                    }
                    Button(onClick = { viewModel.elegirTendencia(Tendencia.NO_CAMBIA) }) {
                        Text(stringResource(R.string.isla_tendencia_no_cambia))
                    }
                }
            }
        }
    }
}
