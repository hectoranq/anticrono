package com.timedead.relojinverso.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timedead.relojinverso.data.intent.AuthIntent
import com.timedead.relojinverso.data.state.AuthState
import com.timedead.relojinverso.domain.model.UserProfile
import com.timedead.relojinverso.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para el módulo de autenticación
 * Implementa el patrón MVI
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SignIn -> signIn(intent.email, intent.password)
            is AuthIntent.Register -> register(intent.name, intent.email, intent.password, intent.profile)
            is AuthIntent.SignOut -> signOut()
            is AuthIntent.CheckAuthStatus -> checkAuthStatus()
        }
    }
    
    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            
            val result = authRepository.signIn(email, password)
            
            _state.value = if (result.isSuccess) {
                AuthState.Success(result.getOrThrow())
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
    
    private fun register(name: String, email: String, password: String, profile: UserProfile) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            
            val result = authRepository.register(name, email, password, profile)
            
            _state.value = if (result.isSuccess) {
                AuthState.Success(result.getOrThrow())
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
    
    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _state.value = AuthState.NotAuthenticated
        }
    }
    
    private fun checkAuthStatus() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            _state.value = if (user != null) {
                AuthState.Success(user)
            } else {
                AuthState.NotAuthenticated
            }
        }
    }
}
