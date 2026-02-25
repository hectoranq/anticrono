package com.timedead.relojinverso

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.timedead.relojinverso.data.repository.AuthRepositoryImpl
import com.timedead.relojinverso.presentation.navigation.NavigationGraph
import com.timedead.relojinverso.presentation.viewmodel.AuthViewModel
import com.timedead.relojinverso.ui.theme.RelojinversoTheme

/**
 * MainActivity - Actividad principal con arquitectura MVI y Navigation Compose
 * 
 * Esta actividad implementa:
 * - Arquitectura MVI (Model-View-Intent)
 * - Clean Architecture (Data, Domain, Presentation layers)
 * - Navigation Compose para navegación type-safe
 * - Inyección de dependencias manual (preparado para Koin)
 */
class MainActivity : ComponentActivity() {
    
    private lateinit var authViewModel: AuthViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Verificar permisos de UsageStats
        if (!hasUsageStatsPermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        
        // Inicializar repositorio y ViewModel (DI manual - futuro: usar Koin)
        val authRepository = AuthRepositoryImpl(applicationContext)
        authViewModel = AuthViewModel(authRepository)
        
        setContent {
            RelojinversoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        authRepository = authRepository
                    )
                }
            }
        }
    }
    
    /**
     * Verifica si la app tiene permiso para acceder a las estadísticas de uso
     */
    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            "android:get_usage_stats",
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
}
