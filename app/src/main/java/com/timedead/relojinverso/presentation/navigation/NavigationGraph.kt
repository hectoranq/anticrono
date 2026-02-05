package com.timedead.relojinverso.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.timedead.relojinverso.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.timedead.relojinverso.data.intent.AuthIntent
import com.timedead.relojinverso.data.repository.AuthRepositoryImpl
import com.timedead.relojinverso.data.state.AuthState
import com.timedead.relojinverso.domain.model.Route
import com.timedead.relojinverso.presentation.auth.ForgotPasswordScreen
import com.timedead.relojinverso.presentation.auth.RegisterScreen
import com.timedead.relojinverso.presentation.auth.SignInScreen
import com.timedead.relojinverso.presentation.timer.DeathTimerMainScreen
import com.timedead.relojinverso.presentation.timer.FinScreen
import com.timedead.relojinverso.presentation.timer.EstadisticasScreen
import com.timedead.relojinverso.presentation.timer.ValorScreen
import com.timedead.relojinverso.presentation.timer.PerfilScreen
import com.timedead.relojinverso.presentation.viewmodel.AuthViewModel

/**
 * Grafo de navegación principal de la aplicación
 * Implementa Navigation Compose con rutas type-safe y Bottom Navigation
 */
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val authState by authViewModel.state.collectAsState()
    
    // Determinar la pantalla inicial según el estado de autenticación
    val startDestination = when (authState) {
        is AuthState.Success -> Route.Inicio.route
        is AuthState.NotAuthenticated -> Route.SignIn.route
        else -> Route.SignIn.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // AUTENTICACIÓN
        composable(Route.SignIn.route) {
            SignInScreen(
                authState = authViewModel.state,
                onIntent = { intent -> authViewModel.handleIntent(intent) },
                onNavigateToRegister = {
                    navController.navigate(Route.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Route.Inicio.route) {
                        popUpTo(Route.SignIn.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onForgotPassword = {
                    navController.navigate(Route.ForgotPassword.route)
                }
            )
        }
        
        composable(Route.Register.route) {
            RegisterScreen(
                authState = authViewModel.state,
                onIntent = { intent -> authViewModel.handleIntent(intent) },
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Route.Inicio.route) {
                        popUpTo(Route.SignIn.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(Route.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // PANTALLAS PRINCIPALES CON BOTTOM NAVIGATION
        composable(Route.Inicio.route) {
            MainScreenWithBottomNav(
                currentRoute = Route.Inicio.route,
                navController = navController,
                authViewModel = authViewModel
            ) {
                DeathTimerMainScreen(
                    onSignOut = {
                        authViewModel.handleIntent(AuthIntent.SignOut)
                        navController.navigate(Route.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
        
        composable(Route.Fin.route) {
            MainScreenWithBottomNav(
                currentRoute = Route.Fin.route,
                navController = navController,
                authViewModel = authViewModel
            ) {
                FinScreen()
            }
        }
        
        composable(Route.Estadisticas.route) {
            MainScreenWithBottomNav(
                currentRoute = Route.Estadisticas.route,
                navController = navController,
                authViewModel = authViewModel
            ) {
                EstadisticasScreen()
            }
        }
        
        composable(Route.Valor.route) {
            MainScreenWithBottomNav(
                currentRoute = Route.Valor.route,
                navController = navController,
                authViewModel = authViewModel
            ) {
                ValorScreen()
            }
        }
        
        composable(Route.Perfil.route) {
            MainScreenWithBottomNav(
                currentRoute = Route.Perfil.route,
                navController = navController,
                authViewModel = authViewModel
            ) {
                PerfilScreen(
                    onSignOut = {
                        authViewModel.handleIntent(AuthIntent.SignOut)
                        navController.navigate(Route.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
        
        // CONFIGURACIÓN (Futuro)
        composable(Route.Settings.route) {
            // TODO: Implementar pantalla de configuración
        }
    }
}

/**
 * Scaffold con Bottom Navigation Bar para las pantallas principales
 */
@Composable
private fun MainScreenWithBottomNav(
    currentRoute: String,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        // Evitar múltiples copias de la misma pantalla
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            content()
        }
    }
}

/**
 * Bottom Navigation Bar con las 4 secciones principales
 */
@Composable
private fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            route = Route.Inicio.route,
            icon = Icons.Default.Home,
            label = "INICIO"
        ),
        BottomNavItem(
            route = Route.Fin.route,
            iconDrawable = R.drawable.outline_skull_24,
            label = "FIN"
        ),
        BottomNavItem(
            route = Route.Estadisticas.route,
            iconDrawable = R.drawable.outline_bar_chart_4_bars_24,
            label = "ESTAS"
        ),
        BottomNavItem(
            route = Route.Valor.route,
            icon = Icons.Default.AccountCircle,
            label = "VALOR"
        ),
        BottomNavItem(
            route = Route.Perfil.route,
            icon = Icons.Default.Person,
            label = "PERFIL"
        )
    )
    
    NavigationBar(
        containerColor = Color(0xFF0A0A0A),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when {
                        item.icon != null -> Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) Color(0xFFDC2626) else Color.Gray
                        )
                        item.iconDrawable != null -> Icon(
                            painter = painterResource(id = item.iconDrawable),
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) Color(0xFFDC2626) else Color.Gray
                        )
                    }
                },
                label = {
                    Text(
                        item.label,
                        fontSize = 10.sp,
                        fontWeight = if (currentRoute == item.route) FontWeight.Bold else FontWeight.Normal,
                        letterSpacing = 1.sp
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFDC2626),
                    selectedTextColor = Color(0xFFDC2626),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFF1A1A1A)
                )
            )
        }
    }
}

/**
 * Data class para items del Bottom Navigation
 */
private data class BottomNavItem(
    val route: String,
    val icon: ImageVector? = null,
    val iconDrawable: Int? = null,
    val label: String
)
