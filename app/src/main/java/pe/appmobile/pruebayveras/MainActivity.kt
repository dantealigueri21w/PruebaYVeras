package pe.appmobile.pruebayveras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pe.appmobile.pruebayveras.data.AppDatabaseProvider
import pe.appmobile.pruebayveras.ui.navigation.PruebaYVerasNavHost
import pe.appmobile.pruebayveras.ui.theme.PruebaYVerasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabaseProvider.obtener(applicationContext)
        setContent {
            PruebaYVerasTheme {
                PruebaYVerasNavHost(db = db)
            }
        }
    }
}
