package pe.appmobile.pruebayveras.data.repository

import kotlinx.coroutines.flow.first
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.data.entity.IntentoEntity
import pe.appmobile.pruebayveras.data.entity.PaginaCuadernoEntity
import pe.appmobile.pruebayveras.data.seed.SemillaInsignias
import pe.appmobile.pruebayveras.data.seed.SemillaIslas
import pe.appmobile.pruebayveras.data.seed.SemillaPiezas
import pe.appmobile.pruebayveras.data.seed.SemillaRetos

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
        valorProbado: String,
        resultadoReal: Float,
        logrado: Boolean,
    ) {
        db.intentoDao().guardar(
            IntentoEntity(
                idReto = idReto,
                variableCambiada = variableCambiada,
                valorProbado = valorProbado,
                resultadoReal = resultadoReal,
                logrado = logrado,
                timestamp = System.currentTimeMillis(),
            )
        )
    }

    suspend fun confirmarPieza(idPieza: String) {
        val pieza = db.piezaChirimboloDao().observarTodas().first().first { it.idPieza == idPieza }
        db.piezaChirimboloDao().actualizar(pieza.copy(confirmada = true))
    }

    /** Una página real por reto logrado — nunca de ejemplo. */
    suspend fun registrarPaginaLogro(idReto: String, resultadoReal: Float) {
        db.paginaCuadernoDao().guardar(
            PaginaCuadernoEntity(
                idReto = idReto,
                resultadoReal = resultadoReal,
                timestamp = System.currentTimeMillis(),
            )
        )
    }
}
