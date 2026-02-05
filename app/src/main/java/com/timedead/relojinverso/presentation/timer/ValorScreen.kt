package com.timedead.relojinverso.presentation.timer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * Muestra el valor del tiempo, análisis económico y métricas de optimización
 */
@Composable
fun ValorScreen() {
    // Datos de ejemplo - en producción vendrían del ViewModel
    val horasRestantes = 438000 // 50 años aprox
    val valorPorHora = 25.0 // USD
    val valorTotalVida = horasRestantes * valorPorHora
    val horasAhorradasMesActual = 120
    val valorAhorrado = horasAhorradasMesActual * valorPorHora
    val eficiencia = 78.5f
    
    val infiniteTransition = rememberInfiniteTransition(label = "money_pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
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
                "VALOR DE TIEMPO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                "Economía Vital",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Valor total principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    AccentGold.copy(alpha = 0.2f),
                                    AccentPurple.copy(alpha = 0.2f)
                                )
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier
                                .size(64.dp)

                        )
                        
                        Spacer(Modifier.height(16.dp))
                        
                        Text(
                            "VALOR TOTAL DE VIDA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Color.Gray
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            formatCurrency(valorTotalVida),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentGold,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            "${NumberFormat.getInstance(Locale.US).format(horasRestantes)} horas restantes × $$valorPorHora/hora",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Métricas de valor
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ValueMetricCard(
                    icon = Icons.Default.Share,
                    title = "Valor/Hora",
                    value = "$$valorPorHora",
                    color = AccentGreen,
                    modifier = Modifier.weight(1f)
                )
                
                ValueMetricCard(
                    icon = Icons.Default.Star,
                    title = "Eficiencia",
                    value = "${eficiencia.toInt()}%",
                    color = AccentPurple,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Ahorro del mes
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(AccentGreen.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = AccentGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Spacer(Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                "AHORRO ESTE MES",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = Color.Gray
                            )
                            
                            Spacer(Modifier.height(4.dp))
                            
                            Text(
                                formatCurrency(valorAhorrado),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AccentGreen
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Horas optimizadas",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        
                        Text(
                            "${horasAhorradasMesActual}h",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Desglose de valor
            Text(
                "DESGLOSE DE VALOR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(16.dp))
            
            ValueBreakdownItem(
                label = "Valor por día (24h)",
                value = formatCurrency(valorPorHora * 24)
            )
            
            Spacer(Modifier.height(12.dp))
            
            ValueBreakdownItem(
                label = "Valor por semana",
                value = formatCurrency(valorPorHora * 24 * 7)
            )
            
            Spacer(Modifier.height(12.dp))
            
            ValueBreakdownItem(
                label = "Valor por mes (30 días)",
                value = formatCurrency(valorPorHora * 24 * 30)
            )
            
            Spacer(Modifier.height(12.dp))
            
            ValueBreakdownItem(
                label = "Valor por año",
                value = formatCurrency(valorPorHora * 24 * 365)
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Nota informativa
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AccentGold.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "💡 El valor por hora se calcula basándose en tu potencial económico, productividad y expectativa de vida. Es una métrica orientativa para optimizar tu tiempo.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ValueMetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
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
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

@Composable
private fun ValueBreakdownItem(
    label: String,
    value: String
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                fontSize = 13.sp,
                color = Color.White
            )
            
            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(value)
}
