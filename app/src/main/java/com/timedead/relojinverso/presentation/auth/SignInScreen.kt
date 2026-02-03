package com.timedead.relojinverso.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.data.intent.AuthIntent
import com.timedead.relojinverso.data.state.AuthState
import kotlinx.coroutines.flow.StateFlow

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
    var email by remember { mutableStateOf("demo@anticrono.com") }
    var password by remember { mutableStateOf("123456") }
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Logo o título
            Text(
                text = "⏳",
                fontSize = 72.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "ANTICRONO",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE6D6B8),
                letterSpacing = 2.sp
            )
            
            Text(
                text = "Death Timer",
                fontSize = 16.sp,
                color = Color(0xFFE6D6B8).copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )
            
            // Campo Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            // Forgot Password
            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("¿Olvidaste tu contraseña?")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botón Sign In
            Button(
                onClick = {
                    onIntent(AuthIntent.SignIn(email, password))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = state !is AuthState.Loading
            ) {
                if (state is AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Iniciar Sesión", fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón Register
            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Crear cuenta", fontSize = 16.sp)
            }
            
            // Mostrar error si existe
            if (showError) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Usuarios demo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "👤 Usuarios Demo:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text("📧 demo@anticrono.com", fontSize = 12.sp)
                    Text("🔑 123456", fontSize = 12.sp)
                }
            }
        }
    }
}
