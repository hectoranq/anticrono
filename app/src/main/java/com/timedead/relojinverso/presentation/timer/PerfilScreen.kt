package com.timedead.relojinverso.presentation.timer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.timedead.relojinverso.presentation.viewmodel.ProfileViewModel
import com.timedead.relojinverso.presentation.viewmodel.ProfileState
import com.timedead.relojinverso.domain.model.UserProfile
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/* ---------- COLORES ---------- */

private val PrimaryRed = Color(0xFFB91C1C)
private val AccentGreen = Color(0xFF22C55E)
private val BackgroundDark = Color(0xFF050505)
private val CardBackground = Color(0xFF1A1A1A)
private val GoldText = Color(0xFFE6D6B8)

/* ---------- DATA MODELS ---------- */


/**
 * Pantalla de Perfil
 * Muestra información del usuario y opciones de configuración
 */
@Composable
fun PerfilScreen(
    viewModel: ProfileViewModel,
    onSignOut: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "profile_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        when (state) {
            is ProfileState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryRed)
                }
            }
            is ProfileState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Error al cargar perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryRed
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        (state as ProfileState.Error).message,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadProfile() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryRed
                        )
                    ) {
                        Text("Reintentar")
                    }
                }
            }
            is ProfileState.Success -> {
                val successState = state as ProfileState.Success
                ProfileContent(
                    user = successState.user,
                    profile = successState.profile,
                    pulse = pulse,
                    onEditClick = { showEditDialog = true },
                    onSignOut = {
                        viewModel.signOut()
                        onSignOut()
                    }
                )
                
                if (showEditDialog) {
                    EditProfileDialog(
                        profile = successState.profile,
                        onDismiss = { showEditDialog = false },
                        onSave = { updatedProfile ->
                            viewModel.updateProfile(updatedProfile)
                            showEditDialog = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun ProfileContent(
    user: com.timedead.relojinverso.domain.model.User,
    profile: UserProfile,
    pulse: Float,
    onEditClick: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Spacer(Modifier.height(32.dp))
        
        // Avatar y nombre
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(CardBackground)
                .border(3.dp, PrimaryRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = GoldText,
                modifier = Modifier.size(64.dp)
            )
        }
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            profile.fullName.ifEmpty { user.name },
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = GoldText
        )
        
        Spacer(Modifier.height(4.dp))
        
        Text(
            "Usuario Premium",
            fontSize = 12.sp,
            color = AccentGreen,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        
        Spacer(Modifier.height(32.dp))
        
        // Información del usuario
        Text(
            "INFORMACIÓN PERSONAL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        InfoItem(
            icon = Icons.Default.Email,
            label = "Email",
            value = user.email
        )
        
        Spacer(Modifier.height(12.dp))
        
        InfoItem(
            icon = Icons.Default.DateRange,
            label = "Fecha de Nacimiento",
            value = profile.birthDate.ifEmpty { "No especificada" }
        )
        
        Spacer(Modifier.height(12.dp))
        
        InfoItem(
            icon = Icons.Default.LocationOn,
            label = "País de Nacimiento",
            value = profile.birthCountry.ifEmpty { "No especificado" }
        )
        
        Spacer(Modifier.height(32.dp))
        
        // Acciones
        Text(
            "ACCIONES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        ActionButton(
            icon = Icons.Default.Edit,
            text = "Editar Perfil",
            color = AccentGreen
        ) {
            onEditClick()
        }
        

        Spacer(Modifier.height(12.dp))
        
        ActionButton(
            icon = Icons.Default.ExitToApp,
            text = "Cerrar Sesión",
            color = PrimaryRed
        ) {
            onSignOut()
        }
        
        Spacer(Modifier.height(32.dp))
        
        // Información de la app
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "⏳ ANTICRONO OS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldText,
                    letterSpacing = 2.sp
                )
                
                Spacer(Modifier.height(8.dp))
                
                Text(
                    "Versión 1.0.0",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    "© 2026 Time Dead Technologies",
                    fontSize = 10.sp,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AccentGreen,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(Modifier.width(16.dp))
            
            Column {
                Text(
                    label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    value,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(Modifier.width(16.dp))
            
            Text(
                text,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var fullName by remember { mutableStateOf(profile.fullName) }
    
    // Parsear birthDate de String a LocalDate
    val initialBirthDate = remember {
        try {
            if (profile.birthDate.isNotEmpty()) {
                LocalDate.parse(profile.birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } else {
                LocalDate.of(1990, 1, 1)
            }
        } catch (e: Exception) {
            LocalDate.of(1990, 1, 1)
        }
    }
    var birthDate by remember { mutableStateOf(initialBirthDate) }
    
    var birthCountry by remember { mutableStateOf(profile.birthCountry) }
    var sleepHours by remember { mutableStateOf(profile.sleepHours.toString()) }
    var tprStart by remember { mutableStateOf(profile.tprStart) }
    var tprEnd by remember { mutableStateOf(profile.tprEnd) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Editar Perfil",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldText
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Gray
                        )
                    }
                }
                
                Spacer(Modifier.height(20.dp))
                
                // Campo: Nombre completo
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        focusedLabelColor = AccentGreen,
                        cursorColor = AccentGreen,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Campo: Fecha de nacimiento
                BirthDateFieldForProfile(
                    date = birthDate,
                    onDateChange = { birthDate = it }
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Campo: País
                CountrySelectorForProfile(
                    value = birthCountry,
                    onSelect = { birthCountry = it }
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Campo: Horas de sueño
                OutlinedTextField(
                    value = sleepHours,
                    onValueChange = { if (it.all { char -> char.isDigit() }) sleepHours = it },
                    label = { Text("Horas de Sueño") },
                    placeholder = { Text("8") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        focusedLabelColor = AccentGreen,
                        cursorColor = AccentGreen,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Campo: TPR Inicio
                OutlinedTextField(
                    value = tprStart,
                    onValueChange = { tprStart = it },
                    label = { Text("TPR Inicio (HH:mm)") },
                    placeholder = { Text("09:00") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        focusedLabelColor = AccentGreen,
                        cursorColor = AccentGreen,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Campo: TPR Fin
                OutlinedTextField(
                    value = tprEnd,
                    onValueChange = { tprEnd = it },
                    label = { Text("TPR Fin (HH:mm)") },
                    placeholder = { Text("17:00") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        focusedLabelColor = AccentGreen,
                        cursorColor = AccentGreen,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Gray
                        )
                    ) {
                        Text("Cancelar")
                    }
                    
                    Button(
                        onClick = {
                            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val updatedProfile = UserProfile(
                                fullName = fullName,
                                birthDate = birthDate.format(dateFormatter),
                                birthCountry = birthCountry,
                                sleepHours = sleepHours.toIntOrNull() ?: 8,
                                tprStart = tprStart,
                                tprEnd = tprEnd
                            )
                            onSave(updatedProfile)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen
                        )
                    ) {
                        Text("Guardar", color = Color.Black)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDateFieldForProfile(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    Column {
        Text(
            "Fecha de Nacimiento",
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = date.toString(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, null, tint = AccentGreen)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
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
                    Text("Aceptar", color = AccentGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = CardBackground
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = CardBackground,
                    titleContentColor = GoldText,
                    headlineContentColor = GoldText,
                    weekdayContentColor = Color.Gray,
                    subheadContentColor = GoldText,
                    yearContentColor = GoldText,
                    currentYearContentColor = AccentGreen,
                    selectedYearContainerColor = AccentGreen,
                    selectedDayContainerColor = AccentGreen,
                    todayContentColor = AccentGreen,
                    todayDateBorderColor = AccentGreen,
                    dayContentColor = Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountrySelectorForProfile(
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
        Text(
            "País de Nacimiento",
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
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
                        text = { Text(country, color = Color.White) },
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
