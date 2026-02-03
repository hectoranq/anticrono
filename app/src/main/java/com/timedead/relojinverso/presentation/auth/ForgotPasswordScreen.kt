package com.timedead.relojinverso.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de recuperación de contraseña (Mock)
 */
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "🔑",
                fontSize = 72.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "RECUPERAR CONTRASEÑA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE6D6B8),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Esta funcionalidad estará disponible próximamente.",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
