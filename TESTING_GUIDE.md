# 🧪 Guía de Pruebas - Firebase Authentication

## ⚡ Inicio Rápido

### 1. **Construir el Proyecto**

Si el build falla por archivos bloqueados, cierra Android Studio y ejecuta:

```bash
cd d:\PROYECTOMEXICO\anticrono
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

O desde Android Studio:
- **Build → Rebuild Project**
- **Run → Run 'app'**

---

## 🧪 Pruebas de Funcionalidad

### ✅ Test 1: Registro de Nuevo Usuario

1. Abre la aplicación
2. Presiona **"¿No tienes cuenta? Regístrate"**
3. Completa el formulario:
   - Nombre: `Test Usuario`
   - Email: `test@ejemplo.com`
   - Password: `123456` (mínimo 6 caracteres)
   - Confirmar password: `123456`
4. Presiona **"CREAR CUENTA"**
5. ✅ **Esperado**: Navegación automática al Home

---

### ✅ Test 2: Login con Usuario Existente

1. Cierra sesión si estás logueado
2. Ingresa:
   - Email: `test@ejemplo.com`
   - Password: `123456`
3. Presiona **"ENTRAR"**
4. ✅ **Esperado**: Navegación al Home

---

### ✅ Test 3: Error - Credenciales Inválidas

1. Ingresa:
   - Email: `test@ejemplo.com`
   - Password: `password_incorrecto`
2. Presiona **"ENTRAR"**
3. ✅ **Esperado**: Mensaje "Credenciales inválidas"

---

### ✅ Test 4: Error - Usuario No Encontrado

1. Ingresa:
   - Email: `usuario_inexistente@ejemplo.com`
   - Password: `123456`
2. Presiona **"ENTRAR"**
3. ✅ **Esperado**: Mensaje "Usuario no encontrado"

---

### ✅ Test 5: Error - Email Ya Registrado

1. Intenta registrar un usuario con email ya usado:
   - Email: `test@ejemplo.com` (ya existe)
   - Password: `123456`
2. Presiona **"CREAR CUENTA"**
3. ✅ **Esperado**: Mensaje "El email ya está registrado"

---

### ✅ Test 6: Error - Contraseña Débil

1. Intenta registrar con password corta:
   - Email: `nuevo@ejemplo.com`
   - Password: `12345` (solo 5 caracteres)
2. Presiona **"CREAR CUENTA"**
3. ✅ **Esperado**: Mensaje "Contraseña muy débil"

---

### ✅ Test 7: Persistencia de Sesión

1. Loguéate con un usuario
2. **Cierra completamente la aplicación**
3. Vuelve a abrir la app
4. ✅ **Esperado**: Sesión mantenida, usuario en Home

---

### ✅ Test 8: Cerrar Sesión

1. Estando logueado, ve a la pantalla de **Perfil**
2. Presiona **"Cerrar Sesión"** (si está implementado)
3. ✅ **Esperado**: Navegación a SignIn Screen

---

## 🔍 Verificación en Firebase Console

### Ver Usuarios Registrados

1. Abre [Firebase Console](https://console.firebase.google.com/)
2. Selecciona tu proyecto
3. Ve a **Authentication → Users**
4. ✅ Deberías ver los usuarios registrados desde la app

### Ver Actividad de Login

1. En Firebase Console → Authentication
2. Ve a la pestaña **"Users"**
3. Verás:
   - Email del usuario
   - UID (User ID)
   - Fecha de creación
   - Último inicio de sesión

---

## 📱 Logs en Logcat

### Ver Logs de Firebase Auth

En Android Studio → Logcat, filtra por:
```
FirebaseAuth
```

Verás:
- `signInWithEmailAndPassword:success`
- `createUserWithEmailAndPassword:success`
- Errores detallados si algo falla

---

## 🐛 Solución de Problemas Comunes

### Error: "An internal error has occurred"

**Causa**: Firebase no inicializado o `google-services.json` faltante

**Solución**:
1. Verifica que existe `app/google-services.json`
2. Sincroniza Gradle: **File → Sync Project with Gradle Files**

---

### Error: "Network Error"

**Causa**: Sin conexión a Internet o firewall

**Solución**:
1. Verifica conexión de internet del dispositivo/emulador
2. Revisa permisos en `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

---

### Error: "Build Failed - R.jar locked"

**Causa**: Archivos del build bloqueados por procesos

**Solución**:
```bash
# Opción 1: Cerrar Android Studio y ejecutar
.\gradlew.bat clean

# Opción 2: Reiniciar el equipo

# Opción 3: Eliminar la carpeta build manualmente
Remove-Item -Recurse -Force .\app\build
```

---

### Usuarios Mock Ya No Funcionan

❌ **Usuarios anteriores (mock) YA NO FUNCIONAN:**
- `demo@anticrono.com` / `123456`
- `test@test.com` / `password`

✅ **Ahora debes crear usuarios reales:**
- Desde la app (RegisterScreen)
- Desde Firebase Console

---

## 🎯 Checklist de Verificación

- [ ] Proyecto compila sin errores
- [ ] Registro de usuario funciona
- [ ] Login con usuario existente funciona
- [ ] Errores se muestran correctamente
- [ ] Sesión persiste al cerrar/abrir app
- [ ] Usuario aparece en Firebase Console
- [ ] Cerrar sesión funciona

---

## 📞 Soporte

Si encuentras errores:

1. **Revisa Logcat** para mensajes de Firebase
2. **Verifica Firebase Console** para ver estado de usuarios
3. **Chequea `google-services.json`** está en `app/`
4. **Sincroniza Gradle** después de cualquier cambio

---

## ✨ Funcionalidades Implementadas

✅ Sign In con Email/Password  
✅ Register (Crear cuenta)  
✅ Manejo de errores específicos  
✅ Persistencia de sesión  
✅ Sign Out  
✅ Obtener usuario actual  

---

## 🚀 Próximos Pasos (Opcional)

### Implementar Reset Password

En `ForgotPasswordScreen.kt`:
```kotlin
// En el repositorio
suspend fun resetPassword(email: String): Result<Unit> {
    return try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(Exception("Error al enviar email: ${e.message}"))
    }
}
```

### Agregar Email Verification

```kotlin
// Después de crear usuario
firebaseUser.sendEmailVerification().await()
```

---

## 📚 Recursos

- [Firebase Auth Docs](https://firebase.google.com/docs/auth/android/start)
- [Firebase Console](https://console.firebase.google.com/)
- Documentación del proyecto: `FIREBASE_AUTH_IMPLEMENTATION.md`
