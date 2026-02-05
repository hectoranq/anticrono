package com.timedead.relojinverso.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.data.intent.AuthIntent
import com.timedead.relojinverso.data.state.AuthState
import com.timedead.relojinverso.ui.theme.RelojinversoTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalTime

/* ---------- COLORES ---------- */

private val Primary = Color(0xFF22C55E)
private val Danger = Color(0xFFDC2626)
private val BackgroundDark = Color(0xFF0A0A0A)
private val GoldText = Color(0xFFE6D6B8)

/* ---------- STATE ---------- */

data class RegisterFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: LocalDate = LocalDate.of(1990, 1, 1),
    val country: String = "",
    val sleepHours: Float = 8f,
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(18, 0)
)

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
    var formState by remember { mutableStateOf(RegisterFormState()) }
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
    
    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            FooterRegisterButton(
                enabled = state !is AuthState.Loading,
                isLoading = state is AuthState.Loading
            ) {
                when {
                    formState.name.isBlank() -> {
                        errorMessage = "El nombre es requerido"
                        showError = true
                    }
                    formState.email.isBlank() -> {
                        errorMessage = "El email es requerido"
                        showError = true
                    }
                    formState.password.isBlank() -> {
                        errorMessage = "La contraseña es requerida"
                        showError = true
                    }
                    formState.password != formState.confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden"
                        showError = true
                    }
                    formState.password.length < 6 -> {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        showError = true
                    }
                    formState.country.isBlank() -> {
                        errorMessage = "Selecciona un país"
                        showError = true
                    }
                    else -> {
                        showError = false
                        onIntent(AuthIntent.Register(formState.name, formState.email, formState.password))
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            
            HeaderSection()
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                "Completa tu perfil para acceder al sistema de análisis temporal TDCR.",
                color = Color.Gray,
                fontSize = 13.sp
            )
            
            Spacer(Modifier.height(24.dp))
            
            // Campos de autenticación
            FieldLabel("Nombre completo")
            StyledTextField(
                value = formState.name,
                onValueChange = { formState = formState.copy(name = it) },
                placeholder = "Tu nombre completo"
            )
            
            Spacer(Modifier.height(16.dp))
            
            FieldLabel("Email")
            StyledTextField(
                value = formState.email,
                onValueChange = { formState = formState.copy(email = it) },
                placeholder = "tu@email.com",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            
            Spacer(Modifier.height(16.dp))
            
            FieldLabel("Contraseña")
            StyledTextField(
                value = formState.password,
                onValueChange = { formState = formState.copy(password = it) },
                placeholder = "Mínimo 6 caracteres",
                isPassword = true
            )
            
            Spacer(Modifier.height(16.dp))
            
            FieldLabel("Confirmar contraseña")
            StyledTextField(
                value = formState.confirmPassword,
                onValueChange = { formState = formState.copy(confirmPassword = it) },
                placeholder = "Repite tu contraseña",
                isPassword = true
            )
            
            Spacer(Modifier.height(24.dp))
            
            // Separador visual
            Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                "PARÁMETROS BIOLÓGICOS",
                fontSize = 10.sp,
                color = Primary,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(16.dp))
            
            BirthDateField(formState.birthDate)
            
            Spacer(Modifier.height(20.dp))
            
            CountrySelector(
                value = formState.country,
                onSelect = { formState = formState.copy(country = it) }
            )
            
            Spacer(Modifier.height(24.dp))
            
            SleepSlider(
                hours = formState.sleepHours,
                onChange = { formState = formState.copy(sleepHours = it) }
            )
            
            Spacer(Modifier.height(28.dp))
            
            ProductivityTimeSection(
                start = formState.startTime,
                end = formState.endTime,
                onStartChange = { formState = formState.copy(startTime = it) },
                onEndChange = { formState = formState.copy(endTime = it) }
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Botón volver a Sign In
            TextButton(
                onClick = onNavigateToSignIn,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión",
                    color = Color.Gray
                )
            }
            
            // Mostrar error si existe
            if (showError) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = Danger,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(Modifier.height(40.dp))
        }
    }
}

/* ---------- HEADER ---------- */

@Composable
private fun HeaderSection() {

    Text(
        "PASO 01",
        color = Primary,
        fontSize = 10.sp,
        letterSpacing = 3.sp,
        fontWeight = FontWeight.Bold
    )
    
    Spacer(Modifier.height(6.dp))
    
    Text(
        "Registro:\nCrear Cuenta Nueva",
        fontSize = 26.sp,
        fontWeight = FontWeight.ExtraBold,
        color = GoldText,
        textAlign = TextAlign.Start
    )
}

/* ---------- CAMPOS ---------- */

@Composable
private fun FieldLabel(text: String) {
    Text(
        text.uppercase(),
        fontSize = 10.sp,
        color = Color.Gray,
        letterSpacing = 2.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = if (isPassword) {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        } else {
            keyboardOptions
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
private fun BirthDateField(date: LocalDate) {
    Column {
        FieldLabel("Fecha de nacimiento")
        OutlinedTextField(
            value = date.toString(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, null, tint = Primary)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountrySelector(
    value: String,
    onSelect: (String) -> Unit
) {
    val countries = listOf("España", "México", "Argentina", "Colombia", "Chile", "Perú", "Venezuela", "Ecuador", "Uruguay", "Paraguay")
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        FieldLabel("País de nacimiento")
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecciona tu país", color = Color.Gray) },
                trailingIcon = {
                    Icon(Icons.Default.Search, null, tint = Primary)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onSelect(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/* ---------- SLEEP SLIDER ---------- */

@Composable
private fun SleepSlider(
    hours: Float,
    onChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FieldLabel("Horas de sueño")
            Text(
                "${hours.toInt()}h",
                color = Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        
        Slider(
            value = hours,
            onValueChange = onChange,
            valueRange = 4f..12f,
            steps = 7,
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Primary,
                inactiveTrackColor = Color.Gray
            )
        )
        
        Text(
            "* Define tu promedio de horas de descanso diarias.",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/* ---------- PRODUCTIVITY ---------- */

@Composable
private fun ProductivityTimeSection(
    start: LocalTime,
    end: LocalTime,
    onStartChange: (LocalTime) -> Unit,
    onEndChange: (LocalTime) -> Unit
) {
    Column {
        FieldLabel("Horas productivas (Ventana TPR)")
        
        Spacer(Modifier.height(12.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeField("Hora inicial", start)
            TimeField("Hora final", end)
        }
        
        Spacer(Modifier.height(8.dp))
        
        Text(
            "* Estos datos definen tu Tasa de Productividad Real (TPR).",
            fontSize = 10.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RowScope.TimeField(label: String, time: LocalTime) {
    Column(modifier = Modifier.weight(1f)) {
        Text(
            label.uppercase(),
            fontSize = 9.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = time.toString(),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

/* ---------- FOOTER ---------- */

@Composable
private fun FooterRegisterButton(
    enabled: Boolean,
    isLoading: Boolean,
    onConfirm: () -> Unit
) {
    Button(
        onClick = onConfirm,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Danger),
        shape = RoundedCornerShape(20.dp),
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White
            )
        } else {
            Text(
                "CREAR CUENTA",
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.AccountCircle, null)
        }
    }
}

/* ---------- PREVIEW ---------- */

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RelojinversoTheme {
        RegisterScreen(
            authState = MutableStateFlow(AuthState.Idle),
            onIntent = {},
            onNavigateToSignIn = {},
            onNavigateToHome = {}
        )
    }
}