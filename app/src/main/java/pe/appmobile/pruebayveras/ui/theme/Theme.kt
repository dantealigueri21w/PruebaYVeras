package pe.appmobile.pruebayveras.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulPetroleo, onPrimary = Color.White,
    secondary = NaranjaOxido, onSecondary = TintaCasiNegra,
    tertiary = VerdeMusgo, onTertiary = TintaCasiNegra,
    background = PapelDeCampo, onBackground = TintaCasiNegra,
    surface = SuperficieClaraSuave, onSurface = TintaCasiNegra,
    error = ErrorClaro, onError = Color.White,
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulPetroleoOscuro, onPrimary = FondoNocheProfunda,
    secondary = NaranjaOxidoOscuro, onSecondary = FondoNocheProfunda,
    tertiary = VerdeMusgoOscuro, onTertiary = FondoNocheProfunda,
    background = FondoNocheProfunda, onBackground = PapelDeCampo,
    surface = SuperficieNoche, onSurface = PapelDeCampo,
    error = ErrorOscuroFondo, onError = ErrorOscuroTexto,
)

@Composable
fun PruebaYVerasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val esquema = if (darkTheme) EsquemaOscuro else EsquemaClaro
    MaterialTheme(colorScheme = esquema, typography = TipografiaCienciaLab, content = content)
}
