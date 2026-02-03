package com.timedead.relojinverso.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timedead.relojinverso.data.intent.AuthIntent
import com.timedead.relojinverso.data.repository.AuthRepositoryImpl
import com.timedead.relojinverso.data.state.AuthState
import com.timedead.relojinverso.domain.model.Route
import com.timedead.relojinverso.presentation.auth.ForgotPasswordScreen
import com.timedead.relojinverso.presentation.auth.RegisterScreen
import com.timedead.relojinverso.presentation.auth.SignInScreen
import com.timedead.relojinverso.presentation.timer.DeathTimerMainScreen
import com.timedead.relojinverso.presentation.viewmodel.AuthViewModel

/**
 * Grafo de navegación principal de la aplicación
 * Implementa Navigation Compose con rutas type-safe
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
        is AuthState.Success -> Route.DeathTimerHome.route
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
                    navController.navigate(Route.DeathTimerHome.route) {
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
                    navController.navigate(Route.DeathTimerHome.route) {
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
        
        // PANTALLA PRINCIPAL - DEATH TIMER
        composable(Route.DeathTimerHome.route) {
            DeathTimerMainScreen(
                onSignOut = {
                    authViewModel.handleIntent(AuthIntent.SignOut)
                    navController.navigate(Route.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // CONFIGURACIÓN (Futuro)
        composable(Route.Settings.route) {
            // TODO: Implementar pantalla de configuración
        }
    }
}
