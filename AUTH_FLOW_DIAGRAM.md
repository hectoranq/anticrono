# 🔄 Flujo de Autenticación - Firebase Auth + MVI

## 📊 Diagrama de Flujo Completo

```
┌─────────────────────────────────────────────────────────────────┐
│                         SIGN IN FLOW                            │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│  SignInScreen │  Usuario ingresa email y password
│   (UI Layer)  │
└───────┬───────┘
        │ onClick() { onIntent(...) }
        ▼
┌───────────────────────────────────────────┐
│   AuthIntent.SignIn(email, password)      │  Intent (Acción)
└───────────────────┬───────────────────────┘
                    │
                    ▼
        ┌──────────────────────┐
        │   AuthViewModel      │  handleIntent()
        │  (Presentation)      │
        └──────────┬───────────┘
                   │ signIn(email, password)
                   │ _state = AuthState.Loading
                   ▼
    ┌─────────────────────────────┐
    │   AuthRepository Interface   │
    │       (Domain Layer)         │
    └──────────────┬──────────────┘
                   │
                   ▼
    ┌──────────────────────────────────────┐
    │   AuthRepositoryImpl (Data Layer)    │
    │                                       │
    │   🔥 Firebase Authentication          │
    └──────────────┬───────────────────────┘
                   │
                   │ auth.signInWithEmailAndPassword(email, password)
                   │
                   ▼
    ┌───────────────────────────────────────┐
    │      Firebase Backend (Cloud)         │
    │   ✓ Valida credenciales               │
    │   ✓ Retorna User UID                  │
    │   ✓ Genera Token de sesión            │
    └──────────────┬────────────────────────┘
                   │
                   │ Result<User>
                   ▼
    ┌──────────────────────────────────────┐
    │   AuthRepositoryImpl                  │
    │   ✓ Crea User(id, email, name)       │
    │   ✓ Guarda en SharedPreferences      │
    └──────────────┬───────────────────────┘
                   │
                   │ Result<User>
                   ▼
        ┌──────────────────────┐
        │   AuthViewModel      │
        │  _state = AuthState  │
        │    .Success(user)    │
        └──────────┬───────────┘
                   │
                   │ StateFlow emite nuevo estado
                   ▼
┌──────────────────────────────┐
│      SignInScreen            │
│  LaunchedEffect(state) {     │
│    when(state) {              │
│      Success → Navigate Home  │
│      Error → Show Error       │
│    }                          │
│  }                            │
└──────────────────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                        REGISTER FLOW                            │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│ RegisterScreen│  Usuario completa formulario
└───────┬───────┘
        │ onIntent(AuthIntent.Register(...))
        ▼
┌───────────────────────────────────────────────────────┐
│   AuthIntent.Register(name, email, password)          │
└───────────────────┬───────────────────────────────────┘
                    │
                    ▼
        ┌──────────────────────┐
        │   AuthViewModel      │
        └──────────┬───────────┘
                   │ register()
                   │ _state = Loading
                   ▼
    ┌────────────────────────────────────────┐
    │   AuthRepositoryImpl                   │
    │                                         │
    │   🔥 auth.createUserWithEmailAndPass   │
    └──────────────┬─────────────────────────┘
                   │
                   ▼
    ┌───────────────────────────────────────┐
    │      Firebase Backend                 │
    │   ✓ Crea nuevo usuario                │
    │   ✓ Valida email único                │
    │   ✓ Valida password strength          │
    └──────────────┬────────────────────────┘
                   │
                   │ FirebaseUser
                   ▼
    ┌──────────────────────────────────────┐
    │   AuthRepositoryImpl                  │
    │   ✓ updateProfile(displayName)       │
    │   ✓ Crea User model                  │
    │   ✓ Guarda en SharedPreferences      │
    └──────────────┬───────────────────────┘
                   │
                   │ Result<User>
                   ▼
        ┌──────────────────────┐
        │   AuthViewModel      │
        │  _state = Success    │
        └──────────┬───────────┘
                   │
                   ▼
┌──────────────────────────────┐
│    RegisterScreen            │
│  → Navigate to Home          │
└──────────────────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                         ERROR FLOW                              │
└─────────────────────────────────────────────────────────────────┘

    Firebase Auth Error
           │
           ▼
┌─────────────────────────────────────────────┐
│   catch (e: FirebaseAuthException) {        │
│     return Result.failure(                  │
│       Exception("Mensaje User-Friendly")    │
│     )                                        │
│   }                                          │
└──────────────┬──────────────────────────────┘
               │
               ▼
    ┌──────────────────────┐
    │   AuthViewModel      │
    │  _state = AuthState  │
    │    .Error(message)   │
    └──────────┬───────────┘
               │
               ▼
┌──────────────────────────────┐
│      UI Screen               │
│  Text(                       │
│    errorMessage,             │
│    color = Red               │
│  )                           │
└──────────────────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                    PERSISTENCE FLOW                             │
└─────────────────────────────────────────────────────────────────┘

    App Starts
        │
        ▼
┌──────────────────────┐
│   AuthViewModel      │
│   init {             │
│     checkAuthStatus()│
│   }                  │
└──────────┬───────────┘
           │
           ▼
┌─────────────────────────────────────────┐
│   AuthRepository.getCurrentUser()       │
│                                          │
│   val firebaseUser = auth.currentUser   │
└──────────┬──────────────────────────────┘
           │
           ├─── User != null ───────────────┐
           │                                 │
           │                                 ▼
           │                    ┌───────────────────────┐
           │                    │  Return User          │
           │                    │  _state = Success     │
           │                    └───────────────────────┘
           │
           └─── User == null ──────────────┐
                                            │
                                            ▼
                               ┌────────────────────────┐
                               │  Return null           │
                               │  _state = NotAuth      │
                               └────────────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                      SIGN OUT FLOW                              │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐
│ PerfilScreen │  Usuario presiona "Cerrar Sesión"
└───────┬───────┘
        │ authViewModel.handleIntent(AuthIntent.SignOut)
        ▼
┌──────────────────────┐
│   AuthViewModel      │
│   signOut()          │
└──────────┬───────────┘
           │
           ▼
┌─────────────────────────────────────────┐
│   AuthRepository.signOut()              │
│                                          │
│   🔥 auth.signOut()                      │
│   ✓ Clear SharedPreferences             │
└──────────┬──────────────────────────────┘
           │
           │ Result<Unit>
           ▼
┌──────────────────────┐
│   AuthViewModel      │
│  _state =            │
│    NotAuthenticated  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────────────┐
│      UI (Any Screen)         │
│  Navigate to SignIn          │
│  popUpTo(0) { inclusive }    │
└──────────────────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                      DATA LAYERS                                │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│              PRESENTATION LAYER                     │
├─────────────────────────────────────────────────────┤
│  SignInScreen.kt        (Compose UI)                │
│  RegisterScreen.kt      (Compose UI)                │
│  AuthViewModel.kt       (Business Logic)            │
└───────────────────┬─────────────────────────────────┘
                    │ uses
                    ▼
┌─────────────────────────────────────────────────────┐
│              DOMAIN LAYER (Clean)                   │
├─────────────────────────────────────────────────────┤
│  AuthRepository.kt      (Interface)                 │
│  User.kt                (Model)                     │
│  Route.kt               (Navigation)                │
└───────────────────┬─────────────────────────────────┘
                    │ implements
                    ▼
┌─────────────────────────────────────────────────────┐
│              DATA LAYER                             │
├─────────────────────────────────────────────────────┤
│  AuthRepositoryImpl.kt  (Firebase Implementation)   │
│    ├─ 🔥 FirebaseAuth                                │
│    └─ SharedPreferences (Backup)                    │
│                                                      │
│  AuthIntent.kt          (User Actions)              │
│  AuthState.kt           (UI States)                 │
└─────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                      STATE MANAGEMENT                           │
└─────────────────────────────────────────────────────────────────┘

sealed class AuthState {
    object Idle             // Estado inicial
    object Loading          // Cargando (llamada a Firebase)
    data class Success(     // Login/Register exitoso
        val user: User
    )
    data class Error(       // Error en auth
        val message: String
    )
    object NotAuthenticated // Sin sesión
}

Transiciones:
   Idle → Loading → Success → Home
   Idle → Loading → Error → Mostrar mensaje
   Success → SignOut → NotAuthenticated → SignIn


┌─────────────────────────────────────────────────────────────────┐
│              FIREBASE EXCEPTIONS HANDLED                        │
└─────────────────────────────────────────────────────────────────┘

FirebaseAuthInvalidUserException
    ↓
"Usuario no encontrado"

FirebaseAuthInvalidCredentialsException
    ↓
"Credenciales inválidas"

FirebaseAuthWeakPasswordException
    ↓
"Contraseña muy débil"

FirebaseAuthUserCollisionException
    ↓
"El email ya está registrado"

Generic Exception
    ↓
"Error de autenticación: {message}"
```

## 🔑 Componentes Clave

### 1. **FirebaseAuth Instance**
```kotlin
private val auth: FirebaseAuth = FirebaseAuth.getInstance()
```

### 2. **Async Operations**
```kotlin
auth.signInWithEmailAndPassword(email, password).await()
```

### 3. **Result Pattern**
```kotlin
return Result.success(user)  // ✅
return Result.failure(error) // ❌
```

### 4. **StateFlow**
```kotlin
private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
val state: StateFlow<AuthState> = _state.asStateFlow()
```

## 🎯 Ventajas de Esta Arquitectura

1. **Separation of Concerns**
   - UI solo renderiza estados
   - ViewModel maneja lógica
   - Repository maneja datos

2. **Testeable**
   - Mock del Repository fácil
   - ViewModel sin dependencias de Android

3. **Escalable**
   - Agregar nuevos providers (Google, Facebook) es fácil
   - Cambiar Firebase por otro backend es sencillo

4. **Type-Safe**
   - Estados definidos con Sealed Classes
   - Intents tipados
   - Navegación type-safe

5. **Reactive**
   - StateFlow observa cambios
   - UI reacciona automáticamente
   - No callbacks anidados
