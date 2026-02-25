package com.timedead.relojinverso.presentation.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.data.repository.DeathTimerDataRepository
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

/* ---------- COLORES ---------- */

private val PrimaryRed = Color(0xFFB91C1C)
private val AccentGreen = Color(0xFF22C55E)
private val AccentGold = Color(0xFFEAB308)
private val AccentPurple = Color(0xFF9333EA)
private val BackgroundDark = Color(0xFF050505)
private val CardBackground = Color(0xFF1A1A1A)

/**
 * Pantalla de Valor
 * Muestra el valor del tiempo restante en días y su traducción a eventos de la vida
 */
@Composable
fun ValorScreen() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    
    // Estado para datos actuales (se actualiza cada segundo)
    var currentData by remember { mutableStateOf(dataRepository.getDeathTimerData()) }
    var expanded by remember { mutableStateOf(false) }
    
    // Actualizar datos cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentData = dataRepository.getDeathTimerData()
        }
    }
    
    val diasRestantes = currentData?.diasRestantes ?: 0L
    val hasData = currentData != null
    
    // Calcular eventos de vida basados en días restantes
    val eventos = remember(diasRestantes) {
        calcularEventosDeVida(diasRestantes)
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "value_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            
            Spacer(Modifier.height(32.dp))
            
            // Header
            Text(
                "MI VALOR",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                "Tiempo de Vida",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            
            if (hasData) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        "${currentData?.edad} años - ${currentData?.etapaDeVida}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(AccentGreen, CircleShape)
                    )
                }
            }
            
            Spacer(Modifier.height(32.dp))
            
            if (!hasData) {
                // Mostrar mensaje cuando no hay datos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⏳", fontSize = 48.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Esperando datos...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Visita la pantalla principal para comenzar",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Valor principal - Clickeable para expandir
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        AccentGold.copy(alpha = 0.3f),
                                        AccentPurple.copy(alpha = 0.2f),
                                        CardBackground
                                    )
                                )
                            )
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(64.dp)
                            )
                            
                            Spacer(Modifier.height(16.dp))
                            
                            Text(
                                "MI VALOR ES",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp,
                                color = Color.Gray
                            )
                            
                            Spacer(Modifier.height(12.dp))
                            
                            Text(
                                text = NumberFormat.getInstance(Locale.US).format(diasRestantes),
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AccentGold,
                                textAlign = TextAlign.Center
                            )
                            
                            Text(
                                "DÍAS",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                            
                            Spacer(Modifier.height(16.dp))
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = AccentGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    if (expanded) "Ocultar desglose" else "Ver en eventos de vida",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AccentGold
                                )
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Desglose expandible de eventos
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Text(
                            "ESTO SIGNIFICA QUE PUEDES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        
                        eventos.forEach { evento ->
                            LifeEventCard(evento)
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
                
                Spacer(Modifier.height(24.dp))
                
                // Conversiones de tiempo
                Text(
                    "CONVERSIONES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Color.Gray
                )
                
                Spacer(Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeConversionCard(
                        value = (diasRestantes / 365).toString(),
                        unit = "AÑOS",
                        color = AccentGreen,
                        modifier = Modifier.weight(1f)
                    )
                    
                    TimeConversionCard(
                        value = (diasRestantes / 30).toString(),
                        unit = "MESES",
                        color = AccentPurple,
                        modifier = Modifier.weight(1f)
                    )
                    
                    TimeConversionCard(
                        value = (diasRestantes / 7).toString(),
                        unit = "SEMANAS",
                        color = PrimaryRed,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeConversionCard(
                        value = (diasRestantes * 24).toString(),
                        unit = "HORAS",
                        color = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    TimeConversionCard(
                        value = NumberFormat.getInstance(Locale.US).format(diasRestantes * 1440),
                        unit = "MINUTOS",
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(Modifier.height(32.dp))
                
                // Nota motivacional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentGold.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "💡",
                            fontSize = 32.sp
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "Cada día cuenta. Estos ${NumberFormat.getInstance(Locale.US).format(diasRestantes)} días son oportunidades para crear recuerdos, lograr metas y vivir con propósito.",
                            fontSize = 13.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )
                    }
                }
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Data class para eventos de vida
 */
data class LifeEvent(
    val emoji: String,
    val title: String,
    val description: String,
    val quantity: Long,
    val color: Color
)

/**
 * Calcular eventos de vida basados en días restantes
 */
fun calcularEventosDeVida(diasRestantes: Long): List<LifeEvent> {
    return listOf(
        LifeEvent(
            emoji = "☕",
            title = "Tazas de café",
            description = "Momentos para reflexionar y disfrutar",
            quantity = diasRestantes * 2, // 2 cafés por día
            color = Color(0xFF92400E)
        ),
        LifeEvent(
            emoji = "📚",
            title = "Libros que puedes leer",
            description = "Conocimiento y aventuras por descubrir",
            quantity = (diasRestantes / 7), // 1 libro por semana
            color = Color(0xFF1E40AF)
        ),
        LifeEvent(
            emoji = "🎬",
            title = "Películas que ver",
            description = "Historias que te inspirarán",
            quantity = (diasRestantes / 2), // 1 película cada 2 días
            color = Color(0xFF7C2D12)
        ),
        LifeEvent(
            emoji = "✈️",
            title = "Viajes posibles",
            description = "Nuevos lugares por explorar",
            quantity = (diasRestantes / 90), // 1 viaje cada 3 meses
            color = Color(0xFF0891B2)
        ),
        LifeEvent(
            emoji = "❤️",
            title = "Conversaciones significativas",
            description = "Conexiones profundas con seres queridos",
            quantity = diasRestantes, // 1 conversación por día
            color = PrimaryRed
        ),
        LifeEvent(
            emoji = "🎯",
            title = "Metas que alcanzar",
            description = "Logros personales y profesionales",
            quantity = (diasRestantes / 30), // 1 meta por mes
            color = AccentGreen
        ),
        LifeEvent(
            emoji = "🌅",
            title = "Amaneceres que presenciar",
            description = "Nuevos comienzos cada día",
            quantity = diasRestantes,
            color = AccentGold
        ),
        LifeEvent(
            emoji = "🍽️",
            title = "Comidas memorables",
            description = "Experiencias culinarias únicas",
            quantity = diasRestantes * 3, // 3 comidas al día
            color = Color(0xFFDB2777)
        ),
        LifeEvent(
            emoji = "🎵",
            title = "Canciones nuevas",
            description = "Música que marcará tu vida",
            quantity = diasRestantes * 10, // 10 canciones por día
            color = AccentPurple
        ),
        LifeEvent(
            emoji = "💪",
            title = "Días de ejercicio",
            description = "Para mantenerte saludable y fuerte",
            quantity = (diasRestantes / 2), // Ejercicio cada 2 días
            color = Color(0xFF059669)
        )
    )
}

/**
 * Tarjeta de evento de vida
 */
@Composable
private fun LifeEventCard(event: LifeEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji y círculo de color
            Box(
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(event.color.copy(alpha = 0.2f), CircleShape)
                )
                Text(
                    event.emoji,
                    fontSize = 32.sp
                )
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    NumberFormat.getInstance(Locale.US).format(event.quantity),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = event.color
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    event.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    event.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/**
 * Tarjeta de conversión de tiempo
 */
@Composable
private fun TimeConversionCard(
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                maxLines = 1
            )
            Spacer(Modifier.height(4.dp))
            Text(
                unit,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = Color.Gray
            )
        }
    }
}
