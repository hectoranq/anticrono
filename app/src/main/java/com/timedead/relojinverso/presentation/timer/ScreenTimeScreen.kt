package com.timedead.relojinverso.presentation.timer

import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.R
import com.timedead.relojinverso.ui.theme.RelojinversoTheme
import kotlinx.coroutines.delay
import java.util.Calendar

/**
 * Modelo de datos para el uso de aplicaciones
 */
data class AppUsage(
    val name: String,
    val packageName: String,
    val emoji: String,
    val timeMillis: Long,
    val color: Color,
    val category: String // "ocio" o "productividad"
)

/**
 * Información de apps conocidas
 */
data class AppInfo(
    val packageName: String,
    val name: String,
    val emoji: String,
    val color: Color,
    val category: String
)

/**
 * Pantalla de Tiempo en Pantalla
 * Muestra estadísticas de uso de aplicaciones en tiempo real
 */
@Composable
fun ScreenTimeScreen() {
    val context = LocalContext.current
    
    // Estado para almacenar el uso de apps
    var appsUsage by remember { mutableStateOf<List<AppUsage>>(emptyList()) }
    var totalTimeMillis by remember { mutableStateOf(0L) }
    var ocioTimeMillis by remember { mutableStateOf(0L) }
    var productividadTimeMillis by remember { mutableStateOf(0L) }
    
    // Definir apps conocidas
    val knownApps = remember {
        listOf(
            AppInfo("com.zhiliaoapp.musically", "TikTok", "📱", Color(0xFFEC4899), "ocio"),
            AppInfo("com.instagram.android", "Instagram", "📷", Color(0xFFA855F7), "ocio"),
            AppInfo("com.facebook.katana", "Facebook", "👥", Color(0xFF3B82F6), "ocio"),
            AppInfo("com.google.android.youtube", "YouTube", "▶️", Color(0xFFEF4444), "ocio"),
            AppInfo("com.whatsapp", "WhatsApp", "💬", Color(0xFF22C55E), "productividad"),
            AppInfo("com.trello", "Trello", "📋", Color(0xFF0EA5E9), "productividad"),
            AppInfo("com.google.android.gm", "Gmail", "📧", Color(0xFFEF4444), "productividad")
        )
    }
    
    // Actualizar datos cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            val usageData = getAppUsageStats(context, knownApps)
            appsUsage = usageData
            
            // Calcular totales
            totalTimeMillis = usageData.sumOf { it.timeMillis }
            ocioTimeMillis = usageData.filter { it.category == "ocio" }.sumOf { it.timeMillis }
            productividadTimeMillis = usageData.filter { it.category == "productividad" }.sumOf { it.timeMillis }
            
            delay(1000L) // Actualizar cada segundo
        }
    }
    
    // Calcular horas y minutos totales
    val totalHours = totalTimeMillis / 3_600_000
    val totalMinutes = (totalTimeMillis % 3_600_000) / 60_000
    
    val ocioHours = ocioTimeMillis / 3_600_000
    val ocioMinutes = (ocioTimeMillis % 3_600_000) / 60_000
    
    val prodHours = productividadTimeMillis / 3_600_000
    val prodMinutes = (productividadTimeMillis % 3_600_000) / 60_000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        /** Header **/
        Text(
            text = "TIEMPO EN PANTALLA",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(24.dp))

        /** Total tiempo **/
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF18181B)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_timer_24),
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("HOY", color = Color.Gray, fontSize = 12.sp)

                    Text(
                        text = "${totalHours}h ${totalMinutes}m",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text("de tu día", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /** Resumen Ocio vs Productividad **/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ocio
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🎮 OCIO",
                        color = Color(0xFFF87171),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${ocioHours}h ${ocioMinutes}m",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Productividad
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF052E16)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "💼 PRODUCTIVO",
                        color = Color(0xFF4ADE80),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${prodHours}h ${prodMinutes}m",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /** Traducción en vida **/
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "ESTO EQUIVALE A:",
                    color = Color(0xFFF87171),
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = String.format("%.2f días de vida", totalHours / 24f),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Si usas tu teléfono así cada día durante un año, habrás gastado ${
                        ((totalHours * 365) / 24)
                    } días de tu vida.",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        /** Por aplicación **/
        Text(
            "Por aplicación",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        if (appsUsage.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF18181B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFDC2626),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Cargando datos de uso...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            appsUsage.sortedByDescending { it.timeMillis }.forEach { app ->
                AppUsageItem(app, totalTimeMillis)
                Spacer(Modifier.height(12.dp))
            }
        }

        /** Promedio semanal **/
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF18181B)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("USO DE HOY", color = Color.Gray, fontSize = 12.sp)
                
                Spacer(Modifier.height(4.dp))
                
                val dailyHours = totalTimeMillis / 3_600_000
                val dailyMinutes = (totalTimeMillis % 3_600_000) / 60_000
                
                Text(
                    "${dailyHours}h ${dailyMinutes}m",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Porcentaje del día
                val percentageOfDay = (totalTimeMillis.toFloat() / (24 * 3_600_000)) * 100
                Text(
                    "Equivale al ${String.format("%.1f", percentageOfDay)}% de tu día",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(Modifier.height(24.dp))
    }
}

/**
 * Item individual de uso de aplicación con tiempo real
 */
@Composable
private fun AppUsageItem(app: AppUsage, totalTime: Long) {
    val hours = app.timeMillis / 3_600_000
    val minutes = (app.timeMillis % 3_600_000) / 60_000
    val percentage = if (totalTime > 0) ((app.timeMillis.toFloat() / totalTime) * 100).toInt() else 0
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (app.category == "ocio") 
                Color(0xFF18181B) 
            else 
                Color(0xFF0F1A0F)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = app.emoji,
                    fontSize = 32.sp
                )
                
                Spacer(Modifier.width(12.dp))
                
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = app.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (app.category == "ocio") "🎮" else "💼",
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "${hours}h ${minutes}m",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            
            // Porcentaje
            Text(
                text = "${percentage}%",
                color = app.color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Obtener estadísticas de uso de apps
 */
private fun getAppUsageStats(context: Context, knownApps: List<AppInfo>): List<AppUsage> {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    
    val cal = Calendar.getInstance()
    val endTime = cal.timeInMillis
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    val startTime = cal.timeInMillis
    
    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    )
    
    val appUsageList = mutableListOf<AppUsage>()
    
    knownApps.forEach { appInfo ->
        val usage = stats?.find { it.packageName == appInfo.packageName }
        val timeInForeground = usage?.totalTimeInForeground ?: 0L
        
        if (timeInForeground > 0) {
            appUsageList.add(
                AppUsage(
                    name = appInfo.name,
                    packageName = appInfo.packageName,
                    emoji = appInfo.emoji,
                    timeMillis = timeInForeground,
                    color = appInfo.color,
                    category = appInfo.category
                )
            )
        }
    }
    
    return appUsageList
}

@Preview(showBackground = true)
@Composable
fun ScrenPreview() {
    RelojinversoTheme {
        ScreenTimeScreen()
    }
}