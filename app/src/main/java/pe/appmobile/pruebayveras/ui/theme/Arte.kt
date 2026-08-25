package pe.appmobile.pruebayveras.ui.theme

import androidx.annotation.DrawableRes
import pe.appmobile.pruebayveras.R

@DrawableRes
fun fondoDeIsla(idIsla: String): Int = when (idIsla) {
    "isla_marea" -> R.drawable.fondo_isla_marea
    "isla_viento" -> R.drawable.fondo_isla_viento
    "isla_jardin" -> R.drawable.fondo_isla_jardin
    "isla_cueva" -> R.drawable.fondo_isla_cueva
    "isla_faro" -> R.drawable.fondo_isla_faro
    "isla_olas" -> R.drawable.fondo_isla_olas
    "isla_risco" -> R.drawable.fondo_isla_risco
    "isla_iman" -> R.drawable.fondo_isla_iman
    "isla_reflejo" -> R.drawable.fondo_isla_reflejo
    else -> error("Isla sin fondo: $idIsla")
}

@DrawableRes
fun iconoDeIsla(idIsla: String): Int = when (idIsla) {
    "isla_marea" -> R.drawable.icono_isla_marea
    "isla_viento" -> R.drawable.icono_isla_viento
    "isla_jardin" -> R.drawable.icono_isla_jardin
    "isla_cueva" -> R.drawable.icono_isla_cueva
    "isla_faro" -> R.drawable.icono_isla_faro
    "isla_olas" -> R.drawable.icono_isla_olas
    "isla_risco" -> R.drawable.icono_isla_risco
    "isla_iman" -> R.drawable.icono_isla_iman
    "isla_reflejo" -> R.drawable.icono_isla_reflejo
    else -> error("Isla sin icono: $idIsla")
}

@DrawableRes
fun piezaDeChirimbolo(idPieza: String, confirmada: Boolean): Int {
    val sufijo = if (confirmada) "confirmada" else "dudosa"
    return when ("${idPieza}_$sufijo") {
        "pieza_tanque_flotador_dudosa" -> R.drawable.pieza_tanque_flotador_dudosa
        "pieza_tanque_flotador_confirmada" -> R.drawable.pieza_tanque_flotador_confirmada
        "pieza_aleta_trasera_dudosa" -> R.drawable.pieza_aleta_trasera_dudosa
        "pieza_aleta_trasera_confirmada" -> R.drawable.pieza_aleta_trasera_confirmada
        "pieza_hojita_dudosa" -> R.drawable.pieza_hojita_dudosa
        "pieza_hojita_confirmada" -> R.drawable.pieza_hojita_confirmada
        "pieza_timpano_lata_dudosa" -> R.drawable.pieza_timpano_lata_dudosa
        "pieza_timpano_lata_confirmada" -> R.drawable.pieza_timpano_lata_confirmada
        "pieza_antena_dudosa" -> R.drawable.pieza_antena_dudosa
        "pieza_antena_confirmada" -> R.drawable.pieza_antena_confirmada
        "pieza_junta_oxidada_dudosa" -> R.drawable.pieza_junta_oxidada_dudosa
        "pieza_junta_oxidada_confirmada" -> R.drawable.pieza_junta_oxidada_confirmada
        "pieza_patas_dudosa" -> R.drawable.pieza_patas_dudosa
        "pieza_patas_confirmada" -> R.drawable.pieza_patas_confirmada
        "pieza_iman_interno_dudosa" -> R.drawable.pieza_iman_interno_dudosa
        "pieza_iman_interno_confirmada" -> R.drawable.pieza_iman_interno_confirmada
        "pieza_placa_pecho_dudosa" -> R.drawable.pieza_placa_pecho_dudosa
        "pieza_placa_pecho_confirmada" -> R.drawable.pieza_placa_pecho_confirmada
        else -> error("Pieza sin arte: $idPieza ($sufijo)")
    }
}

@DrawableRes
fun insigniaDrawable(idInsignia: String): Int = when (idInsignia) {
    "insignia_primera_prueba" -> R.drawable.insignia_primera_prueba
    "insignia_ojo_de_lupa" -> R.drawable.insignia_ojo_de_lupa
    "insignia_pieza_confirmada" -> R.drawable.insignia_pieza_confirmada
    "insignia_chirimbolo_completo" -> R.drawable.insignia_chirimbolo_completo
    "insignia_buen_ojo_de_datos" -> R.drawable.insignia_buen_ojo_de_datos
    "insignia_marea_alta" -> R.drawable.insignia_marea_alta
    "insignia_viento_en_contra" -> R.drawable.insignia_viento_en_contra
    "insignia_brote_firme" -> R.drawable.insignia_brote_firme
    "insignia_eco_certero" -> R.drawable.insignia_eco_certero
    "insignia_chispa_propia" -> R.drawable.insignia_chispa_propia
    "insignia_buen_disolvente" -> R.drawable.insignia_buen_disolvente
    "insignia_cuesta_abajo" -> R.drawable.insignia_cuesta_abajo
    else -> error("Insignia sin arte: $idInsignia")
}

@DrawableRes
fun avatarDrawable(numero: Int): Int {
    val recursos = listOf(
        R.drawable.avatar_01, R.drawable.avatar_02, R.drawable.avatar_03, R.drawable.avatar_04,
        R.drawable.avatar_05, R.drawable.avatar_06, R.drawable.avatar_07, R.drawable.avatar_08,
        R.drawable.avatar_09, R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12,
    )
    return recursos[numero.coerceIn(0, recursos.lastIndex)]
}

@DrawableRes
fun chirimboloPose(pose: String): Int = when (pose) {
    "saluda" -> R.drawable.chirimbolo_saluda
    "senala" -> R.drawable.chirimbolo_senala
    "piensa" -> R.drawable.chirimbolo_piensa
    "escucha" -> R.drawable.chirimbolo_escucha
    "celebra" -> R.drawable.chirimbolo_celebra
    "confundido" -> R.drawable.chirimbolo_confundido
    "explica" -> R.drawable.chirimbolo_explica
    "investiga" -> R.drawable.chirimbolo_investiga
    else -> R.drawable.chirimbolo_saluda
}
