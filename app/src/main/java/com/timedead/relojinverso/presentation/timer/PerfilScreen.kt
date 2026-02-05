package com.timedead.relojinverso.presentation.timer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- COLORES ---------- */

private val PrimaryRed = Color(0xFFB91C1C)
private val AccentGreen = Color(0xFF22C55E)
private val BackgroundDark = Color(0xFF050505)
private val CardBackground = Color(0xFF1A1A1A)
private val GoldText = Color(0xFFE6D6B8)

/**
 * Pantalla de Perfil
 * Muestra información del usuario y opciones de configuración
 */
@Composable
fun PerfilScreen(
    onSignOut: () -> Unit = {}
) {
    // Datos de ejemplo - en producción vendrían del ViewModel
    val nombreUsuario = "Demo User"
    val email = "demo@anticrono.com"
    val fechaNacimiento = "01/01/1990"
    val pais = "México"
    
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
                nombreUsuario,
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
                value = email
            )
            
            Spacer(Modifier.height(12.dp))
            
            InfoItem(
                icon = Icons.Default.DateRange,
                label = "Fecha de Nacimiento",
                value = fechaNacimiento
            )
            
            Spacer(Modifier.height(12.dp))
            
            InfoItem(
                icon = Icons.Default.LocationOn,
                label = "País",
                value = pais
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
                // TODO: Navegar a edición de perfil
            }
            
            Spacer(Modifier.height(12.dp))
            
            ActionButton(
                icon = Icons.Default.Settings,
                text = "Configuración",
                color = Color(0xFF3B82F6)
            ) {
                // TODO: Navegar a configuración
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
