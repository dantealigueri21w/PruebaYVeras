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
import pe.appmobile.pruebayveras.domain.engine.MotorPruebaJusta
import pe.appmobile.pruebayveras.domain.engine.Tendencia
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable

data class EstadoIsla(
    val idIsla: String = "",
    val retos: List<RetoEntity> = emptyList(),
    val indiceRetoActual: Int = 0,
    val prueba: Montaje = Montaje(emptyList()),
    val ultimoResultado: ResultadoPrueba? = null,
    val mostrarPreguntaTendencia: Boolean = false,
    val piezaConfirmada: Boolean = false,
    val tarjetaSabiasQue: String? = null,
) {
    val retoActual: RetoEntity? get() = retos.getOrNull(indiceRetoActual)
    val esPrimerTutorial: Boolean get() = idIsla == "isla_marea" && indiceRetoActual == 0 && ultimoResultado == null
}

/** Qué arrojó "Correr la prueba": el resultado real de control y de prueba, y si fue
 * una comparación justa (una sola variable distinta) — [fueJusta] decide si
 * IslaScreen muestra la explicación causal o la invitación a repetir cambiando
 * solo una cosa. */
data class ResultadoPrueba(
    val resultadoControl: Float,
    val resultadoPrueba: Float,
    val fueJusta: Boolean,
    val variablesDistintas: List<String>,
)

class IslaViewModel(private val db: AppDatabase, private val idIsla: String) : ViewModel() {

    private val repository = CienciaLabRepository(db)
    private val adaptador: AdaptadorIsla = adaptadorDe(idIsla)

    private val _estado = MutableStateFlow(EstadoIsla(idIsla = idIsla, prueba = Montaje(adaptador.variablesBase)))
    val estado: StateFlow<EstadoIsla> = _estado

    init {
        viewModelScope.launch {
            repository.sembrarSiEsPrimeraVez()
            val retos = db.retoDao().observarPorIsla(idIsla).first().sortedBy {
                when (it.dificultad) { "FACIL" -> 0; "MEDIO" -> 1; else -> 2 }
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

    /** Corre siempre. `MotorPruebaJusta` no bloquea nada: solo decide qué pantalla de
     * resultado sigue (explicación causal si fue justa, invitación a repetir si no). */
    fun ejecutarPrueba() {
        val actual = _estado.value
        val reto = actual.retoActual ?: return
        val control = Montaje(adaptador.variablesBase)
        val evaluacion = MotorPruebaJusta.evaluar(control, actual.prueba)
        val resultadoControl = adaptador.calcular(control)
        val resultadoPrueba = adaptador.calcular(actual.prueba)

        viewModelScope.launch {
            repository.registrarIntento(
                idReto = reto.idReto,
                variableCambiada = reto.variableIndependiente,
                valorControl = control.valorDe(reto.variableIndependiente).toString(),
                valorPrueba = actual.prueba.valorDe(reto.variableIndependiente).toString(),
                resultadoControl = resultadoControl,
                resultadoPrueba = resultadoPrueba,
                fueJusta = evaluacion.esJusta,
            )
            _estado.value = actual.copy(
                ultimoResultado = ResultadoPrueba(resultadoControl, resultadoPrueba, evaluacion.esJusta, evaluacion.variablesDistintas),
            )
        }
    }

    /** Cierra el resultado. Si fue justa: guarda la tarjeta "¿sabías que?", y si era el
     * último reto de la isla, pasa a la pregunta de tendencia; si no lo era, avanza al
     * siguiente. Si no fue justa: se queda en el mismo reto, montaje reiniciado, para
     * que el niño repita cambiando solo una cosa. */
    fun continuarTrasResultado() {
        val actual = _estado.value
        val resultado = actual.ultimoResultado ?: return
        val reto = actual.retoActual ?: return

        if (!resultado.fueJusta) {
            _estado.value = actual.copy(prueba = Montaje(adaptador.variablesBase), ultimoResultado = null)
            return
        }

        val esUltimoReto = actual.indiceRetoActual == actual.retos.lastIndex
        _estado.value = actual.copy(
            indiceRetoActual = if (esUltimoReto) actual.indiceRetoActual else actual.indiceRetoActual + 1,
            prueba = Montaje(adaptador.variablesBase),
            ultimoResultado = null,
            mostrarPreguntaTendencia = esUltimoReto,
            tarjetaSabiasQue = reto.datoCientifico,
        )
    }

    fun cerrarTarjetaSabiasQue() {
        _estado.value = _estado.value.copy(tarjetaSabiasQue = null)
    }

    fun elegirTendencia(tendencia: Tendencia) {
        val actual = _estado.value
        val reto = actual.retos.getOrNull(actual.indiceRetoActual) ?: return
        viewModelScope.launch {
            val tendenciaReal = repository.tendenciaRealDe(reto.idReto)
            val correcta = tendenciaReal == tendencia
            repository.registrarPaginaCuaderno(reto.idReto, tendencia, correcta)
            if (correcta) {
                val idPieza = SemillaPiezas.piezas.first { it.idIsla == idIsla }.idPieza
                repository.confirmarPieza(idPieza)
                _estado.value = _estado.value.copy(piezaConfirmada = true)
            }
        }
    }
}
