package com.timedead.relojinverso.presentation.timer

import android.app.AppOpsManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.data.TimeLeft
import com.timedead.relojinverso.domain.model.CountryLifeExpectancy
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration
import java.time.temporal.ChronoUnit
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer
import com.timedead.relojinverso.R

/**
 * Pantalla principal del Death Timer
 * Muestra el contador regresivo de vida del usuario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeathTimerMainScreen(
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    
    // Estado del temporizador
    var timeLeft by remember { 
        mutableStateOf(
            TimeLeft(
                years = 50,
                months = 6,
                weeks = 2,
                days = 3,
                hours = 12,
                minutes = 30,
                seconds = 45
            )
        )
    }
    
    // Animación del pulso
    var pulseTarget by remember { mutableStateOf(1f) }
    val scale by animateFloatAsState(
        targetValue = pulseTarget,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "scaleAnimation"
    )
    
    // Simular actualización del temporizador cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            // Actualizar tiempo restante
            val now = LocalDateTime.now()
            val deathDate = now.plusYears(50) // Ejemplo: 50 años más
            timeLeft = calculateTimeLeft(deathDate)
            
            // Animar pulso
            pulseTarget = 1.1f
            delay(300L)
            pulseTarget = 1f
            delay(700L)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ANTICRONO") },
                actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A),
                    titleContentColor = Color(0xFFE6D6B8)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A))
                .padding(paddingValues)
                .padding(horizontal = 10.dp, vertical = 30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "MORIRÁS EN",
                    color = Color(0xFFE6D6B8),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
                
                // Etiquetas de unidades
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("AÑOS", "MESES", "SEMANAS", "DÍAS", "HORAS", "MIN").forEach { unit ->
                        Text(
                            text = unit,
                            color = Color(0xFFE6D6B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.width(60.dp)
                        )
                    }
                }
                
                // Números del contador
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf(
                        timeLeft.years,
                        timeLeft.months,
                        timeLeft.weeks,
                        timeLeft.days,
                        timeLeft.hours,
                        timeLeft.minutes
                    ).forEach { number ->
                        Text(
                            text = number.toString().padStart(2, '0'),
                            color = Color(0xFFE6D6B8),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                // Corazón con contador de segundos
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Corazón animado
                    Text(
                        text = "❤️",
                        fontSize = 180.sp,
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                    )
                    
                    // Contador de segundos en el centro
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0A0A0A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (59 - (timeLeft.seconds % 60)).toString().padStart(2, '0'),
                            color = Color(0xFFE6D6B8),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Valor en días
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "MI VALOR ES",
                        color = Color(0xFFE6D6B8),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "18,250 DÍAS",
                        color = Color(0xFFE6D6B8),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Botón de configuración (mock)
                Button(
                    onClick = { /* TODO: Abrir configuración */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⚙️ Configurar fecha y país")
                }
            }
        }
    }
}

/**
 * Calcula el tiempo restante hasta una fecha futura
 */
private fun calculateTimeLeft(deathDateTime: LocalDateTime): TimeLeft {
    val now = LocalDateTime.now()
    if (now.isAfter(deathDateTime)) {
        return TimeLeft(0, 0, 0, 0, 0, 0, 0)
    }
    
    val duration = Duration.between(now, deathDateTime)
    val totalDays = duration.toDays()
    val years = totalDays / 365
    val remainingDaysAfterYears = totalDays % 365
    val months = remainingDaysAfterYears / 30
    val remainingDaysAfterMonths = remainingDaysAfterYears % 30
    val weeks = remainingDaysAfterMonths / 7
    val days = remainingDaysAfterMonths % 7
    
    val totalHours = duration.toHours()
    val hours = totalHours % 24
    
    val totalMinutes = duration.toMinutes()
    val minutes = totalMinutes % 60
    
    val totalSeconds = duration.seconds
    val seconds = 59 - (totalSeconds % 60)
    
    return TimeLeft(years, months, weeks, days, hours, minutes, seconds)
}
