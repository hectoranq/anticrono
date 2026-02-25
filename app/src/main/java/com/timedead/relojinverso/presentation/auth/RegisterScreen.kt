package com.timedead.relojinverso.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
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
import com.timedead.relojinverso.domain.model.UserProfile
import com.timedead.relojinverso.ui.theme.RelojinversoTheme
import com.timedead.relojinverso.presentation.timer.jsonLifeExpectancyList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/* ---------- COLORES ---------- */

private val Primary = Color(0xFF22C55E)
private val Danger = Color(0xFFDC2626)
private val BackgroundDark = Color(0xFF0A0A0A)
private val GoldText = Color(0xFFE6D6B8)

/* ---------- DATA MODELS ---------- */

data class CountryLifeExpectancy(
    val Rank: Int,
    val Country: String,
    @com.google.gson.annotations.SerializedName("Life Expectancy (both sexes)")
    val lifeExpectancy: Double,
    @com.google.gson.annotations.SerializedName("Females Life Expectancy")
    val femalesLifeExpectancy: Double,
    @com.google.gson.annotations.SerializedName("Males Life Expectancy")
    val malesLifeExpectancy: Double
)

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
                        
                        // Crear UserProfile con los datos del formulario
                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                        
                        val profile = UserProfile(
                            fullName = formState.name,
                            birthDate = formState.birthDate.format(dateFormatter),
                            birthCountry = formState.country,
                            sleepHours = formState.sleepHours.toInt(),
                            tprStart = formState.startTime.format(timeFormatter),
                            tprEnd = formState.endTime.format(timeFormatter)
                        )
                        
                        onIntent(AuthIntent.Register(
                            name = formState.name,
                            email = formState.email,
                            password = formState.password,
                            profile = profile
                        ))
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
            
            BirthDateField(
                date = formState.birthDate,
                onDateChange = { formState = formState.copy(birthDate = it) }
            )
            
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDateField(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    Column {
        FieldLabel("Fecha de nacimiento")
        OutlinedTextField(
            value = date.toString(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, null, tint = Primary)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,
                disabledBorderColor = Color.Gray
            ),
            enabled = false
        )
    }
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateChange(selectedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = Primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = BackgroundDark
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = BackgroundDark,
                    titleContentColor = GoldText,
                    headlineContentColor = GoldText,
                    weekdayContentColor = Color.Gray,
                    subheadContentColor = GoldText,
                    yearContentColor = GoldText,
                    currentYearContentColor = Primary,
                    selectedYearContainerColor = Primary,
                    selectedDayContainerColor = Primary,
                    todayContentColor = Primary,
                    todayDateBorderColor = Primary,
                    dayContentColor = Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountrySelector(
    value: String,
    onSelect: (String) -> Unit
) {
    val countries = remember {
        try {
            val gson = Gson()
            val type = object : TypeToken<List<CountryLifeExpectancy>>() {}.type
            val countryList: List<CountryLifeExpectancy> = gson.fromJson(jsonLifeExpectancyList, type)
            countryList.map { it.Country }.sorted()
        } catch (e: Exception) {
            listOf("España", "México", "Argentina", "Colombia", "Chile", "Perú", "Venezuela", "Ecuador", "Uruguay", "Paraguay", "Bolivia", "Costa Rica", "Cuba", "República Dominicana", "El Salvador", "Guatemala", "Honduras", "Nicaragua", "Panamá", "Puerto Rico")
        }
    }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(value) }
    
    val filteredCountries = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            countries
        } else {
            countries.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }
    
    Column {
        FieldLabel("País de nacimiento")
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    expanded = true
                },
                placeholder = { Text("Escribe para buscar tu país", color = Color.Gray) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            
            ExposedDropdownMenu(
                expanded = expanded && filteredCountries.isNotEmpty(),
                onDismissRequest = { expanded = false }
            ) {
                filteredCountries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            onSelect(country)
                            searchQuery = country
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
            TimeField("Hora inicial", start, onStartChange)
            TimeField("Hora final", end, onEndChange)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RowScope.TimeField(
    label: String, 
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.weight(1f)) {
        Text(
            label.uppercase(),
            fontSize = 9.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = String.format("%02d:%02d", time.hour, time.minute),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,
                disabledBorderColor = Color.Gray
            ),
            enabled = false
        )
    }
    
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = time.hour,
            initialMinute = time.minute,
            is24Hour = true
        )
        
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedTime = LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        onTimeChange(selectedTime)
                        showTimePicker = false
                    }
                ) {
                    Text("Aceptar", color = Primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            containerColor = BackgroundDark,
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = BackgroundDark,
                        selectorColor = Primary,
                        containerColor = BackgroundDark,
                        periodSelectorBorderColor = Primary,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color.Gray,
                        periodSelectorSelectedContainerColor = Primary,
                        periodSelectorUnselectedContainerColor = Color.Gray,
                        timeSelectorSelectedContainerColor = Primary,
                        timeSelectorUnselectedContainerColor = Color.Gray.copy(alpha = 0.3f),
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = Color.White
                    )
                )
            }
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