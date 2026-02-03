# 🏗️ Reestructuración del Proyecto AntiCrono

## 📋 Resumen de Cambios

El proyecto **AntiCrono** ha sido completamente reestructurado siguiendo la arquitectura **MVI (Model-View-Intent)** combinada con **Clean Architecture**, basándose en el documento `arquitecturaext.md`.

---

## ✅ Cambios Implementados

### 1. **Arquitectura MVI + Clean Architecture**

El proyecto ahora está organizado en 3 capas claramente separadas:

```
com.timedead.relojinverso/
│
├── 📁 data/                          # Capa de Datos
│   ├── intent/                       # Acciones del usuario (MVI)
│   │   ├── AuthIntent.kt             # Intents de autenticación
│   │   └── TimerIntent.kt            # Intents del timer
│   ├── repository/                   # Implementaciones de repositorios
│   │   └── AuthRepositoryImpl.kt     # Mock del repositorio de auth
│   └── state/                        # Estados de la UI (MVI)
│       ├── AuthState.kt              # Estados de autenticación
│       └── TimerState.kt             # Estados del timer
│
├── 📁 domain/                        # Capa de Dominio
│   ├── model/                        # Modelos de dominio
│   │   ├── Route.kt                  # Sealed Class de navegación
│   │   ├── User.kt                   # Modelo de usuario
│   │   └── CountryLifeExpectancy.kt  # Modelo de esperanza de vida
│   └── repository/                   # Interfaces de repositorios
│       └── AuthRepository.kt         # Interface del repositorio
│
└── 📁 presentation/                  # Capa de Presentación
    ├── auth/                         # Módulo de autenticación
    │   ├── SignInScreen.kt           # Pantalla de login
    │   ├── RegisterScreen.kt         # Pantalla de registro
    │   └── ForgotPasswordScreen.kt   # Pantalla de recuperación
    ├── timer/                        # Módulo del Death Timer
    │   └── DeathTimerMainScreen.kt   # Pantalla principal
    ├── navigation/                   # Sistema de navegación
    │   └── NavigationGraph.kt        # Grafo de navegación
    └── viewmodel/                    # ViewModels
        └── AuthViewModel.kt          # ViewModel de autenticación
```

---

## 🎯 Funcionalidades Implementadas

### ✅ Sistema de Autenticación (Mock)

#### 1. **SignInScreen** - Pantalla de Login
- Formulario de email y contraseña
- Validación de credenciales
- Indicador de carga durante login
- Navegación a registro y recuperación de contraseña
- **Usuarios demo incluidos:**
  - 📧 `demo@anticrono.com` / 🔑 `123456`
  - 📧 `test@test.com` / 🔑 `password`

#### 2. **RegisterScreen** - Pantalla de Registro
- Formulario completo (nombre, email, contraseña, confirmación)
- Validación de datos:
  - Campos requeridos
  - Formato de email
  - Longitud mínima de contraseña (6 caracteres)
  - Coincidencia de contraseñas
- Registro de nuevos usuarios en memoria
- Navegación automática al home tras registro exitoso

#### 3. **ForgotPasswordScreen** - Recuperación de Contraseña
- Pantalla placeholder (funcionalidad futura)

---

### ✅ Sistema de Navegación Type-Safe

#### **Route.kt** - Rutas con Sealed Class

```kotlin
sealed class Route(val route: String) {
    object SignIn : Route("sign_in")
    object Register : Route("register")
    object ForgotPassword : Route("forgot_password")
    object DeathTimerHome : Route("death_timer_home")
    object Settings : Route("settings")
}
```

**Ventajas:**
- ✅ Type-safety completa
- ✅ Autocompletado en el IDE
- ✅ Refactoring seguro
- ✅ No hay strings mágicos
- ✅ El compilador detecta rutas faltantes

#### **NavigationGraph.kt** - Grafo de Navegación

