package pe.appmobile.pruebayveras.data.seed

import pe.appmobile.pruebayveras.data.entity.RetoEntity

object SemillaRetos {
    val retos = listOf(
        // Isla de la Marea
        RetoEntity("reto_marea_facil", "isla_marea", "FACIL",
            "Chirimbolo dice: \"este huevo no se hunde igual en todos los charcos. ¿Será la sal?\"", "sal"),
        RetoEntity("reto_marea_medio", "isla_marea", "MEDIO",
            "Una isleña pregunta si el volumen de agua también cambia si el huevo flota.", "sal"),
        RetoEntity("reto_marea_dificil", "isla_marea", "DIFICIL",
            "Prueba con 1, 3 y 6 cucharadas de sal y arma la tendencia completa de cuánto flota.", "sal"),

        // Isla del Viento
        RetoEntity("reto_viento_facil", "isla_viento", "FACIL",
            "Chirimbolo quiere saber si su aleta lo ayuda a caer más despacio, como un paracaídas.", "paracaidas"),
        RetoEntity("reto_viento_medio", "isla_viento", "MEDIO",
            "Un isleño duda si la altura también cambia cuánto tarda en llegar al suelo.", "paracaidas"),
        RetoEntity("reto_viento_dificil", "isla_viento", "DIFICIL",
            "Prueba desde tres alturas distintas y arma la tendencia del tiempo de caída.", "altura"),

        // Isla del Jardín
        RetoEntity("reto_jardin_facil", "isla_jardin", "FACIL",
            "Chirimbolo tiene una hojita que le creció sola. ¿Necesita luz para crecer más?", "luz"),
        RetoEntity("reto_jardin_medio", "isla_jardin", "MEDIO",
            "Una isleña quiere saber si sin agua la hojita crece igual, con o sin luz.", "agua"),
        RetoEntity("reto_jardin_dificil", "isla_jardin", "DIFICIL",
            "Prueba el crecimiento a los 2, 5 y 8 días y arma la tendencia completa.", "dias"),

        // Isla de la Cueva
        RetoEntity("reto_cueva_facil", "isla_cueva", "FACIL",
            "Chirimbolo no está seguro de si su tímpano de lata escucha bien de lejos.", "distancia"),
        RetoEntity("reto_cueva_medio", "isla_cueva", "MEDIO",
            "Un isleño pregunta si una tela puesta en la pared cambia el eco.", "material"),
        RetoEntity("reto_cueva_dificil", "isla_cueva", "DIFICIL",
            "Prueba a tres distancias distintas y arma la tendencia de la intensidad del eco.", "distancia"),

        // Isla del Faro
        RetoEntity("reto_faro_facil", "isla_faro", "FACIL",
            "Chirimbolo frotó su antena contra su propio brazo y ahora atrae cosas. ¿Frotar más ayuda?", "frotadas"),
        RetoEntity("reto_faro_medio", "isla_faro", "MEDIO",
            "Una isleña quiere saber si la distancia a los papelitos también importa.", "frotadas"),
        RetoEntity("reto_faro_dificil", "isla_faro", "DIFICIL",
            "Prueba con 3, 9 y 15 frotadas y arma la tendencia de cuántos papelitos atrae.", "frotadas"),

        // Isla de las Olas
        RetoEntity("reto_olas_facil", "isla_olas", "FACIL",
            "Chirimbolo tiene una junta oxidada. ¿El agua tibia la afloja más rápido que la fría?", "temperatura"),
        RetoEntity("reto_olas_medio", "isla_olas", "MEDIO",
            "Un isleño pregunta si la cantidad de azúcar también cambia el tiempo.", "temperatura"),
        RetoEntity("reto_olas_dificil", "isla_olas", "DIFICIL",
            "Prueba con agua a 5, 20 y 40 grados y arma la tendencia del tiempo de disolución.", "temperatura"),

        // Isla del Risco
        RetoEntity("reto_risco_facil", "isla_risco", "FACIL",
            "Chirimbolo quiere saber si sus patas agarran mejor una rampa lisa o una rugosa.", "superficie"),
        RetoEntity("reto_risco_medio", "isla_risco", "MEDIO",
            "Una isleña pregunta si la altura de la rampa también cambia la distancia.", "superficie"),
        RetoEntity("reto_risco_dificil", "isla_risco", "DIFICIL",
            "Prueba la misma superficie en tres alturas y arma la tendencia de la distancia recorrida.", "altura"),

        // Isla del Imán
        RetoEntity("reto_iman_facil", "isla_iman", "FACIL",
            "Chirimbolo no sabe si su imán interno sigue funcionando bajo un poco de arena.", "grosor"),
        RetoEntity("reto_iman_medio", "isla_iman", "MEDIO",
            "Un isleño duda si el tipo de material entre el imán y el clip también importa.", "material"),
        RetoEntity("reto_iman_dificil", "isla_iman", "DIFICIL",
            "Prueba con tres grosores distintos y arma la tendencia de si el clip se mueve.", "grosor"),

        // Isla del Reflejo
        RetoEntity("reto_reflejo_facil", "isla_reflejo", "FACIL",
            "Chirimbolo se preocupa de que su placa oscura se calienta más que la clara.", "color"),
        RetoEntity("reto_reflejo_medio", "isla_reflejo", "MEDIO",
            "Una isleña pregunta si el tiempo al sol también cambia cuánto se calienta.", "color"),
        RetoEntity("reto_reflejo_dificil", "isla_reflejo", "DIFICIL",
            "Prueba a los 5, 10 y 20 minutos al sol y arma la tendencia de la temperatura.", "minutos"),
    )
}
