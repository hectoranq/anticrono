package com.timedead.relojinverso.presentation.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.OutlinedButtonDefaults

/* ---------- BACKGROUND ---------- */

@Composable
internal fun BackgroundDecorations() {
    val PrimaryRed = Color(0xFFB91C1C)
    val AccentGreen = Color(0xFF22C55E)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset((-80).dp, (-80).dp)
                .background(PrimaryRed.copy(alpha = 0.1f), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomEnd)
                .offset(80.dp, 80.dp)
                .background(AccentGreen.copy(alpha = 0.05f), shape = CircleShape)
        )
    }
}

/* ---------- LOGO ---------- */

@Composable
internal fun LogoSection() {
    val PrimaryRed = Color(0xFFB91C1C)
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = EaseInOut),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(pulse)
                .border(2.dp, PrimaryRed.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = PrimaryRed,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(Modifier.height(12.dp))
        
        Text(
            "ANTICRONO OS",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = Color.White
        )
        
        Text(
            "OPTIMIZACIÓN DE VIDA HUMANA",
            fontSize = 11.sp,
            letterSpacing = 3.sp,
            color = Color.Gray
        )
    }
}

/* ---------- FORM ---------- */

@Composable
internal fun LoginForm(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    isLoading: Boolean
) {
    val PrimaryRed = Color(0xFFB91C1C)
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        
        InputField(
            label = "Correo electrónico",
            value = state.email,
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            onValueChange = onEmailChange
        )
        
        InputField(
            label = "Contraseña",
            value = state.password,
            icon = Icons.Default.Lock,
            isPassword = true,
            onValueChange = onPasswordChange
        )
        
        Spacer(Modifier.height(8.dp))
        
        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    "ENTRAR",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
    }
}

/* ---------- INPUT ---------- */

@Composable
internal fun InputField(
    label: String,
    value: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val AccentGreen = Color(0xFF22C55E)
    
    Column {
        Text(
            label.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            letterSpacing = 1.5.sp
        )
        
        Spacer(Modifier.height(8.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(icon, null, tint = AccentGreen)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}

/* ---------- FOOTER ---------- */

@Composable
internal fun FooterActions(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit
) {
    val AccentGreen = Color(0xFF22C55E)
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        
        TextButton(onClick = onForgotPassword) {
            Text("¿Olvidaste tu contraseña?", color = Color.Gray)
        }
        
        Spacer(Modifier.height(12.dp))
        
        DividerWithText("O")
        
        Spacer(Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = onCreateAccount,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors =  ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, AccentGreen)
        ) {
            Icon(Icons.Default.Person, null)
            Spacer(Modifier.width(8.dp))
            Text("Crear una cuenta")
        }
    }
}

/* ---------- HELPERS ---------- */

@Composable
internal fun DividerWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
        Text(
            text,
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
    }
}

@Composable
internal fun BoxScope.ThemeToggle(
    darkMode: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp)
    ) {
        Icon(
            if (darkMode) Icons.Default.Star else Icons.Default.Star,
            contentDescription = null,
            tint = if (darkMode) Color.Yellow else Color.Gray
        )
    }
}

@Composable
internal fun DemoUsersCard() {
    val GoldText = Color(0xFFE6D6B8)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "👤 Usuarios Demo:",
                fontWeight = FontWeight.Bold,
                color = GoldText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("📧 demo@anticrono.com", fontSize = 12.sp, color = Color.Gray)
            Text("🔑 123456", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