```kotlin
NavHost(
    navController = navController,
    startDestination = determineStartDestination(authState)
) {
    composable(Route.SignIn.route) { SignInScreen(...) }
    composable(Route.Register.route) { RegisterScreen(...) }
    composable(Route.ForgotPassword.route) { ForgotPasswordScreen(...) }
    composable(Route.DeathTimerHome.route) { DeathTimerMainScreen(...) }
}
```

**Características:**
- Navegación basada en estado de autenticación
- Limpieza del back stack al hacer login/logout
- Callbacks para acciones de navegación
- Separación de responsabilidades

---

### ✅ Patrón MVI (Model-View-Intent)

#### **Flujo de Datos Unidireccional**

```
User Action (Intent)
    ↓
ViewModel.handleIntent(intent)
    ↓
Repository.operation()
    ↓
State.emit(newState)
    ↓
UI.recompose()
```

#### **Ejemplo: AuthViewModel**

```kotlin
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()
    
    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SignIn -> signIn(intent.email, intent.password)
            is AuthIntent.Register -> register(...)
            is AuthIntent.SignOut -> signOut()
        }
    }
}
```

---

## 🔄 Flujo de Navegación

### Flujo de Autenticación

```
┌─────────────┐
│  SignIn     │ (Pantalla inicial)
└──────┬──────┘
       │
       ├──────────────┬──────────────┐
       │              │              │
       ▼              ▼              ▼
┌───────────┐  ┌───────────┐  ┌──────────────┐
│  Register │  │  Forgot   │  │DeathTimer    │
│           │  │  Password │  │    Home      │
└─────┬─────┘  └───────────┘  └──────────────┘
      │                               ▲
      └───────────────────────────────┘
         (tras registro exitoso)
```

### Gestión de Sesión

- ✅ Verificación automática de sesión al iniciar
- ✅ Persistencia con SharedPreferences
- ✅ Navegación automática según estado de auth
- ✅ Logout limpia el back stack

---

## 📦 Repositorios Mock

### **AuthRepositoryImpl** - Implementación Mock

```kotlin
class AuthRepositoryImpl(context: Context) : AuthRepository {
    
    private val mockUsers = mutableMapOf(
        "demo@anticrono.com" to Pair("Demo User", "123456"),
        "test@test.com" to Pair("Test User", "password")
    )
    
    override suspend fun signIn(email: String, password: String): Result<User> {
        delay(1500) // Simula latencia de red
        // Validación de credenciales...
    }
    
    override suspend fun register(...): Result<User> {
        delay(2000) // Simula latencia de red
        // Registro de usuario...
    }
}
```

**Características:**
- ✅ Simula latencia de red con `delay()`
- ✅ Guarda sesión en SharedPreferences
- ✅ Maneja usuarios en memoria (mockUsers)
- ✅ Validaciones de email duplicado
- ✅ Usa `Result<T>` para manejar errores

---

## 🎨 UI y Diseño

### **SignInScreen**
- Diseño minimalista con logo ⏳
- Campos de email y contraseña
- Botón de login con indicador de carga
- Card con usuarios demo visible
- Animación de transición

### **RegisterScreen**
- Formulario completo de registro
- Validaciones en tiempo real
- Mensajes de error claros
- Navegación a login existente

### **DeathTimerMainScreen** (Refactorizada)
- Versión simplificada de la pantalla original
- Contador regresivo en tiempo real
- Corazón animado con pulso
- Contador de segundos en el centro
- Botón de configuración (futuro)
- Opción de cerrar sesión en top bar

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Uso |
|-----------|-----|
| **Kotlin** 2.0.21 | Lenguaje principal |
| **Jetpack Compose** | UI declarativa |
| **Navigation Compose** 2.9.5 | Navegación type-safe |
| **Kotlin Coroutines** | Asincronía |
| **StateFlow** | Gestión de estado reactivo |
| **ViewModel** | Gestión de estado de UI |
| **SharedPreferences** | Persistencia local |
| **Gson** | Serialización JSON |

