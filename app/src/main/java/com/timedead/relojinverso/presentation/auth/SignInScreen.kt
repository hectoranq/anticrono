package com.timedead.relojinverso.presentation.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.data.intent.AuthIntent
import com.timedead.relojinverso.data.state.AuthState
import com.timedead.relojinverso.ui.theme.RelojinversoTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/* ---------- COLORES ---------- */

private val PrimaryRed = Color(0xFFB91C1C)
private val AccentGreen = Color(0xFF22C55E)
private val BackgroundDark = Color(0xFF050505)
private val GoldText = Color(0xFFE6D6B8)

/* ---------- STATE ---------- */

data class LoginState(
    val email: String = "demo@anticrono.com",
    val password: String = "123456",
    val darkMode: Boolean = true
)

/**
 * Pantalla de inicio de sesión
 */
@Composable
fun SignInScreen(
    authState: StateFlow<AuthState>,
    onIntent: (AuthIntent) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var loginState by remember { mutableStateOf(LoginState()) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val state by authState.collectAsState()
    
    // Observar el estado y navegar si es exitoso
    LaunchedEffect(state) {
        when (state) {
            is AuthState.Success -> {
                onNavigateToHome()
            }
            is AuthState.Error -> {
                errorMessage = (state as AuthState.Error).message
                showError = true
            }
            else -> {}
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (loginState.darkMode) BackgroundDark else Color(0xFFF8FAFC)
    ) {
        Box {
            
            BackgroundDecorations()
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                
                LogoSection()
                
                Spacer(Modifier.height(32.dp))
                
                LoginForm(
                    state = loginState,
                    onEmailChange = { loginState = loginState.copy(email = it) },
                    onPasswordChange = { loginState = loginState.copy(password = it) },
                    onLogin = { onIntent(AuthIntent.SignIn(loginState.email, loginState.password)) },
                    isLoading = state is AuthState.Loading
                )
                
                Spacer(Modifier.height(24.dp))
                
                FooterActions(
                    onForgotPassword = onForgotPassword,
                    onCreateAccount = onNavigateToRegister
                )
                
                // Mostrar error si existe
                if (showError) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = PrimaryRed,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(Modifier.height(24.dp))
                
                // Usuarios demo
                DemoUsersCard()
            }
            
            ThemeToggle(
                darkMode = loginState.darkMode,
                onToggle = { loginState = loginState.copy(darkMode = !loginState.darkMode) }
            )
        }
    }
}


/* ---------- PREVIEW ---------- */

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    RelojinversoTheme {
        SignInScreen(
            authState = MutableStateFlow(AuthState.Idle),
            onIntent = {},
            onNavigateToRegister = {},
            onForgotPassword = {},
            onNavigateToHome = {}
        )
    }
}