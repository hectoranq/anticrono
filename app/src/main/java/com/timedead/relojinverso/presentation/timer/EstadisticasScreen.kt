package com.timedead.relojinverso.presentation.timer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.timedead.relojinverso.data.repository.DeathTimerDataRepository
import kotlinx.coroutines.delay

/* ---------- COLORES ---------- */

private val PrimaryRed = Color(0xFFB91C1C)
private val AccentGreen = Color(0xFF22C55E)
private val AccentBlue = Color(0xFF3B82F6)
private val AccentYellow = Color(0xFFF59E0B)
private val BackgroundDark = Color(0xFF050505)
private val CardBackground = Color(0xFF1A1A1A)

/**
 * Pantalla de Estadísticas
 * Muestra métricas, gráficos y análisis de uso del tiempo
 */
@Composable
fun EstadisticasScreen() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    
    // Estado para datos actuales (se actualiza cada segundo)
    var currentData by remember { mutableStateOf(dataRepository.getDeathTimerData()) }
    
    // Actualizar datos cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentData = dataRepository.getDeathTimerData()
        }
    }
    
    // Calcular métricas con datos actuales
    val tdcr = currentData?.let { 
        val totalTimeAwake = (24 - it.horasSueno) * 60f
        val timeConsumed = (it.screenTimeToday / 60000f)
        if (totalTimeAwake > 0) (timeConsumed / totalTimeAwake) * 100f else 0f
    } ?: 0f
    
    val tpr = currentData?.let {
        val totalAwake = (24 - it.horasSueno).toFloat()
        if (totalAwake > 0) (it.horasProductivas / totalAwake) * 100f else 0f
    } ?: 0f
    
    val horasSueno = currentData?.horasSueno?.toFloat() ?: 8f
    val horasProductivas = currentData?.horasProductivas?.toFloat() ?: 0f
    val diasVividos = currentData?.edad?.let { it * 365 } ?: 0
    val diasRestantes = currentData?.diasRestantes?.toInt() ?: 0
    val edad = currentData?.edad ?: 0
    val etapaVida = currentData?.etapaDeVida ?: "Sin datos"
    val porcentajeVivido = (currentData?.porcentajeVidaVivida ?: 0f) * 100f
    
    // Datos de tiempo de pantalla
    val screenTimeHours = (currentData?.screenTimeToday ?: 0L) / 3_600_000f
    val ocioHoras = (currentData?.ocioMillis ?: 0L) / 3_600_000f
    val prodHoras = (currentData?.productividadMillis ?: 0L) / 3_600_000f
    
    // Estado de disponibilidad de datos
    val hasData = currentData != null
    
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
                "ESTADÍSTICAS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                "Análisis de Vida",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            
            // Mostrar edad y etapa de vida
            if (hasData) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        "$edad años - $etapaVida",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(AccentGreen, CircleShape)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "ACTUALIZADO",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                }
            } else {
                Text(
                    "Sin datos disponibles",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
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
                        Text(
                            "⏳",
                            fontSize = 48.sp
                        )
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
                            "Visita la pantalla principal para comenzar a recopilar estadísticas",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Indicadores principales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AnimatedStatCard(
                        title = "TDCR",
                        value = tdcr,
                        maxValue = 100f,
                        subtitle = "Tasa Consumo",
                        color = PrimaryRed,
                        modifier = Modifier.weight(1f)
                    )
                    
                    AnimatedStatCard(
                        title = "TPR",
                        value = tpr,
                        maxValue = 100f,
                        subtitle = "Productividad",
                        color = AccentGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Indicador de tiempo de pantalla
                ScreenTimeIndicator(
                    screenTimeHours = screenTimeHours,
                    ocioHoras = ocioHoras,
                    prodHoras = prodHoras
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Gráfico circular de progreso de vida
                LifeProgressChart(
                    diasVividos = diasVividos,
                    diasRestantes = diasRestantes
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Balance de tiempo diario
                DailyTimeBalance(
                    horasSueno = horasSueno,
                    horasProductivas = horasProductivas,
                    horasScreenTime = screenTimeHours
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Métricas de uso
                Text(
                    "USO DE TIEMPO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Color.Gray
                )
                
                Spacer(Modifier.height(16.dp))
                
                UsageBreakdown(
                    ocioMillis = currentData?.ocioMillis ?: 0L,
                    prodMillis = currentData?.productividadMillis ?: 0L,
                    ocioPct = currentData?.ocioPorcentaje ?: 0f,
                    prodPct = currentData?.productividadPorcentaje ?: 0f
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Resumen estadístico
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = AccentGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            
                            Spacer(Modifier.width(12.dp))
                            
                            Text(
                                "Resumen",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        
                        Divider(color = Color.Gray.copy(alpha = 0.3f))
                        
                        Spacer(Modifier.height(16.dp))
                        
                        SummaryItem("Edad actual", "$edad años")
                        SummaryItem("Etapa de vida", etapaVida)
                        SummaryItem("Días vividos", "$diasVividos días")
                        SummaryItem("Días restantes", "$diasRestantes días")
                        SummaryItem("Vida vivida", "${porcentajeVivido.toInt()}%")
                        
                        Spacer(Modifier.height(8.dp))
                        Divider(color = Color.Gray.copy(alpha = 0.3f))
                        Spacer(Modifier.height(8.dp))
                        
                        SummaryItem("Tiempo pantalla hoy", String.format("%.1fh", screenTimeHours))
                        SummaryItem("Horas de sueño", "${horasSueno.toInt()}h")
                        SummaryItem("Horas productivas", "${horasProductivas.toInt()}h")
                    }
                }
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                value,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                subtitle,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun LifeProgressChart(
    diasVividos: Int,
    diasRestantes: Int
) {
    val total = diasVividos + diasRestantes
    val porcentajeVivido = diasVividos.toFloat() / total
    
    val animatedProgress by animateFloatAsState(
        targetValue = porcentajeVivido,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "PROGRESO DE VIDA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(24.dp))
            
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    val strokeWidth = 20.dp.toPx()
                    
                    // Fondo del círculo
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    )
                    
                    // Progreso vivido
                    drawArc(
                        color = PrimaryRed,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${(animatedProgress * 100).toInt()}%",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryRed
                    )
                    Text(
                        "Vivido",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(color = PrimaryRed, label = "Vivido", value = "$diasVividos días")
                LegendItem(color = AccentGreen, label = "Restante", value = "$diasRestantes días")
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        
        Spacer(Modifier.width(8.dp))
        
        Column {
            Text(
                label,
                fontSize = 10.sp,
                color = Color.Gray
            )
            Text(
                value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun MetricRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(Modifier.width(16.dp))
            
            Text(
                title,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 13.sp,
            color = Color.Gray
        )
        
        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

/**
 * Tarjeta de estadística animada
 */
@Composable
private fun AnimatedStatCard(
    title: String,
    value: Float,
    maxValue: Float,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "stat_value"
    )
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Barra de progreso circular
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(80.dp)) {
                    val strokeWidth = 8.dp.toPx()
                    
                    // Fondo
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    )
                    
                    // Progreso
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360f * (animatedValue / maxValue),
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    )
                }
                
                Text(
                    "${animatedValue.toInt()}%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                subtitle,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Indicador de tiempo de pantalla
 */
@Composable
private fun ScreenTimeIndicator(
    screenTimeHours: Float,
    ocioHoras: Float,
    prodHoras: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "TIEMPO DE PANTALLA HOY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        String.format("%.1fh", screenTimeHours),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        "Total",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(PrimaryRed, CircleShape)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            String.format("%.1fh Ocio", ocioHoras),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(AccentGreen, CircleShape)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            String.format("%.1fh Prod", prodHoras),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Barra de progreso
            val ocioRatio = if (screenTimeHours > 0) ocioHoras / screenTimeHours else 0f
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
            ) {
                if (ocioRatio > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(ocioRatio)
                            .background(PrimaryRed)
                    )
                }
                if (ocioRatio < 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1 - ocioRatio)
                            .background(AccentGreen)
                    )
                }
            }
        }
    }
}

/**
 * Balance de tiempo diario
 */
@Composable
private fun DailyTimeBalance(
    horasSueno: Float,
    horasProductivas: Float,
    horasScreenTime: Float
) {
    val horasLibres = 24f - horasSueno - horasProductivas
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "BALANCE DIARIO (24h)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Sueño
            TimeBalanceItem(
                label = "Sueño",
                hours = horasSueno,
                color = AccentBlue,
                icon = "😴"
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Productivo
            TimeBalanceItem(
                label = "Productivo",
                hours = horasProductivas,
                color = AccentYellow,
                icon = "💼"
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Pantalla
            TimeBalanceItem(
                label = "Pantalla",
                hours = horasScreenTime,
                color = PrimaryRed,
                icon = "📱"
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Tiempo libre
            TimeBalanceItem(
                label = "Libre",
                hours = horasLibres,
                color = AccentGreen,
                icon = "🌟"
            )
        }
    }
}

@Composable
private fun TimeBalanceItem(
    label: String,
    hours: Float,
    color: Color,
    icon: String
) {
    val percentage = (hours / 24f) * 100f
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            icon,
            fontSize = 20.sp
        )
        
        Spacer(Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    label,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    String.format("%.1fh (%.0f%%)", hours, percentage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            
            Spacer(Modifier.height(6.dp))
            
            LinearProgressIndicator(
                progress = percentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = Color.Gray.copy(alpha = 0.2f)
            )
        }
    }
}

/**
 * Desglose de uso (Ocio vs Productividad)
 */
@Composable
private fun UsageBreakdown(
    ocioMillis: Long,
    prodMillis: Long,
    ocioPct: Float,
    prodPct: Float
) {
    val ocioHoras = ocioMillis / 3_600_000f
    val prodHoras = prodMillis / 3_600_000f
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ocio
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🎮",
                        fontSize = 32.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "OCIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        String.format("%.1fh", ocioHoras),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryRed
                    )
                    Text(
                        "${ocioPct.toInt()}%",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(100.dp),
                    color = Color.Gray.copy(alpha = 0.3f)
                )
                
                // Productividad
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "💼",
                        fontSize = 32.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "PRODUCTIVO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        String.format("%.1fh", prodHoras),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentGreen
                    )
                    Text(
                        "${prodPct.toInt()}%",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
