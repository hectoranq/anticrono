# Guía de Uso de Datos Compartidos del Death Timer

## Descripción General

Todos los cálculos importantes realizados en `DeathTimerMainScreen` se guardan automáticamente en `SharedPreferences` cada 5 segundos usando `DeathTimerDataRepository`. Estos datos están disponibles para todas las demás pantallas de la aplicación.

## Datos Guardados

### Estructura `DeathTimerData`

```kotlin
data class DeathTimerData(
    // Tiempo restante de vida
    val timeLeft: TimeLeft,           // Años, meses, semanas, días, horas, minutos, segundos
    val diasRestantes: Long,          // Total de días restantes
    
    // Información del usuario
    val edad: Int,                    // Edad actual en años
    val etapaDeVida: String,          // "Infancia", "Adolescencia", "Adultez", etc.
    val porcentajeVidaVivida: Float,  // 0.0 a 1.0
    val porcentajeVidaRestante: Float,// 0.0 a 1.0
    
    // Tiempo de pantalla
    val screenTimeMillis: Long,       // Tiempo total de pantalla (últimas 24h)
    val screenTimeToday: Long,        // Tiempo de pantalla hoy
    
    // Uso por categorías
    val ocioMillis: Long,             // Tiempo en apps de ocio
    val productividadMillis: Long,    // Tiempo en apps productivas
    val ocioPorcentaje: Float,        // Porcentaje de ocio
    val productividadPorcentaje: Float,// Porcentaje de productividad
    
    // Configuración del usuario
    val horasSueno: Int,              // Horas de sueño configuradas
    val horasProductivas: Int,        // Horas productivas configuradas
    
    // Estado visual
    val heartDrawableId: Int,         // ID del drawable del corazón (color según uso)
    
    // Metadata
    val lastUpdated: Long             // Timestamp de última actualización
)
```

## Cómo Usar en Otras Pantallas

### 1. Importar el Repository

```kotlin
import com.timedead.relojinverso.data.repository.DeathTimerDataRepository
```

### 2. Obtener Instancia del Repository

```kotlin
@Composable
fun TuPantalla() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    
    // ... resto del código
}
```

### 3. Leer Datos

#### Opción A: Obtener todos los datos

```kotlin
val savedData = remember { dataRepository.getDeathTimerData() }

// Usar los datos
if (savedData != null) {
    Text("Edad: ${savedData.edad} años")
    Text("Etapa: ${savedData.etapaDeVida}")
    Text("Días restantes: ${savedData.diasRestantes}")
}
```

#### Opción B: Obtener datos específicos

```kotlin
// Solo días restantes
val diasRestantes = dataRepository.getDiasRestantes()

// Solo edad
val edad = dataRepository.getEdad()

// Solo etapa de vida
val etapa = dataRepository.getEtapaDeVida()

// Solo tiempo de pantalla
val screenTime = dataRepository.getScreenTimeMillis()

// TimeLeft completo
val timeLeft = dataRepository.getTimeLeft()

// Uso por categoría
val (ocioMillis, prodMillis) = dataRepository.getUsageByCategory()
```

### 4. Verificar si hay datos disponibles

```kotlin
if (dataRepository.hasData()) {
    // Hay datos guardados
    val data = dataRepository.getDeathTimerData()
} else {
    // No hay datos, mostrar mensaje o valores por defecto
    Text("Aún no hay datos disponibles")
}
```

### 5. Obtener timestamp de última actualización

```kotlin
val lastUpdated = dataRepository.getLastUpdated()
val timeAgo = System.currentTimeMillis() - lastUpdated

if (timeAgo > 60000) { // Más de 1 minuto
    Text("Datos desactualizados")
}
```

## Ejemplos de Uso

### Ejemplo 1: Pantalla de Estadísticas

```kotlin
@Composable
fun EstadisticasScreen() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    val savedData = dataRepository.getDeathTimerData()
    
    Column {
        savedData?.let { data ->
            StatCard("Edad", "${data.edad} años")
            StatCard("Etapa", data.etapaDeVida)
            StatCard("Vida vivida", "${(data.porcentajeVidaVivida * 100).toInt()}%")
            
            // Calcular métricas
            val tdcr = calculateTDCR(data)
            StatCard("TDCR", "${tdcr}%")
        }
    }
}

fun calculateTDCR(data: DeathTimerData): Int {
    val totalTimeAwake = (24 - data.horasSueno) * 60f
    val timeConsumed = (data.screenTimeToday / 60000f)
    return ((timeConsumed / totalTimeAwake) * 100f).toInt()
}
```

