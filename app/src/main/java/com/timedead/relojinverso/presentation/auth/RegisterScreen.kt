package com.timedead.relojinverso.presentation.auth

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
 * Pantalla de registro de usuario
 */
@Composable
fun RegisterScreen(
    authState: StateFlow<AuthState>,
    onIntent: (AuthIntent) -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                text = "CREAR CUENTA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE6D6B8),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
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
                    .padding(bottom = 16.dp),
                singleLine = true
            )
            
            // Campo Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = true
            )
            
            // Botón Register
            Button(
                onClick = {
                    when {
                        name.isBlank() -> {
                            errorMessage = "El nombre es requerido"
                            showError = true
                        }
                        email.isBlank() -> {
                            errorMessage = "El email es requerido"
                            showError = true
                        }
                        password.isBlank() -> {
                            errorMessage = "La contraseña es requerida"
                            showError = true
                        }
                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden"
                            showError = true
                        }
                        password.length < 6 -> {
                            errorMessage = "La contraseña debe tener al menos 6 caracteres"
                            showError = true
                        }
                        else -> {
                            showError = false
                            onIntent(AuthIntent.Register(name, email, password))
                        }
                    }
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
                    Text("Registrarse", fontSize = 16.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón volver a Sign In
            TextButton(
                onClick = onNavigateToSignIn
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
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
        }
    }
}
