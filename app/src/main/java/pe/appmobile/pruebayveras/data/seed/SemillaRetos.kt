package pe.appmobile.pruebayveras.data.seed

import pe.appmobile.pruebayveras.data.entity.RetoEntity

/**
 * `valorObjetivo` y `margenObjetivo` de cada reto se calcularon a mano contra la
 * fórmula real del motor de su isla (nunca a ojo — ver `docs/superpowers/specs/
 * 2026-08-26-mecanica-logro-tocar-y-ver.md`, sección 4.1, para el detalle isla por
 * isla). El margen es más ancho en fácil y más angosto en difícil, pero la escala
 * absoluta la fija cada motor (0-17 en Marea, 0-6 en Faro, etc.), así que no sale de
 * una fórmula única — se verificó reto por reto que el objetivo sea alcanzable dentro
 * del rango real de la variable.
 */
object SemillaRetos {
    val retos = listOf(
        // Isla de la Marea — MotorFlotabilidad: alturaFlotacion = max(0, sal - 3)
        RetoEntity("reto_marea_facil", "isla_marea", "FACIL",
            "Chirimbolo dice: \"este huevo no se hunde igual en todos los charcos. Pongo dos " +
                "charcos con la misma agua: a uno le echo sal, al otro no. ¿Será la sal la que " +
                "cambia si flota?\"", "sal", valorObjetivo = 5f, margenObjetivo = 1.75f),
        RetoEntity("reto_marea_medio", "isla_marea", "MEDIO",
            "Una isleña pregunta si el volumen de agua también cambia si el huevo flota.", "sal",
            valorObjetivo = 9f, margenObjetivo = 1.8f),
        RetoEntity("reto_marea_dificil", "isla_marea", "DIFICIL",
            "Chirimbolo quiere que el huevo llegue justo a la marca del vaso — ni que se hunda, " +
                "ni que flote de más.", "sal", valorObjetivo = 15f, margenObjetivo = 1.5f),

        // Isla del Viento — MotorCaida: con paracaidas x3.5 mas lento; altura por sqrt
        RetoEntity("reto_viento_facil", "isla_viento", "FACIL",
            "Chirimbolo quiere saber si su aleta lo ayuda a caer más despacio. Suelta dos " +
                "Chirimbolos iguales desde la misma altura: uno con la aleta puesta, el otro sin " +
                "ella. ¿Cuál llega primero?", "paracaidas", valorObjetivo = 7.07f, margenObjetivo = 2.5f),
        RetoEntity("reto_viento_medio", "isla_viento", "MEDIO",
            "Un isleño duda si la altura también cambia cuánto tarda en llegar al suelo.", "paracaidas",
            valorObjetivo = 7.07f, margenObjetivo = 1.4f),
        RetoEntity("reto_viento_dificil", "isla_viento", "DIFICIL",
            "Ajusta la altura para que Chirimbolo aterrice suave, justo dentro del círculo marcado.",
            "altura", valorObjetivo = 2.79f, margenObjetivo = 0.3f),

        // Isla del Jardín — MotorGerminacion: crecimiento = (1.2 con luz / 0.4 sin luz) * dias
        RetoEntity("reto_jardin_facil", "isla_jardin", "FACIL",
            "Chirimbolo tiene una hojita que le creció sola. ¿Necesita luz para crecer más?", "luz",
            valorObjetivo = 6f, margenObjetivo = 2.1f),
        RetoEntity("reto_jardin_medio", "isla_jardin", "MEDIO",
            "Una isleña quiere saber si sin agua la hojita crece igual, con o sin luz.", "agua",
            valorObjetivo = 6f, margenObjetivo = 1.2f),
        RetoEntity("reto_jardin_dificil", "isla_jardin", "DIFICIL",
            "Cuenta los días justos para que la hojita llegue exactamente a la marca de altura.",
            "dias", valorObjetivo = 18f, margenObjetivo = 1.8f),

        // Isla de la Cueva — MotorEco: intensidad = max(0, 100 - distancia*8) * (0.4 con material)
        RetoEntity("reto_cueva_facil", "isla_cueva", "FACIL",
            "Chirimbolo no está seguro de si su tímpano de lata escucha bien de lejos.", "distancia",
            valorObjetivo = 40f, margenObjetivo = 14f),
        RetoEntity("reto_cueva_medio", "isla_cueva", "MEDIO",
            "Un isleño pregunta si una tela puesta en la pared cambia el eco.", "material",
            valorObjetivo = 8f, margenObjetivo = 1.6f),
        RetoEntity("reto_cueva_dificil", "isla_cueva", "DIFICIL",
            "Ubica la fuente de sonido a la distancia justa para que el eco haga sonar la " +
                "campanita marcada.", "distancia", valorObjetivo = 64f, margenObjetivo = 6.4f),

        // Isla del Faro — MotorEstatica: papelitos = min(10, frotadas / 3)
        RetoEntity("reto_faro_facil", "isla_faro", "FACIL",
            "Chirimbolo frotó su antena contra su propio brazo y ahora atrae cosas. ¿Frotar más ayuda?",
            "frotadas", valorObjetivo = 3f, margenObjetivo = 1.05f),
        RetoEntity("reto_faro_medio", "isla_faro", "MEDIO",
            "Una isleña quiere saber si la distancia a los papelitos también importa.", "frotadas",
            valorObjetivo = 4f, margenObjetivo = 0.8f),
        RetoEntity("reto_faro_dificil", "isla_faro", "DIFICIL",
            "Frota el globo las veces justas para atraer exactamente los papelitos marcados.",
            "frotadas", valorObjetivo = 6f, margenObjetivo = 0.6f),

        // Isla de las Olas — MotorDisolucion: tiempo = max(5, 60 - temperatura)
        RetoEntity("reto_olas_facil", "isla_olas", "FACIL",
            "Chirimbolo tiene una junta oxidada. ¿El agua tibia la afloja más rápido que la fría?",
            "temperatura", valorObjetivo = 50f, margenObjetivo = 6f),
        RetoEntity("reto_olas_medio", "isla_olas", "MEDIO",
            "Un isleño pregunta si la cantidad de azúcar también cambia el tiempo.", "temperatura",
            valorObjetivo = 48f, margenObjetivo = 4f),
        RetoEntity("reto_olas_dificil", "isla_olas", "DIFICIL",
            "Ajusta la temperatura del agua para que el azúcar se disuelva justo antes de que se " +
                "acabe el tiempo.", "temperatura", valorObjetivo = 45f, margenObjetivo = 2f),

        // Isla del Risco — MotorFriccion: distancia = altura*4 / (1 + coeficiente*10)
        RetoEntity("reto_risco_facil", "isla_risco", "FACIL",
            "Chirimbolo quiere saber si sus patas agarran mejor una rampa lisa o una rugosa.",
            "superficie", valorObjetivo = 60f, margenObjetivo = 12f),
        RetoEntity("reto_risco_medio", "isla_risco", "MEDIO",
            "Una isleña pregunta si la altura de la rampa también cambia la distancia.", "superficie",
            valorObjetivo = 17.1f, margenObjetivo = 3.4f),
        RetoEntity("reto_risco_dificil", "isla_risco", "DIFICIL",
            "Ajusta la altura para que el carrito se detenga justo en la zona marcada del suelo.",
            "altura", valorObjetivo = 90f, margenObjetivo = 9f),

        // Isla del Imán — MotorMagnetismo: se mueve (1) si grosor <= 5 mm, si no, no (0)
        RetoEntity("reto_iman_facil", "isla_iman", "FACIL",
            "Chirimbolo no sabe si su imán interno sigue funcionando bajo un poco de arena.", "grosor",
            valorObjetivo = 1f, margenObjetivo = 0.6f),
        RetoEntity("reto_iman_medio", "isla_iman", "MEDIO",
            "Un isleño duda si el tipo de material entre el imán y el clip también importa.", "material",
            valorObjetivo = 1f, margenObjetivo = 0.6f),
        RetoEntity("reto_iman_dificil", "isla_iman", "DIFICIL",
            "Encuentra el grosor justo para que el clip todavía se mueva con el imán.", "grosor",
            valorObjetivo = 1f, margenObjetivo = 0.3f),

        // Isla del Reflejo — MotorAbsorcionLuz: temperatura = minutos * (0.8 oscuro / 0.3 claro)
        RetoEntity("reto_reflejo_facil", "isla_reflejo", "FACIL",
            "Chirimbolo se preocupa de que su placa oscura se calienta más que la clara.", "color",
            valorObjetivo = 8f, margenObjetivo = 3f),
        RetoEntity("reto_reflejo_medio", "isla_reflejo", "MEDIO",
            "Una isleña pregunta si el tiempo al sol también cambia cuánto se calienta.", "color",
            valorObjetivo = 3f, margenObjetivo = 1.5f),
        RetoEntity("reto_reflejo_dificil", "isla_reflejo", "DIFICIL",
            "Ajusta los minutos al sol para que la tela llegue justo a la temperatura marcada.",
            "minutos", valorObjetivo = 13f, margenObjetivo = 1.5f),
    )
}
