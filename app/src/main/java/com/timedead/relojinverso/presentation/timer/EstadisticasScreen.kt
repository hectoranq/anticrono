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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    // Datos de ejemplo - en producción vendrían del ViewModel
    val tdcr = 68.5f // Tasa de Consumo de Reloj
    val tpr = 82.3f // Tasa de Productividad Real
    val horasSueno = 7.5f
    val horasProductivas = 8.5f
    val diasVividos = 12450
    val diasRestantes = 18250
    
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
            
            Spacer(Modifier.height(32.dp))
            
            // Indicadores principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "TDCR",
                    value = "${tdcr.toInt()}%",
                    subtitle = "Tasa Consumo",
                    color = PrimaryRed,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "TPR",
                    value = "${tpr.toInt()}%",
                    subtitle = "Productividad",
                    color = AccentGreen,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Gráfico circular de progreso de vida
            LifeProgressChart(
                diasVividos = diasVividos,
                diasRestantes = diasRestantes
            )
            
            Spacer(Modifier.height(24.dp))
            
            // Métricas detalladas
            Text(
                "MÉTRICAS DIARIAS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(16.dp))
            
            MetricRow(
                icon = Icons.Default.Info,
                title = "Horas de sueño",
                value = "${horasSueno}h",
                color = AccentBlue
            )
            
            Spacer(Modifier.height(12.dp))
            
            MetricRow(
                icon = Icons.Default.Info,
                title = "Horas productivas",
                value = "${horasProductivas}h",
                color = AccentYellow
            )
            
            Spacer(Modifier.height(12.dp))
            
            MetricRow(
                icon = Icons.Default.Info,
                title = "Tiempo libre",
                value = "${24 - horasSueno - horasProductivas}h",
                color = AccentGreen
            )
            
            Spacer(Modifier.height(32.dp))
            
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
                    
                    SummaryItem("Días vividos", "$diasVividos días")
                    SummaryItem("Días restantes (estimado)", "$diasRestantes días")
                    SummaryItem("Porcentaje de vida", "${(diasVividos.toFloat() / (diasVividos + diasRestantes) * 100).toInt()}%")
                }
            }
            
            Spacer(Modifier.height(32.dp))
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
