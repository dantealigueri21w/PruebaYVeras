package pe.appmobile.pruebayveras.domain.adapter

import pe.appmobile.pruebayveras.domain.engine.MotorAbsorcionLuz
import pe.appmobile.pruebayveras.domain.engine.MotorCaida
import pe.appmobile.pruebayveras.domain.engine.MotorDisolucion
import pe.appmobile.pruebayveras.domain.engine.MotorEco
import pe.appmobile.pruebayveras.domain.engine.MotorEstatica
import pe.appmobile.pruebayveras.domain.engine.MotorFlotabilidad
import pe.appmobile.pruebayveras.domain.engine.MotorFriccion
import pe.appmobile.pruebayveras.domain.engine.MotorGerminacion
import pe.appmobile.pruebayveras.domain.engine.MotorMagnetismo
import pe.appmobile.pruebayveras.domain.model.Montaje
import pe.appmobile.pruebayveras.domain.model.TipoObstaculo
import pe.appmobile.pruebayveras.domain.model.TipoSuperficie
import pe.appmobile.pruebayveras.domain.model.Variable

object AdaptadorMarea : AdaptadorIsla {
    override val variablesBase = listOf(Variable("sal", 0), Variable("volumenAgua", 250))
    override fun calcular(montaje: Montaje) =
        MotorFlotabilidad.calcular(
            cucharadasDeSal = montaje.valorDe("sal") as Int,
            volumenAguaMl = montaje.valorDe("volumenAgua") as Int,
        ).alturaFlotacion
}

object AdaptadorViento : AdaptadorIsla {
    override val variablesBase = listOf(Variable("paracaidas", false), Variable("altura", 20f))
    override fun calcular(montaje: Montaje) = MotorCaida.calcular(
        alturaMetros = montaje.valorDe("altura") as Float,
        tieneParacaidas = montaje.valorDe("paracaidas") as Boolean,
    )
}

object AdaptadorJardin : AdaptadorIsla {
    override val variablesBase = listOf(Variable("luz", true), Variable("agua", true), Variable("dias", 5))
    override fun calcular(montaje: Montaje) = MotorGerminacion.calcular(
        tieneAgua = montaje.valorDe("agua") as Boolean,
        tieneLuz = montaje.valorDe("luz") as Boolean,
        dias = montaje.valorDe("dias") as Int,
    )
}

object AdaptadorCueva : AdaptadorIsla {
    override val variablesBase = listOf(Variable("distancia", 10f), Variable("material", false))
    override fun calcular(montaje: Montaje) = MotorEco.intensidad(
        distanciaMetros = montaje.valorDe("distancia") as Float,
        materialAbsorbente = montaje.valorDe("material") as Boolean,
    )
}

object AdaptadorFaro : AdaptadorIsla {
    override val variablesBase = listOf(Variable("frotadas", 0), Variable("distancia", 5))
    override fun calcular(montaje: Montaje) = MotorEstatica.papelitosAtraidos(
        frotadas = montaje.valorDe("frotadas") as Int,
        distanciaCm = montaje.valorDe("distancia") as Int,
    ).toFloat()
}

object AdaptadorOlas : AdaptadorIsla {
    override val variablesBase = listOf(Variable("temperatura", 20), Variable("azucar", 10))
    override fun calcular(montaje: Montaje) = MotorDisolucion.tiempoSegundos(
        temperaturaC = montaje.valorDe("temperatura") as Int,
        cantidadAzucarG = montaje.valorDe("azucar") as Int,
    ).toFloat()
}

object AdaptadorRisco : AdaptadorIsla {
    override val variablesBase = listOf(Variable("superficie", TipoSuperficie.LISA), Variable("altura", 30f))
    override fun calcular(montaje: Montaje) = MotorFriccion.distanciaCm(
        alturaCm = montaje.valorDe("altura") as Float,
        superficie = montaje.valorDe("superficie") as TipoSuperficie,
    )
}

object AdaptadorIman : AdaptadorIsla {
    override val variablesBase = listOf(Variable("grosor", 2), Variable("material", TipoObstaculo.CARTON))
    override fun calcular(montaje: Montaje): Float {
        val resultado = MotorMagnetismo.calcular(
            grosorMm = montaje.valorDe("grosor") as Int,
            obstaculo = montaje.valorDe("material") as TipoObstaculo,
        )
        return if (resultado.clipSeMueve) 1f else 0f
    }
}

object AdaptadorReflejo : AdaptadorIsla {
    override val variablesBase = listOf(Variable("color", true), Variable("minutos", 10))
    override fun calcular(montaje: Montaje) = MotorAbsorcionLuz.temperaturaGanada(
        esOscuro = montaje.valorDe("color") as Boolean,
        minutosAlSol = montaje.valorDe("minutos") as Int,
    )
}

fun adaptadorDe(idIsla: String): AdaptadorIsla = when (idIsla) {
    "isla_marea" -> AdaptadorMarea
    "isla_viento" -> AdaptadorViento
    "isla_jardin" -> AdaptadorJardin
    "isla_cueva" -> AdaptadorCueva
    "isla_faro" -> AdaptadorFaro
    "isla_olas" -> AdaptadorOlas
    "isla_risco" -> AdaptadorRisco
    "isla_iman" -> AdaptadorIman
    "isla_reflejo" -> AdaptadorReflejo
    else -> error("Isla sin adaptador: $idIsla")
}
