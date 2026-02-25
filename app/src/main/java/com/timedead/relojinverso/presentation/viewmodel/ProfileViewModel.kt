package com.timedead.relojinverso.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timedead.relojinverso.domain.model.UserProfile
import com.timedead.relojinverso.domain.model.User
import com.timedead.relojinverso.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el perfil del usuario
 */
class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state.asStateFlow()
    
    init {
        loadProfile()
    }
    
    fun loadProfile() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            
            // Obtener usuario actual
            val currentUser = authRepository.getCurrentUser()
            
            if (currentUser == null) {
                _state.value = ProfileState.Error("Usuario no autenticado")
                return@launch
            }
            
            // Obtener perfil del usuario
            val result = authRepository.getUserProfile()
            result.fold(
                onSuccess = { profile ->
                    _state.value = ProfileState.Success(
                        user = currentUser,
                        profile = profile
                    )
                },
                onFailure = { error ->
                    _state.value = ProfileState.Error(
                        error.message ?: "Error al cargar perfil"
                    )
                }
            )
        }
    }
    
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            
            val result = authRepository.saveUserProfile(profile)
            result.fold(
                onSuccess = {
                    // Recargar perfil actualizado
                    loadProfile()
                },
                onFailure = { error ->
                    _state.value = ProfileState.Error(
                        error.message ?: "Error al actualizar perfil"
                    )
                }
            )
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}

/**
 * Estados del perfil
 */
sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(
        val user: User,
        val profile: UserProfile
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
