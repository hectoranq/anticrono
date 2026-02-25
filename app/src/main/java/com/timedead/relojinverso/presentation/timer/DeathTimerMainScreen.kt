package com.timedead.relojinverso.presentation.timer

import android.app.AppOpsManager
import android.content.Context
import android.app.usage.UsageStatsManager
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timedead.relojinverso.data.TimeLeft
import com.timedead.relojinverso.domain.model.CountryLifeExpectancy
import com.timedead.relojinverso.domain.model.User
import com.timedead.relojinverso.domain.model.UserProfile
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration
import java.time.temporal.ChronoUnit
import com.google.gson.reflect.TypeToken
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.timedead.relojinverso.R
import com.timedead.relojinverso.data.UserPrefsData
import com.timedead.relojinverso.jsonLifeExpectancyList
import com.timedead.relojinverso.ui.theme.RelojinversoTheme
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.ceil
import kotlin.math.min
import com.timedead.relojinverso.domain.repository.AuthRepository
import com.timedead.relojinverso.data.DeathTimerData
import com.timedead.relojinverso.data.repository.DeathTimerDataRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
/**
 * Pantalla principal del Death Timer
 * Muestra el contador regresivo de vida del usuario
 */

private const val PREFS_NAME = "DeathTimerPrefs"
private const val KEY_SELECTED_DATE = "selectedDate"
private const val KEY_SELECTED_COUNTRY = "selectedCountry"

private const val KEY_SELECTED_DREAM = "selectedDream"
private const val KEY_SELECTED_PRODUCTIVE = "selectedProductive"

data class UsoCategorias(
    val ocioMillis: Long,
    val productividadMillis: Long
)

