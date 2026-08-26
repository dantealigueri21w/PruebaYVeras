package pe.appmobile.pruebayveras.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.appmobile.pruebayveras.data.AppDatabase
import pe.appmobile.pruebayveras.ui.screens.ajustes.AjustesScreen
import pe.appmobile.pruebayveras.ui.screens.archipielago.ArchipielagoScreen
import pe.appmobile.pruebayveras.ui.screens.archipielago.ArchipielagoViewModel
import pe.appmobile.pruebayveras.ui.screens.cobertizo.CobertizoScreen
import pe.appmobile.pruebayveras.ui.screens.cobertizo.CobertizoViewModel
import pe.appmobile.pruebayveras.ui.screens.cuaderno.CuadernoScreen
import pe.appmobile.pruebayveras.ui.screens.cuaderno.CuadernoViewModel
import pe.appmobile.pruebayveras.ui.screens.isla.IslaScreen
import pe.appmobile.pruebayveras.ui.screens.isla.IslaViewModel
import pe.appmobile.pruebayveras.ui.screens.perfil.PerfilScreen
import pe.appmobile.pruebayveras.ui.screens.perfil.PerfilViewModel

@Composable
fun PruebaYVerasNavHost(db: AppDatabase) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.ARCHIPIELAGO) {
        composable(Rutas.ARCHIPIELAGO) {
            val viewModel = remember { ArchipielagoViewModel(db) }
            ArchipielagoScreen(
                viewModel = viewModel,
                onAbrirIsla = { navController.navigate(Rutas.isla(it)) },
                onAbrirCuaderno = { navController.navigate(Rutas.CUADERNO) },
                onAbrirCobertizo = { navController.navigate(Rutas.COBERTIZO) },
                onAbrirPerfil = { navController.navigate(Rutas.PERFIL) },
                onAbrirAjustes = { navController.navigate(Rutas.AJUSTES) },
            )
        }
        composable(
            route = Rutas.ISLA,
            arguments = listOf(navArgument("idIsla") { type = NavType.StringType }),
        ) { entrada ->
            val idIsla = entrada.arguments?.getString("idIsla") ?: return@composable
            val viewModel = remember(idIsla) { IslaViewModel(db, idIsla) }
            IslaScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() },
            )
        }
        composable(Rutas.COBERTIZO) {
            val viewModel = remember { CobertizoViewModel(db) }
            CobertizoScreen(viewModel = viewModel, onVolver = { navController.popBackStack() })
        }
        composable(Rutas.CUADERNO) {
            val viewModel = remember { CuadernoViewModel(db) }
            CuadernoScreen(viewModel = viewModel, onVolver = { navController.popBackStack() })
        }
        composable(Rutas.PERFIL) {
            val viewModel = remember { PerfilViewModel(db) }
            PerfilScreen(viewModel = viewModel, onVolver = { navController.popBackStack() })
        }
        composable(Rutas.AJUSTES) { AjustesScreen(onVolver = { navController.popBackStack() }) }
    }
}