### Ejemplo 2: Pantalla de Screen Time

```kotlin
@Composable
fun ScreenTimeScreen() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    
    val screenTime = dataRepository.getScreenTimeMillis()
    val (ocio, prod) = dataRepository.getUsageByCategory()
    
    Column {
        Text("Tiempo total: ${formatMillisToHours(screenTime)}")
        Text("Ocio: ${formatMillisToHours(ocio)}")
        Text("Productividad: ${formatMillisToHours(prod)}")
        
        // Barra de progreso
        LinearProgressIndicator(
            progress = ocio.toFloat() / (ocio + prod).toFloat()
        )
    }
}

fun formatMillisToHours(millis: Long): String {
    val hours = millis / 3_600_000
    val minutes = (millis % 3_600_000) / 60_000
    return "${hours}h ${minutes}m"
}
```

### Ejemplo 3: Pantalla de Valor

```kotlin
@Composable
fun ValorScreen() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    
    val diasRestantes = dataRepository.getDiasRestantes()
    val timeLeft = dataRepository.getTimeLeft()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "MI VALOR ES",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            "$diasRestantes DÍAS",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFDC2626)
        )
        
        timeLeft?.let { time ->
            Row {
                TimeUnit("AÑOS", time.years)
                TimeUnit("MESES", time.months)
                TimeUnit("DÍAS", time.days)
            }
        }
    }
}
```

### Ejemplo 4: Widget o Dashboard

```kotlin
@Composable
fun DashboardWidget() {
    val context = LocalContext.current
    val dataRepository = remember { DeathTimerDataRepository(context) }
    val data = dataRepository.getDeathTimerData()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            data?.let {
                Text("⏰ ${it.diasRestantes} días restantes")
                Text("👤 ${it.edad} años - ${it.etapaDeVida}")
                Text("📱 ${formatTime(it.screenTimeToday)} hoy")
                Text("✨ ${(it.porcentajeVidaVivida * 100).toInt()}% vivido")
            } ?: Text("Cargando datos...")
        }
    }
}
```

## Actualización de Datos

Los datos se actualizan automáticamente cada 5 segundos en `DeathTimerMainScreen`. Para forzar una actualización en otra pantalla:

```kotlin
// Observar cambios con LaunchedEffect
LaunchedEffect(Unit) {
    while (true) {
        delay(5000L) // Verificar cada 5 segundos
        val newData = dataRepository.getDeathTimerData()
        // Actualizar UI
    }
}
```

## Limpiar Datos

```kotlin
// Limpiar todos los datos guardados (útil al cerrar sesión)
dataRepository.clearData()
```

## Notas Importantes

1. **Permisos**: La app necesita permisos de `PACKAGE_USAGE_STATS` para obtener datos de tiempo de pantalla.

2. **Actualización automática**: Los datos se guardan cada 5 segundos mientras `DeathTimerMainScreen` está activa.

3. **Persistencia**: Los datos persisten incluso si la app se cierra, hasta que se limpien explícitamente.

4. **Thread safety**: `SharedPreferences` es thread-safe, pero considera usar coroutines para operaciones pesadas.

5. **Tamaño de datos**: Los datos ocupan aproximadamente 1-2 KB en SharedPreferences.

## Categorías de Apps

### Apps de Ocio (Rojo)
- TikTok (`com.zhiliaoapp.musically`)
- Instagram (`com.instagram.android`)
- Facebook (`com.facebook.katana`)
- YouTube (`com.google.android.youtube`)

### Apps de Productividad (Verde)
- WhatsApp (`com.whatsapp`)
- Trello (`com.trello`)
- Gmail (`com.google.android.gm`)

Para agregar más apps a las categorías, modifica los sets `ocioApps` y `productividadApps` en `DeathTimerMainScreen.kt`.
