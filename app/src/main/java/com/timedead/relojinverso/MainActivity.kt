package com.timedead.relojinverso

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.timedead.relojinverso.ui.theme.RelojinversoTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.Calendar
import android.app.DatePickerDialog
import android.content.Intent
import androidx.activity.result.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.coroutineScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.timedead.relojinverso.data.TimeLeft
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.tasks.OnSuccessListener
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.core.content.ContextCompat.getSystemService
import com.timedead.relojinverso.data.UserPrefsData
import java.time.LocalTime
import java.time.Period
import kotlin.math.ceil
import kotlin.math.min
import kotlin.text.contains

// Define constantes para las claves de SharedPreferences
private const val PREFS_NAME = "DeathTimerPrefs"
private const val KEY_SELECTED_DATE = "selectedDate"
private const val KEY_SELECTED_COUNTRY = "selectedCountry"

private const val KEY_SELECTED_DREAM = "selectedDream"
private const val KEY_SELECTED_PRODUCTIVE = "selectedProductive"

class MainActivity : ComponentActivity() {
    private val screenTimeMillisState = mutableStateOf(0L)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Verifica si el permiso está concedido
        if (!hasUsageStatsPermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            val screenTime = getScreenTime()
            screenTimeMillisState.value = screenTime
        }

        setContent {
            RelojinversoTheme {

                DeathTimerScreen(screenTimeMillis = screenTimeMillisState.value)

            }
        }
    }


    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            "android:get_usage_stats",
            android.os.Process.myUid(),
            packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }

    private fun getScreenTime(): Long {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000L * 60 * 60 * 24 // Últimas 24 horas

        val stats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        val screenTimeMillis = stats.firstOrNull { it.packageName == packageName }?.totalTimeInForeground ?: 0L

        Log.d("WellbeingAPI", "Tiempo en pantalla (esta app): $screenTimeMillis ms")

        return screenTimeMillis
    }

}

