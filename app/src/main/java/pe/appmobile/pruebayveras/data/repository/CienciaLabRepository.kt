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

    suspend fun confirmarPieza(idPieza: String) {
        val pieza = db.piezaChirimboloDao().observarTodas().first().first { it.idPieza == idPieza }
        db.piezaChirimboloDao().actualizar(pieza.copy(confirmada = true))
    }
}
