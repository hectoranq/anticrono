package com.timedead.relojinverso.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.timedead.relojinverso.domain.model.User
import com.timedead.relojinverso.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.util.UUID

/**
 * Implementación Mock del repositorio de autenticación
 * Simula llamadas a un backend con SharedPreferences
 */
class AuthRepositoryImpl(private val context: Context) : AuthRepository {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val mockUsers = mutableMapOf(
        "demo@anticrono.com" to Pair("Demo User", "123456"),
        "test@test.com" to Pair("Test User", "password")
    )
    
    override suspend fun signIn(email: String, password: String): Result<User> {
        // Simular delay de red
        delay(1500)
        
        val userData = mockUsers[email]
        return if (userData != null && userData.second == password) {
            val user = User(
                id = UUID.randomUUID().toString(),
                email = email,
                name = userData.first
            )
            // Guardar sesión
            prefs.edit().apply {
                putString("user_id", user.id)
                putString("user_email", user.email)
                putString("user_name", user.name)
                putBoolean("is_authenticated", true)
                apply()
            }
            Result.success(user)
        } else {
            Result.failure(Exception("Credenciales inválidas"))
        }
    }
    
    override suspend fun register(name: String, email: String, password: String): Result<User> {
        // Simular delay de red
        delay(2000)
        
        return if (mockUsers.containsKey(email)) {
            Result.failure(Exception("El email ya está registrado"))
        } else {
            // Registrar nuevo usuario
            mockUsers[email] = Pair(name, password)
            
            val user = User(
                id = UUID.randomUUID().toString(),
                email = email,
                name = name
            )
            
            // Guardar sesión
            prefs.edit().apply {
                putString("user_id", user.id)
                putString("user_email", user.email)
                putString("user_name", user.name)
                putBoolean("is_authenticated", true)
                apply()
            }
            
            Result.success(user)
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        prefs.edit().clear().apply()
        return Result.success(Unit)
    }
    
    override suspend fun getCurrentUser(): User? {
        val isAuth = prefs.getBoolean("is_authenticated", false)
        return if (isAuth) {
            User(
                id = prefs.getString("user_id", "") ?: "",
                email = prefs.getString("user_email", "") ?: "",
                name = prefs.getString("user_name", "") ?: ""
            )
        } else {
            null
        }
    }
    
    override fun isUserAuthenticated(): Flow<Boolean> = flow {
        emit(prefs.getBoolean("is_authenticated", false))
    }
}