data class UsoCategorias(
    val ocioMillis: Long,
    val productividadMillis: Long
)
@Composable
fun DeathTimerScreen(screenTimeMillis: Long) {

    val context = LocalContext.current
    val achievedTdcrHours = 12f // This seems like an example, 25 hours achieved out of 5 potential?
    // Let's assume you meant achieved is *part* of potential for this formatting example.
    // Or, if achievedTdcrHours can be > potential, the meaning of "progress" changes.

    val coroutineScope =
        rememberCoroutineScope() // Para operaciones asíncronas de SharedPreferences
    val gson = remember { Gson() } // Instancia de Gson
    var refreshTrigger by remember { mutableStateOf(0) }


    val formattedTdcrTime = formatHoursToHHmm(achievedTdcrHours)
    //val achivedTpaHours = getEstimatedScreenOnTimeToday(context)
    //val formattedTpaTime = formatHoursToHHmm(achivedTpaHours)
    val currentAchievedHoursForProgress = 3.5f
    val currentPotentialHoursForProgress = 16f // e.g., 24h day - 8h sleep

    val tdcrProgress = if (currentPotentialHoursForProgress > 0) {
        (currentAchievedHoursForProgress / currentPotentialHoursForProgress).coerceIn(0f, 1f)
    } else {
        0f
    }

    val ocioApps = setOf(
        "com.zhiliaoapp.musically", // TikTok
        "com.instagram.android",
        "com.facebook.katana",
        "com.google.android.youtube"
    )

    val productividadApps = setOf(
        "com.whatsapp",
        "com.trello",
        "com.google.android.gm"
    )

    fun getUsoPorCategoria(context: Context): UsoCategorias {
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

        var ocioTime = 0L
        var productividadTime = 0L

        stats?.forEach { usage ->
            when {
                ocioApps.contains(usage.packageName) -> ocioTime += usage.totalTimeInForeground
                productividadApps.contains(usage.packageName) -> productividadTime += usage.totalTimeInForeground
            }
        }

        return UsoCategorias(ocioTime, productividadTime)
    }

    var pulseTarget by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) { // Se ejecuta una vez cuando el composable entra en la composición
        while (true) {
            pulseTarget = 1.1f // O el valor que desees para la expansión
            delay(300L) // Duración de la expansión (ajusta según la duración de tu tween)
            pulseTarget = 1f   // Volver al tamaño original
            delay(700L) // Esperar hasta completar el segundo (1000ms - 300ms)
        }
    }

    // Escala animada sincronizada con el temporizador
    val scale by animateFloatAsState(
        targetValue = pulseTarget,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "scaleAnimation"
    )

    // Función para guardar los datos en SharedPreferences
    fun saveDataToPrefs(date: LocalDate, country: CountryLifeExpectancy?, dream: Int, productive: Int) {
        coroutineScope.launch { // Usa un CoroutineScope para operaciones de I/O
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(KEY_SELECTED_DATE, date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                val countryJson = country?.let { gson.toJson(it) }
                putString(KEY_SELECTED_COUNTRY, countryJson)
                putInt(KEY_SELECTED_DREAM, dream)
                putInt(KEY_SELECTED_PRODUCTIVE, productive)
                apply() // apply() es asíncrono y más eficiente que commit()
            }
        }
    }

    // Función para cargar los datos desde SharedPreferences
    fun loadDataFromPrefs(): UserPrefsData {
        Log.i("loadDataFromPrefs", "Cargando datos desde SharedPreferences")
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dateString = sharedPreferences.getString(KEY_SELECTED_DATE, null)
        val countryJson = sharedPreferences.getString(KEY_SELECTED_COUNTRY, null)
        val dreamInt = sharedPreferences.getInt(KEY_SELECTED_DREAM, 0)
        val productive = sharedPreferences.getInt(KEY_SELECTED_PRODUCTIVE, 0)

        val loadedDate = dateString?.let {
            try {
                LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
            } catch (e: Exception) {
                LocalDate.now() // Valor predeterminado si hay error al parsear
            }
        } ?: LocalDate.now() // Valor predeterminado si no hay fecha guardada

        val loadedCountry = countryJson?.let {
            try {
                gson.fromJson(it, CountryLifeExpectancy::class.java)
            } catch (e: Exception) {
                null // Valor predeterminado si hay error al parsear
            }
        }
        return UserPrefsData(loadedDate, loadedCountry, dreamInt, productive)
    }

    var showDialog by remember { mutableStateOf(false) }

    // Cargar datos al iniciar o usar valores predeterminados
    var selectedDate by remember {
        mutableStateOf(loadDataFromPrefs().date)
    }
    var nullableSelectedCountry by remember {
        mutableStateOf(loadDataFromPrefs().country)
    }

    var selectdreams by remember {
        mutableStateOf(loadDataFromPrefs().sleepHours)
    }

    var selectProductive by remember {
        mutableStateOf(loadDataFromPrefs().productiveHours)
    }

    LaunchedEffect(refreshTrigger) {
        val loadedPrefs = loadDataFromPrefs()
        selectedDate = loadedPrefs.date
        nullableSelectedCountry = loadedPrefs.country
        selectdreams = loadedPrefs.sleepHours
        selectProductive = loadedPrefs.productiveHours
    }

    // Si no hay país seleccionado después de cargar, muestra el diálogo
    // Esto es útil si quieres que el usuario siempre seleccione la primera vez.
    LaunchedEffect(nullableSelectedCountry) {
        if (nullableSelectedCountry == null) {
            showDialog = true
        }
    }


    val diasRestantes = calcularDiasRestantes(
        fechaNacimiento = selectedDate,
        esperanzaVidaAnios = nullableSelectedCountry?.lifeExpectancyBoth?.toLong() ?: 80L
    )

    BirthCountryDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onSave = { birthDate, country, hours, timeStart, timeEnd ->
            selectedDate = birthDate
            nullableSelectedCountry = country
            val productiveTimeHours = timeEnd.hour - timeStart.hour
            saveDataToPrefs(birthDate, country, hours, productiveTimeHours) // Guardar los datos aquí
            showDialog = false
            refreshTrigger++ // Fuerza una recomposición y recarga de datos
        }
    )


    // Variable para almacenar el estado completo del tiempo restante, incluyendo segundos
    val timeLeftState = remember { mutableStateOf(calculateTimeLeft(LocalDateTime.now().plusYears(1))) } // Valor inicial dummy

    // LifeCountdown ahora también actualiza timeLeftState que incluye segundos
    // y CountdownTimer usará los segundos de timeLeftState.
    // Necesitamos un estado que LifeCountdown actualice y CountdownTimer pueda leer.

    val lifeExpectancyYears = nullableSelectedCountry?.lifeExpectancyBoth?.toLong() ?: 80L
    val deathDateTime = remember(selectedDate, lifeExpectancyYears) {
        selectedDate.atStartOfDay().plusYears(lifeExpectancyYears)
    }


    LaunchedEffect(deathDateTime) {
        while (true) {
            // 1. Actualizar el estado del tiempo restante
            timeLeftState.value = calculateTimeLeft(deathDateTime)

            // 2. Iniciar la animación de pulso
            pulseTarget = 1.1f // Expansión
            // No necesitamos un delay aquí para la expansión,
            // porque `animateFloatAsState` se encargará de la duración de la animación.

            // 3. Esperar a que la animación de expansión termine y luego contraer
            // La animación de expansión dura 300ms (definido en `tween`)
            delay(300L) // Esperar a que la expansión se complete (o casi)
            pulseTarget = 1f   // Contracción (volver al tamaño original)

            // 4. Esperar el resto del segundo antes de la próxima actualización y pulso
            // La animación de contracción también dura 300ms.
            // Total esperado para el pulso: 300ms (expansión) + 300ms (contracción con animateFloatAsState)
            // El delay principal es de 1000ms.
            // Necesitamos esperar: 1000ms (total) - 300ms (expansión ya esperada) = 700ms
            // La contracción ocurrirá durante estos 700ms.
            delay(700L)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(horizontal = 10.dp, vertical = 30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Text(
                text = "MORIRÁS EN",
                color = Color(0xFFE6D6B8),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Time units
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
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(60.dp)
                    )
                }
            }

            // Numbers
            // Muestra Años, Meses, ..., Minutos
            LifeCountdownDisplay(timeLeft = timeLeftState.value)
            // Heart and circles container
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Heart image - replace with actual image resource
                Image(
                    painter = painterResource(id = obtenerDrawablePorHoras(screenTimeMillis)),
                    contentDescription = "Beating Heart",
                    modifier = Modifier
                        .size(230.dp)
                        .offset(x = 10.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                )

                // Center circle with 58
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0A0A0A))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    // El CountdownTimer ahora toma los segundos de timeLeftState
                    CountdownTimerDisplay(seconds = timeLeftState.value.seconds)
                }

                // Left circles (green)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = (-40).dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    CircularProgressWithTextSync(
                        color = Color.Green,
                       // averageSleepHours = (selectdreams + screenTimeMillis.toDouble() / 3_600_000.0).toInt(),
                       averageSleepHours = 0,
                        acronym = "RI"
                    )

                    CircularProgressWithTextSync(
                        color = Color.Green,
                        averageSleepHours = (selectdreams + getTotalUsageToday(context).toDouble() / 3_600_000.0).toInt(),
                        acronym = "TDCR"
                    )

                }

                // Right circles (red)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    CircularProgressWithText(
                        progress = 0.5f,          // 50% completado
                        color = Color.Green,      // Color del progreso
                        time = formatTime(getTotalUsageToday(context)),              // Texto principal
                        acronym = "TPA",          // Texto secundario
                        reverse = false           // Si quieres que vaya al revés
                    )

                    CircularProgressWithTextSync(
                        color = Color.Red,
                        averageSleepHours = selectProductive,
                        acronym = "TPR"
                    )
                }
            }

            // MI VALOR ES
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
                    text = "$diasRestantes DÍAS",
                    color = Color(0xFFE6D6B8),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            if (selectedDate != null && nullableSelectedCountry?.lifeExpectancyBoth != null) {
                val edad = calcularEdad(selectedDate) // selectedDate debe ser tipo LocalDate
                val expectativaVida = nullableSelectedCountry?.lifeExpectancyBoth?.toInt() ?: 0
                val restante = (expectativaVida - edad).coerceAtLeast(0)

                val uso = getUsoPorCategoria(context)
                Log.i("UsoPorCategoria", "Uso: $uso")
                val (ocioPct, prodPct) = calcularPorcentaje(uso.ocioMillis, uso.productividadMillis)

                BarraProgresoOcio(ocioPct, prodPct)

                // ETAPA: ADULTEZ
                Text(
                    text = "ETAPA: ${obtenerEtapaDeVida(selectedDate)}",
                    color = Color(0xFFE6D6B8),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Dots
                DotsProgressMultilineNative(
                    redCount = edad,
                    greenCount = restante
                )

                // Percentages
                PorcentajeVidaTextos(selectedDate, nullableSelectedCountry?.lifeExpectancyBoth?.toInt() ?: 0)
            }



        }
    }
}

