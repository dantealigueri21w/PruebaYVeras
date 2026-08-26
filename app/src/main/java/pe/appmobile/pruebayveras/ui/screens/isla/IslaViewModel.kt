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
import pe.appmobile.pruebayveras.domain.engine.MotorCuadernoDatos
import pe.appmobile.pruebayveras.domain.engine.Tendencia
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.Variable

data class EstadoIsla(
    val idIsla: String = "",
    val retos: List<RetoEntity> = emptyList(),
    val indiceRetoActual: Int = 0,
    val prueba: Montaje = Montaje(emptyList()),
    val ultimoAvisoInjusto: Boolean = false,
    val resultadosPorReto: Map<String, Float> = emptyMap(),
    val mostrarPreguntaTendencia: Boolean = false,
    val piezaConfirmada: Boolean = false,
    val ultimoResultado: ResultadoReto? = null,
)

/** Lo que arrojó el último "Correr la prueba", para mostrarlo antes de pasar al
 * siguiente reto — sin esto la pantalla avanzaba en silencio y no parecía haber
 * pasado nada. */
data class ResultadoReto(val resultadoControl: Float, val resultadoPrueba: Float)

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
        _estado.value = _estado.value.copy(prueba = Montaje(nuevasVariables), ultimoAvisoInjusto = false)
    }

    fun avisarPruebaInjusta() {
        _estado.value = _estado.value.copy(ultimoAvisoInjusto = true)
    }

    fun ejecutarPrueba() {
        val actual = _estado.value
        val reto = actual.retos.getOrNull(actual.indiceRetoActual) ?: return
        val control = Montaje(adaptador.variablesBase)
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
                fueJusta = true,
            )

            _estado.value = actual.copy(
                resultadosPorReto = actual.resultadosPorReto + (reto.idReto to resultadoPrueba),
                ultimoResultado = ResultadoReto(resultadoControl, resultadoPrueba),
            )
        }
    }

    /** Avanza al siguiente reto (o abre la pregunta de tendencia si era el último) — se
     * llama cuando el niño ya vio el resultado de [ejecutarPrueba] y toca "Continuar",
     * nunca automáticamente, para que el resultado no desaparezca antes de leerlo. */
    fun continuarTrasResultado() {
        val actual = _estado.value
        val esUltimoReto = actual.indiceRetoActual == actual.retos.lastIndex
        _estado.value = actual.copy(
            indiceRetoActual = if (esUltimoReto) actual.indiceRetoActual else actual.indiceRetoActual + 1,
            mostrarPreguntaTendencia = esUltimoReto,
            prueba = Montaje(adaptador.variablesBase),
            ultimoResultado = null,
        )
    }

    fun elegirTendencia(tendencia: Tendencia) {
        val actual = _estado.value
        val datos = actual.resultadosPorReto.values.toList()
        val reto = actual.retos.getOrNull(actual.indiceRetoActual) ?: return
        viewModelScope.launch {
            val correcta = MotorCuadernoDatos.conclusionEsCorrecta(datos, tendencia)
            repository.registrarPaginaCuaderno(reto.idReto, tendencia, correcta)
            if (correcta) {
                val idPieza = SemillaPiezas.piezas.first { it.idIsla == idIsla }.idPieza
                repository.confirmarPieza(idPieza)
                _estado.value = _estado.value.copy(piezaConfirmada = true)
            }
        }
    }
}
