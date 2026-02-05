package com.timedead.relojinverso.presentation.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.timedead.relojinverso.ui.theme.RelojinversoTheme

/* ---------- COLORS ---------- */

private val PrimaryRed = Color(0xFFD41111)
private val BackgroundDark = Color(0xFF0A0505)

/* ---------- MESSAGES ---------- */

private val lifeMessages = listOf(
    "Recuerda ese abrazo que no diste...",
    "¿Le dijiste \"te quiero\" a tu familia hoy?",
    "Ese viaje que siempre postergaste...",
    "Las palabras que nunca expresaste...",
    "Ese sueño que dejaste para después...",
    "Los amigos que perdiste por orgullo...",
    "El tiempo que pasaste en pantallas...",
    "Las oportunidades que dejaste pasar..."
)

/* ---------- SCREEN ---------- */

/**
 * Pantalla de FIN
 * Muestra la fecha estimada de fin de vida y detalles relacionados
 */
@Composable
fun FinScreen() {
    var secondsLeft by remember { mutableStateOf(59) }
    var heartRate by remember { mutableStateOf(72) }
    var vitalityPercent by remember { mutableStateOf(1f) }
    var isSimulating by remember { mutableStateOf(false) }
    var currentMessageIndex by remember { mutableStateOf(0) }
    
    // Cambiar mensaje cada 4 segundos
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(4000L)
            currentMessageIndex = (currentMessageIndex + 1) % lifeMessages.size
        }
    }
    
    // Simulación del countdown
    LaunchedEffect(isSimulating) {
        if (isSimulating) {
            while (secondsLeft > 0 && isSimulating) {
                kotlinx.coroutines.delay(1000L)
                secondsLeft--
                // Calcular heartRate y vitalityPercent basado en los segundos restantes
                heartRate = 72 + ((59 - secondsLeft) * 2) // Aumenta progresivamente
                vitalityPercent = secondsLeft / 59f // Disminuye proporcionalmente
            }
            // Si llegó a 0, detener la simulación
            if (secondsLeft <= 0) {
                isSimulating = false
            }
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Box  {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                TopBar()

                Spacer(Modifier.height(12.dp))

                Headline()

                Spacer(Modifier.height(24.dp))

                CountdownCircle(secondsLeft)

                Spacer(Modifier.height(32.dp))

                VitalitySection(
                    heartRate = heartRate,
                    vitalityPercent = vitalityPercent,
                    currentMessage = lifeMessages[currentMessageIndex]
                )

                Spacer(Modifier.height(40.dp))

                Footer(
                    isSimulating = isSimulating,
                    onToggleSimulation = {
                        if (isSimulating) {
                            // Salir de la simulación - reiniciar
                            isSimulating = false
                            secondsLeft = 59
                            heartRate = 72
                            vitalityPercent = 1f
                        } else {
                            // Iniciar simulación
                            isSimulating = true
                        }
                    }
                )
            }

            VignetteOverlay()
        }
    }
   
}


/* ---------- TOP BAR ---------- */

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            "FIN",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )

        Box(
            modifier = Modifier
                .size(8.dp)
                .background(PrimaryRed, CircleShape)
        )
    }
}

/* ---------- HEADLINE ---------- */

@Composable
private fun Headline() {
    Text(
        "SIMULACIÓN EN CURSO: ÚLTIMO MINUTO",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = PrimaryRed,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.sp
    )
}

/* ---------- CIRCULAR TIMER ---------- */

@Composable
private fun CountdownCircle(secondsLeft: Int) {
    Box(
        modifier = Modifier.size(196.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 10f, cap = StrokeCap.Round)
            val radius = size.minDimension / 2 - 12f

            // Background ring
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = radius,
                style = stroke
            )

            // Progress ring
            drawArc(
                color = PrimaryRed,
                startAngle = -90f,
                sweepAngle = (secondsLeft / 60f) * 360f,
                useCenter = false,
                style = stroke
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "00",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
                Text(
                    ":",
                    fontSize = 36.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    secondsLeft.toString().padStart(2, '0'),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                "SEGUNDOS",
                fontSize = 10.sp,
                letterSpacing = 4.sp,
                color = Color.White.copy(alpha = 0.4f)
            )
        }
    }
}

/* ---------- VITALITY ---------- */

@Composable
private fun VitalitySection(
    heartRate: Int,
    vitalityPercent: Float,
    currentMessage: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    "RITMO CARDÍACO",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 2.sp
                )
                Text(
                    "$heartRate ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "BPM",
                    fontSize = 10.sp,
                    color = PrimaryRed
                )
            }

            Text(
                "CRÍTICO",
                color = PrimaryRed,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = vitalityPercent,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = PrimaryRed,
            trackColor = Color.White.copy(alpha = 0.1f)
        )

        Spacer(Modifier.height(16.dp))

        AnimatedContent(
            targetState = currentMessage,
            transitionSpec = {
                fadeIn(animationSpec = tween(800)) togetherWith 
                fadeOut(animationSpec = tween(800))
            },
            label = "messageAnimation"
        ) { message ->
            Text(
                message,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

/* ---------- FOOTER ---------- */

@Composable
private fun Footer(
    isSimulating: Boolean,
    onToggleSimulation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedButton(
            onClick = onToggleSimulation,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isSimulating) PrimaryRed else Color.White
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(
                    if (isSimulating) PrimaryRed else Color.White.copy(alpha = 0.5f)
                )
            )
        ) {
            Icon(
                if (isSimulating) Icons.Default.Close else Icons.Default.Favorite,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (isSimulating) "Salir de la simulación" else "Inicio de simulación",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "MODO EXPERIMENTAL LONGETIVITY APP v2.4",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.2f),
            letterSpacing = 3.sp
        )
    }
}

/* ---------- VIGNETTE ---------- */

@Composable
private fun VignetteOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black.copy(alpha = 0.6f)
            )
            .alpha(0.6f)
    )
}

@Preview(showBackground = true)
@Composable
fun FinPreview() {
    RelojinversoTheme {
        FinScreen()
    }
}