fun calcularEdad(fechaNacimiento: LocalDate): Int {
    val hoy = LocalDate.now()
    return Period.between(fechaNacimiento, hoy).years
}

fun calcularPorcentaje(ocioMillis: Long, productividadMillis: Long): Pair<Float, Float> {
    val total = ocioMillis + productividadMillis
    if (total == 0L) return Pair(0f, 0f)

    val ocioPorcentaje = (ocioMillis.toFloat() / total) * 100
    val prodPorcentaje = (productividadMillis.toFloat() / total) * 100
    return Pair(ocioPorcentaje, prodPorcentaje)
}

@DrawableRes
fun obtenerDrawablePorHoras(screenTimeMillis: Long): Int {
    val horas = screenTimeMillis / 3_600_000.0 // convertir ms a horas

    return when {
        horas < 3 -> R.drawable.heardredintensity
        horas < 5 -> R.drawable.heartorange
        else -> R.drawable.heartyellow
    }
}
fun calcularPorcentajeVidaVivida(fechaNacimiento: LocalDate, expectativaDeVidaAnios: Int): Float {
    val hoy = LocalDate.now()
    val diasVividos = ChronoUnit.DAYS.between(fechaNacimiento, hoy).toFloat()
    val diasEsperados = expectativaDeVidaAnios * 365.25f // considerar años bisiestos

    return (diasVividos / diasEsperados).coerceIn(0f, 1f) // para evitar sobrepasar el 100%
}

