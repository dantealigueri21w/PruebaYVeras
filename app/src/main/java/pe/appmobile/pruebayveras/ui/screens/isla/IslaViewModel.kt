package pe.appmobile.pruebayveras.ui.screens.isla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.RetoEntity
import pe.appmobile.pruebayveras.data.repository.CienciaLabRepository
import pe.appmobile.pruebayveras.data.seed.SemillaPiezas
import pe.appmobile.pruebayveras.domain.adapter.AdaptadorIsla
import pe.appmobile.pruebayveras.domain.adapter.adaptadorDe
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable
import kotlin.math.abs

data class EstadoIsla(
    val idIsla: String = "",
    val retos: List<RetoEntity> = emptyList(),
    val indiceRetoActual: Int = 0,
    val prueba: Montaje = Montaje(emptyList()),
    val ultimoResultado: ResultadoLogro? = null,
    val piezaConfirmada: Boolean = false,
) {
    val retoActual: RetoEntity? get() = retos.getOrNull(indiceRetoActual)
}

/** Lo que arrojó el último "¡Pruébalo!": el valor real que devolvió el motor de esta
 * isla y si cayó dentro de la zona objetivo del reto — sin esto, tocar el botón
 * avanzaba en silencio y no se sentía un logro. */
data class ResultadoLogro(val resultadoReal: Float, val logrado: Boolean)

class IslaViewModel(private val db: AppDatabase, private val idIsla: String) : ViewModel() {

    private val repository = CienciaLabRepository(db)
    private val adaptador: AdaptadorIsla = adaptadorDe(idIsla)

    private val _estado = MutableStateFlow(EstadoIsla(idIsla = idIsla, prueba = Montaje(adaptador.variablesBase)))
    val estado: StateFlow<EstadoIsla> = _estado

    init {
        viewModelScope.launch {
            repository.sembrarSiEsPrimeraVez()
            val retos = db.retoDao().observarPorIsla(idIsla).first().sortedBy {
                when (it.dificultad) {
                    "FACIL" -> 0
                    "MEDIO" -> 1
                    else -> 2
                }
            }
            _estado.value = _estado.value.copy(retos = retos)
        }
    }

    fun cambiarVariablePrueba(nombre: String, valor: Any) {
        val nuevasVariables = _estado.value.prueba.variables.map {
            if (it.nombre == nombre) Variable(nombre, valor) else it
        }
        _estado.value = _estado.value.copy(prueba = Montaje(nuevasVariables))
    }

    /** Calcula el resultado real con el motor de esta isla y lo compara contra la
     * zona objetivo del reto activo — sin mesa de control, sin validar si es "justo":
     * se toca, se prueba, se ve si se logró. */
    fun probar() {
        val actual = _estado.value
        val reto = actual.retoActual ?: return
        val resultadoReal = adaptador.calcular(actual.prueba)
        val logrado = abs(resultadoReal - reto.valorObjetivo) <= reto.margenObjetivo

        viewModelScope.launch {
            repository.registrarIntento(
                idReto = reto.idReto,
                variableCambiada = reto.variableIndependiente,
                valorProbado = actual.prueba.valorDe(reto.variableIndependiente).toString(),
                resultadoReal = resultadoReal,
                logrado = logrado,
            )
            _estado.value = actual.copy(ultimoResultado = ResultadoLogro(resultadoReal, logrado))
        }
    }

    /** Cierra el resultado que se acaba de ver. Si se logró la meta, guarda la página
     * real en el Cuaderno y avanza al siguiente reto (o confirma la pieza de
     * Chirimbolo si era el último); si no se logró, se queda en el mismo reto para
     * reintentarlo — nunca automático, para que el resultado no desaparezca antes de
     * leerlo. */
    fun continuarTrasResultado() {
        val actual = _estado.value
        val resultado = actual.ultimoResultado

        if (resultado == null || !resultado.logrado) {
            _estado.value = actual.copy(prueba = Montaje(adaptador.variablesBase), ultimoResultado = null)
            return
        }

        val reto = actual.retoActual ?: return
        val esUltimoReto = actual.indiceRetoActual == actual.retos.lastIndex

        _estado.value = actual.copy(
            indiceRetoActual = if (esUltimoReto) actual.indiceRetoActual else actual.indiceRetoActual + 1,
            prueba = Montaje(adaptador.variablesBase),
            ultimoResultado = null,
            piezaConfirmada = esUltimoReto || actual.piezaConfirmada,
        )

        viewModelScope.launch {
            repository.registrarPaginaLogro(reto.idReto, resultado.resultadoReal)
            if (esUltimoReto) {
                val idPieza = SemillaPiezas.piezas.first { it.idIsla == idIsla }.idPieza
                repository.confirmarPieza(idPieza)
            }
        }
    }
}
