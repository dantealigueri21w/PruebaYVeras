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

/** El reto difícil corre tres montajes (varias magnitudes de la misma variable) para
 * armar una tendencia real — no una comparación de dos, como fácil y medio. */
private const val CORRIDAS_REQUERIDAS_DIFICIL = 3

data class EstadoIsla(
    val idIsla: String = "",
    val retos: List<RetoEntity> = emptyList(),
    val indiceRetoActual: Int = 0,
    val prueba: Montaje = Montaje(emptyList()),
    val ultimoAvisoInjusto: Boolean = false,
    val ultimoAvisoMagnitudRepetida: Boolean = false,
    val mostrarPreguntaTendencia: Boolean = false,
    val piezaConfirmada: Boolean = false,
    val ultimoResultado: ResultadoReto? = null,
    val corridasRetoActual: Int = 0,
) {
    val retoActual: RetoEntity? get() = retos.getOrNull(indiceRetoActual)
    val corridasNecesarias: Int get() = if (retoActual?.dificultad == "DIFICIL") CORRIDAS_REQUERIDAS_DIFICIL else 1
}

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
        val magnitudPrueba = (actual.prueba.valorDe(reto.variableIndependiente) as? Number)?.toFloat()

        viewModelScope.launch {
            // En el reto difícil, cada una de las tres corridas tiene que probar una
            // magnitud distinta de la misma variable — repetir la misma cantidad tres
            // veces no arma una tendencia real, solo repite el mismo punto.
            if (reto.dificultad == "DIFICIL" && magnitudPrueba != null) {
                val yaProbadas = repository.magnitudesProbadas(reto.idReto)
                if (yaProbadas.any { it == magnitudPrueba }) {
                    _estado.value = actual.copy(ultimoAvisoMagnitudRepetida = true)
                    return@launch
                }
            }

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
                ultimoResultado = ResultadoReto(resultadoControl, resultadoPrueba),
                corridasRetoActual = actual.corridasRetoActual + 1,
                ultimoAvisoMagnitudRepetida = false,
            )
        }
    }

    /** Cierra el resultado que se acaba de ver. Si el reto actual todavía no llegó a
     * [EstadoIsla.corridasNecesarias] (el reto difícil pide tres, el resto una), se
     * queda en el mismo reto para la siguiente corrida; si ya las completó, recién ahí
     * avanza al siguiente reto (o abre la pregunta de tendencia si era el último) —
     * nunca automáticamente, para que el resultado no desaparezca antes de leerlo. */
    fun continuarTrasResultado() {
        val actual = _estado.value
        if (actual.corridasRetoActual < actual.corridasNecesarias) {
            _estado.value = actual.copy(prueba = Montaje(adaptador.variablesBase), ultimoResultado = null)
            return
        }

        val esUltimoReto = actual.indiceRetoActual == actual.retos.lastIndex
        _estado.value = actual.copy(
            indiceRetoActual = if (esUltimoReto) actual.indiceRetoActual else actual.indiceRetoActual + 1,
            mostrarPreguntaTendencia = esUltimoReto,
            prueba = Montaje(adaptador.variablesBase),
            ultimoResultado = null,
            corridasRetoActual = 0,
        )
    }

    fun elegirTendencia(tendencia: Tendencia) {
        val actual = _estado.value
        val reto = actual.retos.getOrNull(actual.indiceRetoActual) ?: return
        viewModelScope.launch {
            // Los datos reales del propio reto difícil (ordenados por la magnitud que
            // se probó, no por el orden en que se jugó) — no los de fácil/medio, que en
            // varias islas prueban una variable distinta y mezclarlos con esos daría
            // una tendencia sin sentido.
            val datos = repository.datosOrdenadosPorMagnitud(reto.idReto)
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