fun obtenerEtapaDeVida(fechaNacimiento: LocalDate): String {
    val hoy = LocalDate.now()
    val edad = Period.between(fechaNacimiento, hoy).years

    return when {
        edad < 0 -> "Fecha inválida"
        edad <= 11 -> "Infancia"
        edad in 12..17 -> "Adolescencia"
        edad in 18..25 -> "Juventud"
        edad in 26..40 -> "Adultez Temprana"
        edad in 41..60 -> "Adultez Media"
        else -> "Adultez Mayor / Vejez"
    }
}
@Composable
fun DotsProgressMultilineNative(
    redCount: Int,
    greenCount: Int,
    dotsPerRow: Int = 10 // ajustable
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Rojas en la mitad izquierda
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            drawDotsInRows(redCount, dotsPerRow, Color(0xFFB71C1C))
        }

        // Verdes en la mitad derecha
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            drawDotsInRows(greenCount, dotsPerRow, Color(0xFF2E7D32))
        }
    }
}

@Composable
private fun drawDotsInRows(count: Int, perRow: Int, color: Color) {
    val numRows = ceil(count / perRow.toFloat()).toInt()
    var remaining = count

    repeat(numRows) {
        val dotsThisRow = min(perRow, remaining)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            repeat(dotsThisRow) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
        remaining -= dotsThisRow
    }
}

@Composable
fun PorcentajeVidaTextos(
    fechaNacimiento: LocalDate,
    expectativaDeVidaAnios: Int
) {
    val porcentajeVivido = calcularPorcentajeVidaVivida(fechaNacimiento, expectativaDeVidaAnios)
    val porcentajeVividoRedondeado = (porcentajeVivido * 100).toInt()
    val porcentajeRestante = 100 - porcentajeVividoRedondeado

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$porcentajeVividoRedondeado%",
            color = Color(0xFFB71C1C),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$porcentajeRestante%",
            color = Color(0xFF2E7D32),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun BarraProgresoVida(
    fechaNacimiento: LocalDate,
    expectativaDeVidaAnios: Int
) {
    val porcentajeVivido = calcularPorcentajeVidaVivida(fechaNacimiento, expectativaDeVidaAnios)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0A0A0A))
            .padding(horizontal = 32.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Parte vivida
            Box(
                modifier = Modifier
                    .weight(porcentajeVivido)
                    .fillMaxHeight()
                    .background(Color(0xFFB71C1C)) // Rojo
            )
            // Parte restante
            Box(
                modifier = Modifier
                    .weight(1f - porcentajeVivido)
                    .fillMaxHeight()
                    .background(Color(0xFF2E7D32)) // Verde
            )
        }
    }
}


