package pe.appmobile.pruebayveras.domain.model

data class Variable(val nombre: String, val valor: Any)

data class Montaje(val variables: List<Variable>) {
    fun valorDe(nombre: String): Any =
        variables.first { it.nombre == nombre }.valor
}
