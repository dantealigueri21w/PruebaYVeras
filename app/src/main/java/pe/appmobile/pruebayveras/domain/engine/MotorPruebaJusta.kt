package pe.appmobile.pruebayveras.domain.engine

import pe.appmobile.pruebayveras.domain.model.Montaje

object MotorPruebaJusta {

    data class Resultado(val esJusta: Boolean, val variablesDistintas: List<String>)

    fun evaluar(control: Montaje, prueba: Montaje): Resultado {
        val nombresControl = control.variables.map { it.nombre }
        val nombresPrueba = prueba.variables.map { it.nombre }
        require(nombresControl.toSet() == nombresPrueba.toSet()) {
            "Los dos montajes deben declarar las mismas variables para poder compararse"
        }

        val distintas = control.variables
            .filter { variableControl ->
                val valorPrueba = prueba.valorDe(variableControl.nombre)
                variableControl.valor != valorPrueba
            }
            .map { it.nombre }

        return Resultado(esJusta = distintas.size == 1, variablesDistintas = distintas)
    }
}