@Composable
fun BarraProgresoOcio(
    porcentajeOcio: Float,
    porcentajeProd: Float
) {
    val ocioWeight = if (porcentajeOcio > 0) porcentajeOcio else 0.0001f
    val prodWeight = if (porcentajeProd > 0) porcentajeProd else 0.0001f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0A0A0A))
            .padding(horizontal = 32.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Parte vivida
            Box(
                modifier = Modifier
                    .weight(ocioWeight)
                    .fillMaxHeight()
                    .background(Color(0xFFB71C1C)) // Rojo
            )
            // Parte restante
            Box(
                modifier = Modifier
                    .weight(prodWeight)
                    .fillMaxHeight()
                    .background(Color(0xFF2E7D32)) // Verde
            )
        }
    }
}

// Renombrado para claridad, solo muestra los segundos que se le pasan
@Composable
fun CountdownTimerDisplay(seconds: Long) {
    // Círculo central con el contador de segundos
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFF0A0A0A)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            // Muestra los segundos que se le pasan, invertidos para cuenta regresiva
            // Por ejemplo, si el segundo actual es 2, muestra 60-2 = 58 (si quieres que cuente hacia abajo desde 60)
            // O simplemente muestra el segundo actual.
            // Para el comportamiento de cuenta regresiva de 60 a 0 dentro del minuto actual:
            text = (59 - (seconds % 60)).toString().padStart(2, '0'),
            color = Color(0xFFE6D6B8),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LifeCountdownDisplay(timeLeft: TimeLeft) {
    val (years, months, weeks, days, hours, minutes) = timeLeft // No necesitamos segundos aquí para mostrar

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        listOf(years, months, weeks, days, hours, minutes).forEach { number ->
            Text(
                text = number.toString().padStart(2, '0'),
                color = Color(0xFFE6D6B8),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CountdownTimer() {
    var countdownValue by remember { mutableStateOf(60) }
    var isActive by remember { mutableStateOf(true) }

    // Efecto para el contador regresivo
    LaunchedEffect(isActive) {
        while (true) { // Loop indefinitely
            if (countdownValue > 0) {
                delay(1000L) // Espera 1 segundo
                countdownValue--
            } else {
                // Reset the counter when it reaches zero
                countdownValue = 60
                // You could add a small delay here if you want a pause before it restarts
                // delay(500L)
            }
        }
    }

    // Círculo central con el contador
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFF0A0A0A)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countdownValue.toString(),
            color = Color(0xFFE6D6B8),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CircularProgressWithTextSync(
    color: Color,
    averageSleepHours: Int,
    acronym: String,
    reverse: Boolean = false
) {
    val totalAwakeSeconds = (24 - averageSleepHours) * 3600

    // Estado para tiempo restante
    var secondsRemaining by remember { mutableStateOf(totalAwakeSeconds) }

    LaunchedEffect(Unit) {
        while(true) {
            val now = LocalTime.now()
            val secondsSinceMidnight = now.toSecondOfDay()

            val sleepStartSeconds = 24 * 3600 - averageSleepHours * 3600

            secondsRemaining = if (secondsSinceMidnight <= sleepStartSeconds) {
                sleepStartSeconds - secondsSinceMidnight
            } else {
                0 // o totalAwakeSeconds para reiniciar ciclo
            }

            delay(1000L)
        }
    }

    val progress = secondsRemaining.toFloat() / totalAwakeSeconds.toFloat()

    // Formatear tiempo restante en hh:mm
    val minutes = (secondsRemaining / 60) % 60
    val hours = (secondsRemaining / 3600)

    val formattedTime = "%02d:%02d".format(hours, minutes)

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(80.dp)) {
            drawCircle(
                color = color.copy(alpha = 0.3f),
                radius = size.minDimension / 2 - 4.dp.toPx(),
                style = Stroke(width = 4.dp.toPx())
            )
        }

        androidx.compose.foundation.Canvas(modifier = Modifier.size(80.dp)) {
            val sweepAngle = 360f * progress
            val startAngle = if (reverse) 270f else -90f
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = if (reverse) -sweepAngle else sweepAngle,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formattedTime,
                color = Color(0xFFE6D6B8),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = acronym,
                color = Color(0xFFE6D6B8),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun CircularProgressWithText(
    progress: Float,
    color: Color,
    time: String,
    acronym: String,
    reverse: Boolean = false
) {
    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background circle
        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(80.dp)
        ) {
            drawCircle(
                color = color.copy(alpha = 0.3f),
                radius = size.minDimension / 2 - 4.dp.toPx(),
                style = Stroke(width = 4.dp.toPx())
            )
        }

        // Progress circle
        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(80.dp)
        ) {
            val sweepAngle = 360f * progress
            val startAngle = if (reverse) 270f else -90f

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = if (reverse) -sweepAngle else sweepAngle,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Text content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time,
                color = Color(0xFFE6D6B8),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = acronym,
                color = Color(0xFFE6D6B8),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun BirthCountryDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (LocalDate, CountryLifeExpectancy, Int, LocalTime, LocalTime) -> Unit // Se agregaron los rangos de hora
) {
    val gson = Gson()
    val listType = object : TypeToken<List<CountryLifeExpectancy>>() {}.type
    val countries: List<CountryLifeExpectancy> = gson.fromJson(jsonLifeExpectancyList, listType)
    val sortedCountries = countries.sortedBy { it.Country }

    if (showDialog) {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        var showDatePicker by remember { mutableStateOf(false) }

        var expandedCountry by remember { mutableStateOf(false) }
        var selectedCountry by remember { mutableStateOf(sortedCountries.first()) }

        val sleepOptions = listOf(8, 6, 4)
        var expandedSleep by remember { mutableStateOf(false) }
        var selectedSleepHours by remember { mutableStateOf(sleepOptions.first()) }

        var selectedStartTime by remember { mutableStateOf(LocalTime.of(22, 0)) }
        var selectedEndTime by remember { mutableStateOf(LocalTime.of(6, 0)) }

        var showStartTimePicker by remember { mutableStateOf(false) }
        var showEndTimePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Datos de Residencia") },
            text = {
                Column {
                    OutlinedButton(onClick = { showDatePicker = true }) {
                        Text("Fecha: $selectedDate")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // País
                    Box {
                        OutlinedButton(onClick = { expandedCountry = true }) {
                            Text("País: ${selectedCountry.Country}")
                        }
                        DropdownMenu(
                            expanded = expandedCountry,
                            onDismissRequest = { expandedCountry = false }
                        ) {
                            sortedCountries.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text(country.Country) },
                                    onClick = {
                                        selectedCountry = country
                                        expandedCountry = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Horas de sueño
                    Box {
                        OutlinedButton(onClick = { expandedSleep = true }) {
                            Text("Horas de sueño: $selectedSleepHours")
                        }
                        DropdownMenu(
                            expanded = expandedSleep,
                            onDismissRequest = { expandedSleep = false }
                        ) {
                            sleepOptions.forEach { hours ->
                                DropdownMenuItem(
                                    text = { Text("$hours horas") },
                                    onClick = {
                                        selectedSleepHours = hours
                                        expandedSleep = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Horas de productivas")
                    // Hora inicio
                    OutlinedButton(onClick = { showStartTimePicker = true }) {
                        Text("Hora de inicio: ${selectedStartTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                    }

                    // Hora fin
                    OutlinedButton(onClick = { showEndTimePicker = true }) {
                        Text("Hora de fin: ${selectedEndTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                    }

                    if (showDatePicker) {
                        DatePickerDialogComposable(
                            initialDate = selectedDate,
                            onDateSelected = {
                                selectedDate = it
                                showDatePicker = false
                            },
                            onDismissRequest = { showDatePicker = false }
                        )
                    }

                    // Picker para hora inicio
                    if (showStartTimePicker) {
                        TimePickerDialog(
                            onTimeSelected = {
                                selectedStartTime = it
                                showStartTimePicker = false
                            },
                            onDismissRequest = { showStartTimePicker = false }
                        )
                    }

                    // Picker para hora fin
                    if (showEndTimePicker) {
                        TimePickerDialog(
                            onTimeSelected = {
                                selectedEndTime = it
                                showEndTimePicker = false
                            },
                            onDismissRequest = { showEndTimePicker = false }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onSave(selectedDate, selectedCountry, selectedSleepHours, selectedStartTime, selectedEndTime)
                    onDismiss()
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun TimePickerDialog(
    onTimeSelected: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val timePickerDialog = remember {
        val calendar = Calendar.getInstance()
        android.app.TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                onTimeSelected(LocalTime.of(hour, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    // Mostrar el diálogo solo una vez
    LaunchedEffect(Unit) {
        timePickerDialog.setOnCancelListener {
            onDismissRequest()
        }
        timePickerDialog.show()
    }
}


@Composable
fun LifeCountdown(birthDate: LocalDate, lifeExpectancyYears: Long) {
    val deathDateTime = remember(birthDate, lifeExpectancyYears) {
        birthDate.atStartOfDay().plusYears(lifeExpectancyYears)
    }

    val timeLeft = remember { mutableStateOf(calculateTimeLeft(deathDateTime)) }

    LaunchedEffect(deathDateTime) {
        while (true) {
            timeLeft.value = calculateTimeLeft(deathDateTime)
            // Considera si necesitas loguear esto con tanta frecuencia
            // Log.i("Countdown", "Time left: ${timeLeft.value}")
            delay(1000L) // Se actualiza cada segundo, lo cual es apropiado para ver minutos
        }
    }

    // Desestructura incluyendo los minutos
    val (years, months, weeks, days, hours, minutes) = timeLeft.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp), // Ajusta el padding si es necesario
        horizontalArrangement = Arrangement.SpaceAround // SpaceAround puede ser mejor para más elementos
    ) {
        // Asegúrate de que el orden aquí coincida con el de las etiquetas
        listOf(years, months, weeks, days, hours, minutes).forEach { number ->
            Text(
                text = number.toString().padStart(2, '0'), // padStart para mostrar siempre dos dígitos
                color = Color(0xFFE6D6B8),
                fontSize = 24.sp, // Podrías necesitar ajustar el tamaño si el espacio es limitado
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f) // Usar weight ayuda a distribuir el espacio
            )
        }
    }
}



fun calculateTimeLeft(deathDateTime: LocalDateTime): TimeLeft {
    val now = LocalDateTime.now()
    if (now.isAfter(deathDateTime)) {
        return TimeLeft(0, 0, 0, 0, 0, 0, 0) // Incluye segundos
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

    val totalSeconds = duration.seconds // Obtenemos el total de segundos de la duración
    val seconds = 59 - (totalSeconds % 60)

    return TimeLeft(years, months, weeks, days, hours, minutes, seconds)
}
// Dialogo de selección de fecha usando el DatePicker clásico de Android
@Composable
fun DatePickerDialogComposable(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val year = initialDate.year
    val month = initialDate.monthValue - 1
    val day = initialDate.dayOfMonth

    LaunchedEffect(Unit) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                onDateSelected(LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth))
            },
            year, month, day
        )
        datePickerDialog.setOnDismissListener { onDismissRequest() }
        datePickerDialog.show()
    }
}

fun getTotalUsageToday(context: Context): Long {
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

    var totalTime: Long = 0
    stats?.forEach { usage ->
        totalTime += usage.totalTimeInForeground
    }

    return totalTime
}

fun formatHoursToHHmm(hours: Float): String {
    val totalMinutes = (hours * 60).toInt()
    val displayHours = totalMinutes / 60
    val displayMinutes = totalMinutes % 60
    return String.format("%02d:%02d", displayHours, displayMinutes)
}

fun calcularDiasRestantes(
    fechaNacimiento: LocalDate,
    esperanzaVidaAnios: Long
): Long {
    val fechaMuerteEstimada = fechaNacimiento.plusYears(esperanzaVidaAnios)
    val hoy = LocalDate.now()

    return ChronoUnit.DAYS.between(hoy, fechaMuerteEstimada).coerceAtLeast(0)
}

// Put hasUsageStatsPermission inside the Activity or pass context appropriately
private fun hasUsageStatsPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
        return true // Not needed for older versions
    }
    val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    // MODE_ALLOWED is AppOpsManager.MODE_ALLOWED
    val mode = appOpsManager.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    val granted = mode == AppOpsManager.MODE_ALLOWED

    // More robust check: also try to query stats for a very short recent interval
    if (granted) {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            currentTime - (1000 * 60), // 1 minute ago
            currentTime
        )
        return stats != null && stats.isNotEmpty()
    }
    return false
}

fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return "${hours}:${minutes % 60}"
}



data class CountryLifeExpectancy(
    val Rank: Int,
    val Country: String,
    @SerializedName("Life Expectancy (both sexes)")
    val lifeExpectancyBoth: Double,
    @SerializedName("Females Life Expectancy")
    val femaleLifeExpectancy: Double,
    @SerializedName("Males Life Expectancy")
    val maleLifeExpectancy: Double
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RelojinversoTheme {
        DeathTimerScreen(screenTimeMillis = 0L)
    }
}