package pe.appmobile.pruebayveras.data.repository

import kotlinx.coroutines.flow.first
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.IntentoEntity
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity
import pe.appmobile.pruebayveras.data.seed.SemillaInsignias
import pe.appmobile.pruebayveras.data.seed.SemillaIslas
import pe.appmobile.pruebayveras.data.seed.SemillaPiezas
import pe.appmobile.pruebayveras.data.seed.SemillaRetos
import pe.appmobile.pruebayveras.domain.engine.MotorCuadernoDatos
import pe.appmobile.pruebayveras.domain.engine.Tendencia

class CienciaLabRepository(private val db: AppDatabase) {

    suspend fun sembrarSiEsPrimeraVez() {
        if (db.islaDao().observarTodas().first().isNotEmpty()) return

        db.islaDao().insertarTodas(SemillaIslas.islas)
        db.retoDao().insertarTodos(SemillaRetos.retos)
        db.piezaChirimboloDao().insertarTodas(SemillaPiezas.piezas)
        db.insigniaDao().insertarTodas(SemillaInsignias.insignias)
    }

    suspend fun registrarIntento(
        idReto: String,
        variableCambiada: String,
        valorControl: String,
        valorPrueba: String,
        resultadoControl: Float,
        resultadoPrueba: Float,
        fueJusta: Boolean,
    ) {
        db.intentoDao().guardar(
            IntentoEntity(
                idReto = idReto,
                variableCambiada = variableCambiada,
                valorControl = valorControl,
                valorPrueba = valorPrueba,
                resultadoControl = resultadoControl,
                resultadoPrueba = resultadoPrueba,
                fueJusta = fueJusta,
                timestamp = System.currentTimeMillis(),
            )
        )
    }

    suspend fun tendenciaRealDe(idReto: String): Tendencia {
        val intentos = db.intentoDao().observarPorReto(idReto).first()
        val datos = intentos.filter { it.fueJusta }.map { it.resultadoPrueba }
        return MotorCuadernoDatos.tendenciaReal(datos)
    }

    /** Las magnitudes reales ya probadas para un reto — `valorPrueba` es siempre el
     * valor de `variableIndependiente` en esa corrida (así se guarda en
     * [registrarIntento]), así que no hace falta guardarlo aparte. Sirve para no dejar
     * repetir la misma magnitud dos veces en el reto difícil: una "tendencia" armada
     * con el mismo dato tres veces no dice nada. */
    suspend fun magnitudesProbadas(idReto: String): List<Float> =
        db.intentoDao().observarPorReto(idReto).first()
            .filter { it.fueJusta }
            .mapNotNull { it.valorPrueba.toFloatOrNull() }

    /** Los resultados reales de un reto, ordenados por la magnitud que se probó (no por
     * el orden en que se jugaron) — para que la tendencia sea correcta sin importar en
     * qué orden el niño haya elegido las tres cantidades. */
    suspend fun datosOrdenadosPorMagnitud(idReto: String): List<Float> =
        db.intentoDao().observarPorReto(idReto).first()
            .filter { it.fueJusta }
            .mapNotNull { intento -> intento.valorPrueba.toFloatOrNull()?.let { it to intento.resultadoPrueba } }
            .sortedBy { it.first }
            .map { it.second }

    suspend fun confirmarPieza(idPieza: String) {
        val pieza = db.piezaChirimboloDao().observarTodas().first().first { it.idPieza == idPieza }
        db.piezaChirimboloDao().actualizar(pieza.copy(confirmada = true))
    }

    suspend fun registrarPaginaCuaderno(idReto: String, tendenciaElegida: Tendencia, tendenciaCorrecta: Boolean) {
        db.paginaCuadernoDao().guardar(
            PaginaCuadernoEntity(
                idReto = idReto,
                tendenciaElegida = tendenciaElegida.name,
                tendenciaCorrecta = tendenciaCorrecta,
                timestamp = System.currentTimeMillis(),
            )
        )
    }
}