data class AppCategoriesConfig(
    @SerializedName("ocioApps")
    val ocioApps: List<String> = emptyList(),
    @SerializedName("productividadApps")
    val productividadApps: List<String> = emptyList()
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeathTimerMainScreen(
    authRepository: AuthRepository,
    screenTimeMillis: Long = 0L
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val gson = remember { Gson() }
    
    // Repository para guardar datos calculados
    val dataRepository = remember { DeathTimerDataRepository(context) }

    // Estado para almacenar el perfil del usuario desde Firebase
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoadingProfile by remember { mutableStateOf(true) }
    var profileError by remember { mutableStateOf<String?>(null) }

    // Función para cargar desde SharedPreferences (fallback)
    val loadDataFromPrefs: () -> UserPrefsData = remember(gson) {
        {
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
                    LocalDate.now()
                }
            } ?: LocalDate.now()

            val loadedCountry = countryJson?.let {
                try {
                    gson.fromJson(it, CountryLifeExpectancy::class.java)
                } catch (e: Exception) {
                    null
                }
            }
            UserPrefsData(loadedDate, loadedCountry, dreamInt, productive)
        }
    }

    // Estados mutables para las categorías de apps
    var ocioApps by remember { 
        mutableStateOf(setOf(
            "com.zhiliaoapp.musically", // TikTok
            "com.instagram.android",
            "com.facebook.katana",
            "com.google.android.youtube"
        ))
    }

    var productividadApps by remember { 
        mutableStateOf(setOf(
            "com.whatsapp",
            "com.trello",
            "com.google.android.gm"
        ))
    }

    // Función para parsear y actualizar las categorías de apps desde JSON
    fun parseApps(json: String) {
        try {
            val config = gson.fromJson(json, AppCategoriesConfig::class.java)
            ocioApps = config.ocioApps.toSet()
            productividadApps = config.productividadApps.toSet()
            Log.i("DeathTimerMainScreen", "Categorías actualizadas desde Remote Config")
            Log.i("DeathTimerMainScreen", "Ocio: $ocioApps")
            Log.i("DeathTimerMainScreen", "Productividad: $productividadApps")
        } catch (e: Exception) {
            Log.e("DeathTimerMainScreen", "Error al parsear app_categories: ${e.message}")
        }
    }

    // Obtener configuración de Firebase Remote Config
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val remoteConfig = FirebaseRemoteConfig.getInstance()
                val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600) // 1 hora
                    .build()
                remoteConfig.setConfigSettingsAsync(configSettings)

                // Establecer valores por defecto
                val defaults = hashMapOf<String, Any>(
                    "app_categories" to """{
                        "ocioApps": [
                            "com.zhiliaoapp.musically",
                            "com.instagram.android",
                            "com.facebook.katana",
                            "com.google.android.youtube"
                        ],
                        "productividadApps": [
                            "com.whatsapp",
                            "com.trello",
                            "com.google.android.gm"
                        ]
                    }"""
                )
                remoteConfig.setDefaultsAsync(defaults)

                // Fetch y activar
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val json = remoteConfig.getString("app_categories")
                            if (json.isNotEmpty()) {
                                parseApps(json)
                            }
                            Log.i("DeathTimerMainScreen", "Remote Config actualizado correctamente")
                        } else {
                            Log.e("DeathTimerMainScreen", "Error al obtener Remote Config")
                        }
                    }
            } catch (e: Exception) {
                Log.e("DeathTimerMainScreen", "Error al inicializar Remote Config: ${e.message}")
            }
        }
    }

    // Cargar perfil del usuario desde Firebase
    LaunchedEffect(Unit) {
        scope.launch {
            isLoadingProfile = true
            val result = authRepository.getUserProfile()
            result.fold(
                onSuccess = { profile ->
                    userProfile = profile
                    isLoadingProfile = false
                    profileError = null
                    Log.i("DeathTimerMainScreen", "Perfil cargado desde Firebase: $profile")
                },
                onFailure = { error ->
                    profileError = error.message
                    isLoadingProfile = false
                    Log.e("DeathTimerMainScreen", "Error al cargar perfil: ${error.message}")
                    // Fallback a SharedPreferences si falla Firebase
                    val loadedPrefs = loadDataFromPrefs.invoke()
                    userProfile = UserProfile(
                        birthDate = loadedPrefs.date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                        birthCountry = loadedPrefs.country?.Country ?: "",
                        sleepHours = loadedPrefs.sleepHours,
                        tprStart = "",
                        tprEnd = ""
                    )
                }
            )
        }
    }

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

    // Cargar datos desde el perfil de Firebase o usar valores predeterminados
    val selectedDate = remember(userProfile) {
        userProfile?.birthDate?.let {
            try {
                LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
            } catch (e: Exception) {
                Log.e("DeathTimerMainScreen", "Error parsing birthDate: ${e.message}")
                LocalDate.now()
            }
        } ?: LocalDate.now()
    }
    
    val nullableSelectedCountry = remember(userProfile, gson) {
        userProfile?.birthCountry?.let { countryName ->
            // Buscar el país en la lista de expectativas de vida
            try {
                val type = object : TypeToken<List<CountryLifeExpectancy>>() {}.type
                val countries: List<CountryLifeExpectancy> = gson.fromJson(jsonLifeExpectancyList, type)
                countries.find { it.Country.equals(countryName, ignoreCase = true) }
            } catch (e: Exception) {
                Log.e("DeathTimerMainScreen", "Error finding country: ${e.message}")
                null
            }
        }
    }

    val selectdreams = remember(userProfile) {
        userProfile?.sleepHours ?: 8
    }

    val selectProductive = remember(userProfile) {
        // Calcular horas productivas desde tprStart y tprEnd
        val profile = userProfile
        if (profile != null && profile.tprStart.isNotEmpty() && profile.tprEnd.isNotEmpty()) {
            try {
                val start = LocalTime.parse(profile.tprStart, DateTimeFormatter.ofPattern("HH:mm"))
                val end = LocalTime.parse(profile.tprEnd, DateTimeFormatter.ofPattern("HH:mm"))
                val duration = Duration.between(start, end)
                (duration.toHours()).toInt()
            } catch (e: Exception) {
                Log.e("DeathTimerMainScreen", "Error calculating productive hours: ${e.message}")
                0
            }
        } else {
            0
        }
    }

    // Mostrar indicador de carga mientras se obtiene el perfil
    if (isLoadingProfile) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0A0A)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFE6D6B8))
        }
        return
    }




    val diasRestantes = calcularDiasRestantes(
        fechaNacimiento = selectedDate,
        esperanzaVidaAnios = nullableSelectedCountry?.lifeExpectancyBoth?.toLong() ?: 80L
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
    
    // Guardar datos calculados en SharedPreferences cada 5 segundos
    LaunchedEffect(selectedDate, nullableSelectedCountry, selectdreams, selectProductive, screenTimeMillis) {
        while (true) {
            delay(5000L) // Guardar cada 5 segundos
            
            try {
                val edad = calcularEdad(selectedDate)
                val uso = getUsoPorCategoria(context)
                val (ocioPct, prodPct) = calcularPorcentaje(uso.ocioMillis, uso.productividadMillis)
                val porcentajeVivido = calcularPorcentajeVidaVivida(
                    selectedDate,
                    nullableSelectedCountry?.lifeExpectancyBoth?.toInt() ?: 80
                )
                val screenTimeToday = getTotalUsageToday(context)
                val heartDrawable = obtenerDrawablePorHoras(screenTimeMillis)
                
                val deathTimerData = DeathTimerData(
                    timeLeft = timeLeftState.value,
                    diasRestantes = diasRestantes,
                    edad = edad,
                    etapaDeVida = obtenerEtapaDeVida(selectedDate),
                    porcentajeVidaVivida = porcentajeVivido,
                    porcentajeVidaRestante = 1f - porcentajeVivido,
                    screenTimeMillis = screenTimeMillis,
                    screenTimeToday = screenTimeToday,
                    ocioMillis = uso.ocioMillis,
                    productividadMillis = uso.productividadMillis,
                    ocioPorcentaje = ocioPct,
                    productividadPorcentaje = prodPct,
                    horasSueno = selectdreams,
                    horasProductivas = selectProductive,
                    heartDrawableId = heartDrawable
                )
                
                dataRepository.saveDeathTimerData(deathTimerData)
                Log.i("DeathTimerMainScreen", "Datos guardados en SharedPreferences")
            } catch (e: Exception) {
                Log.e("DeathTimerMainScreen", "Error guardando datos: ${e.message}")
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp, vertical = 30.dp),
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

            if (nullableSelectedCountry != null) {
                val edad = calcularEdad(selectedDate) // selectedDate debe ser tipo LocalDate
                val expectativaVida = nullableSelectedCountry.lifeExpectancyBoth.toInt()
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
                PorcentajeVidaTextos(selectedDate, nullableSelectedCountry.lifeExpectancyBoth.toInt())
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

fun calcularDiasRestantes(
    fechaNacimiento: LocalDate,
    esperanzaVidaAnios: Long
): Long {
    val fechaMuerteEstimada = fechaNacimiento.plusYears(esperanzaVidaAnios)
    val hoy = LocalDate.now()

    return ChronoUnit.DAYS.between(hoy, fechaMuerteEstimada).coerceAtLeast(0)
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
    // Mock repository para el preview
    val mockRepository = object : AuthRepository {
        override suspend fun signIn(email: String, password: String) = Result.failure<User>(Exception("Preview"))
        override suspend fun register(name: String, email: String, password: String, profile: UserProfile) = Result.failure<User>(Exception("Preview"))
        override suspend fun signOut() = Result.failure<Unit>(Exception("Preview"))
        override suspend fun getCurrentUser() = null
        override fun isUserAuthenticated() = kotlinx.coroutines.flow.flowOf(false)
        override suspend fun saveUserProfile(profile: UserProfile) = Result.failure<Unit>(Exception("Preview"))
        override suspend fun getUserProfile() = Result.success(
            UserProfile(
                fullName = "Usuario Demo",
                birthDate = "1990-01-01",
                birthCountry = "Mexico",
                sleepHours = 8,
                tprStart = "09:00",
                tprEnd = "17:00"
            )
        )
    }
    
    RelojinversoTheme {
        DeathTimerMainScreen(
            authRepository = mockRepository,
            screenTimeMillis = 0L
        )
    }
}