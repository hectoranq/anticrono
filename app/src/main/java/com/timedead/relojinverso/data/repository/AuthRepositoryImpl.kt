package com.timedead.relojinverso.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import com.timedead.relojinverso.domain.model.User
import com.timedead.relojinverso.domain.model.UserProfile
import com.timedead.relojinverso.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Implementación del repositorio de autenticación con Firebase Authentication
 */
class AuthRepositoryImpl(private val context: Context) : AuthRepository {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: email,
                    name = firebaseUser.displayName ?: email.substringBefore('@')
                )
                
                // Guardar sesión local
                prefs.edit().apply {
                    putString("user_id", user.id)
                    putString("user_email", user.email)
                    putString("user_name", user.name)
                    putBoolean("is_authenticated", true)
                    apply()
                }
                
                Result.success(user)
            } else {
                Result.failure(Exception("Error al iniciar sesión"))
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("Usuario no encontrado"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Credenciales inválidas"))
        } catch (e: Exception) {
            Result.failure(Exception("Error de autenticación: ${e.message}"))
        }
    }
    
    override suspend fun register(name: String, email: String, password: String, profile: UserProfile): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                // Actualizar el perfil del usuario con el nombre
                val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()
                
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: email,
                    name = name
                )
                
                // Guardar sesión local
                prefs.edit().apply {
                    putString("user_id", user.id)
                    putString("user_email", user.email)
                    putString("user_name", user.name)
                    putBoolean("is_authenticated", true)
                    apply()
                }
                
                // Guardar perfil en Realtime Database
                saveUserProfile(profile)
                
                Result.success(user)
            } else {
                Result.failure(Exception("Error al registrar usuario"))
            }
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("Contraseña muy débil"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("El email ya está registrado"))
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar: ${e.message}"))
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            prefs.edit().clear().apply()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al cerrar sesión: ${e.message}"))
        }
    }
    
    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: firebaseUser.email?.substringBefore('@') ?: ""
            )
        } else {
            // Fallback a SharedPreferences si hay sesión guardada
            val isAuth = prefs.getBoolean("is_authenticated", false)
            if (isAuth) {
                User(
                    id = prefs.getString("user_id", "") ?: "",
                    email = prefs.getString("user_email", "") ?: "",
                    name = prefs.getString("user_name", "") ?: ""
                )
            } else {
                null
            }
        }
    }
    
    override fun isUserAuthenticated(): Flow<Boolean> = flow {
        emit(auth.currentUser != null)
    }
    
    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado"))
            
            database.getReference("users")
                .child(uid)
                .child("profile")
                .setValue(profile)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al guardar perfil: ${e.message}"))
        }
    }
    
    override suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado"))
            
            val snapshot = database.getReference("users")
                .child(uid)
                .child("profile")
                .get()
                .await()
            
            val profile = snapshot.getValue(UserProfile::class.java)
                ?: return Result.failure(Exception("Perfil no encontrado"))
            
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener perfil: ${e.message}"))
        }
    }
}

