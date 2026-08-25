package pe.appmobile.pruebayveras.ui.shapes

import androidx.compose.foundation.shape.GenericShape

/** Una nota de papel con una esquina doblada — para la corazonada de un isleño. */
val NotaRotaShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    val doblez = h * 0.18f
    moveTo(0f, 0f)
    lineTo(w - doblez, 0f)
    lineTo(w, doblez)
    lineTo(w, h)
    lineTo(0f, h)
    close()
}

/** Un globo de diálogo con una cola apuntando hacia abajo-izquierda, para Chirimbolo. */
val GloboDialogoShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    val radio = h * 0.16f
    val colaAncho = w * 0.12f
    val colaAlto = h * 0.18f

    moveTo(radio, 0f)
    lineTo(w - radio, 0f)
    quadraticTo(w, 0f, w, radio)
    lineTo(w, h - colaAlto - radio)
    quadraticTo(w, h - colaAlto, w - radio, h - colaAlto)
    lineTo(colaAncho * 2.2f, h - colaAlto)
    lineTo(colaAncho, h)
    lineTo(colaAncho * 1.6f, h - colaAlto)
    lineTo(radio, h - colaAlto)
    quadraticTo(0f, h - colaAlto, 0f, h - colaAlto - radio)
    lineTo(0f, radio)
    quadraticTo(0f, 0f, radio, 0f)
    close()
}

/** La etiqueta de un frasco de vidrio — un rectángulo con las dos esquinas superiores en ángulo. */
val EtiquetaFrascoShape = GenericShape { size, _ ->
    val w = size.width
    val h = size.height
    val corte = h * 0.22f
    moveTo(0f, corte)
    lineTo(w * 0.15f, 0f)
    lineTo(w * 0.85f, 0f)
    lineTo(w, corte)
    lineTo(w, h)
    lineTo(0f, h)
    close()
}