---

## 📝 Próximos Pasos

### 🔜 Implementaciones Futuras

1. **Inyección de Dependencias con Koin**
   ```kotlin
   val appModule = module {
       single<AuthRepository> { AuthRepositoryImpl(androidContext()) }
       viewModel { AuthViewModel(get()) }
   }
   ```

2. **Pantalla de Configuración**
   - Selección de fecha de nacimiento
   - Selección de país
   - Horas de sueño
   - Horas productivas

3. **Persistencia de Configuración**
   - Guardar en SharedPreferences o Room
   - Sincronizar con el timer

4. **ViewModel para TimerScreen**
   ```kotlin
   class TimerViewModel(
       private val timerRepository: TimerRepository,
       private val usageStatsRepository: UsageStatsRepository
   ) : ViewModel()
   ```

5. **Integración Real de UsageStats**
   - Acceso real a estadísticas de uso
   - Clasificación de apps (ocio vs productividad)
   - Cálculo de tiempo productivo real

6. **Testing**
   - Unit tests para ViewModels
   - UI tests para Compose screens
   - Tests de repositorios

---

## 🚀 Cómo Usar

### 1. **Compilar el Proyecto**
```bash
./gradlew clean build
```

### 2. **Ejecutar la App**
- Abrir en Android Studio
- Ejecutar en emulador o dispositivo físico

### 3. **Probar Autenticación**
- Usar credenciales demo:
  - Email: `demo@anticrono.com`
  - Password: `123456`
- O crear una cuenta nueva

### 4. **Navegar**
- Login → Death Timer Home
- Cerrar sesión → Volver a Login
- Registro → Validar y crear cuenta

---

## 📊 Comparación: Antes vs Después

| Aspecto | Antes | Después |
|---------|-------|---------|
| **Líneas en MainActivity** | 1355 líneas | 75 líneas |
| **Arquitectura** | Monolítica | MVI + Clean Architecture |
| **Navegación** | Sin navegación | Navigation Compose |
| **Pantallas** | 1 (todo en una) | 5 pantallas separadas |
| **Autenticación** | ❌ No existía | ✅ Implementada |
| **Testabilidad** | ❌ Difícil | ✅ Fácil |
| **Escalabilidad** | ❌ Baja | ✅ Alta |
| **Separación de responsabilidades** | ❌ No | ✅ Sí (3 capas) |
| **Inyección de dependencias** | ❌ No | ✅ Preparada (manual ahora) |
| **Estado reactivo** | `mutableStateOf` local | `StateFlow` global |

---

## 📚 Documentación de Referencia

- [MVI Architecture Pattern](https://medium.com/huawei-developers/stateful-android-apps-with-mvi-architecture-model-view-intent-d106b09bd967)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [StateFlow y SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

## ✅ Checklist de Reestructuración

- [x] Crear estructura de carpetas (data, domain, presentation)
- [x] Implementar Route.kt con Sealed Class
- [x] Crear modelos de dominio (User, CountryLifeExpectancy)
- [x] Crear States (AuthState, TimerState)
- [x] Crear Intents (AuthIntent, TimerIntent)
- [x] Implementar AuthRepository (mock)
- [x] Implementar AuthViewModel
- [x] Crear SignInScreen
- [x] Crear RegisterScreen
- [x] Crear ForgotPasswordScreen
- [x] Refactorizar DeathTimerMainScreen
- [x] Implementar NavigationGraph
- [x] Actualizar MainActivity
- [ ] Implementar Koin DI
- [ ] Crear TimerViewModel
- [ ] Implementar pantalla de configuración
- [ ] Agregar tests

---

**Fecha de reestructuración:** Febrero 1, 2026  
**Desarrollador:** GitHub Copilot  
**Proyecto:** AntiCrono (Death Timer)  
**Package:** `com.timedead.relojinverso`
