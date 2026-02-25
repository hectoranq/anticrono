package com.timedead.relojinverso.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.timedead.relojinverso.data.DeathTimerData
import com.timedead.relojinverso.data.TimeLeft

/**
 * Repository para gestionar datos calculados del Death Timer en SharedPreferences
 */
class DeathTimerDataRepository(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "DeathTimerCalculatedData"
        
        // Keys para SharedPreferences
        private const val KEY_TIME_LEFT_YEARS = "time_left_years"
        private const val KEY_TIME_LEFT_MONTHS = "time_left_months"
        private const val KEY_TIME_LEFT_WEEKS = "time_left_weeks"
        private const val KEY_TIME_LEFT_DAYS = "time_left_days"
        private const val KEY_TIME_LEFT_HOURS = "time_left_hours"
        private const val KEY_TIME_LEFT_MINUTES = "time_left_minutes"
        private const val KEY_TIME_LEFT_SECONDS = "time_left_seconds"
        
        private const val KEY_DIAS_RESTANTES = "dias_restantes"
        private const val KEY_EDAD = "edad"
        private const val KEY_ETAPA_VIDA = "etapa_vida"
        private const val KEY_PORCENTAJE_VIVIDA = "porcentaje_vivida"
        private const val KEY_PORCENTAJE_RESTANTE = "porcentaje_restante"
        
        private const val KEY_SCREEN_TIME_MILLIS = "screen_time_millis"
        private const val KEY_SCREEN_TIME_TODAY = "screen_time_today"
        
        private const val KEY_OCIO_MILLIS = "ocio_millis"
        private const val KEY_PRODUCTIVIDAD_MILLIS = "productividad_millis"
        private const val KEY_OCIO_PORCENTAJE = "ocio_porcentaje"
        private const val KEY_PRODUCTIVIDAD_PORCENTAJE = "productividad_porcentaje"
        
        private const val KEY_HORAS_SUENO = "horas_sueno"
        private const val KEY_HORAS_PRODUCTIVAS = "horas_productivas"
        
        private const val KEY_HEART_DRAWABLE_ID = "heart_drawable_id"
        private const val KEY_LAST_UPDATED = "last_updated"
    }
    
    /**
     * Guardar todos los datos calculados
     */
    fun saveDeathTimerData(data: DeathTimerData) {
        prefs.edit().apply {
            // TimeLeft
            putLong(KEY_TIME_LEFT_YEARS, data.timeLeft.years)
            putLong(KEY_TIME_LEFT_MONTHS, data.timeLeft.months)
            putLong(KEY_TIME_LEFT_WEEKS, data.timeLeft.weeks)
            putLong(KEY_TIME_LEFT_DAYS, data.timeLeft.days)
            putLong(KEY_TIME_LEFT_HOURS, data.timeLeft.hours)
            putLong(KEY_TIME_LEFT_MINUTES, data.timeLeft.minutes)
            putLong(KEY_TIME_LEFT_SECONDS, data.timeLeft.seconds)
            
            // Días restantes y edad
            putLong(KEY_DIAS_RESTANTES, data.diasRestantes)
            putInt(KEY_EDAD, data.edad)
            putString(KEY_ETAPA_VIDA, data.etapaDeVida)
            putFloat(KEY_PORCENTAJE_VIVIDA, data.porcentajeVidaVivida)
            putFloat(KEY_PORCENTAJE_RESTANTE, data.porcentajeVidaRestante)
            
            // Screen time
            putLong(KEY_SCREEN_TIME_MILLIS, data.screenTimeMillis)
            putLong(KEY_SCREEN_TIME_TODAY, data.screenTimeToday)
            
            // Uso por categorías
            putLong(KEY_OCIO_MILLIS, data.ocioMillis)
            putLong(KEY_PRODUCTIVIDAD_MILLIS, data.productividadMillis)
            putFloat(KEY_OCIO_PORCENTAJE, data.ocioPorcentaje)
            putFloat(KEY_PRODUCTIVIDAD_PORCENTAJE, data.productividadPorcentaje)
            
            // Horas
            putInt(KEY_HORAS_SUENO, data.horasSueno)
            putInt(KEY_HORAS_PRODUCTIVAS, data.horasProductivas)
            
            // Heart drawable
            putInt(KEY_HEART_DRAWABLE_ID, data.heartDrawableId)
            
            // Timestamp
            putLong(KEY_LAST_UPDATED, data.lastUpdated)
            
            apply()
        }
    }
    
    /**
     * Obtener todos los datos calculados
     */
    fun getDeathTimerData(): DeathTimerData? {
        val lastUpdated = prefs.getLong(KEY_LAST_UPDATED, 0L)
        if (lastUpdated == 0L) return null
        
        val timeLeft = TimeLeft(
            years = prefs.getLong(KEY_TIME_LEFT_YEARS, 0),
            months = prefs.getLong(KEY_TIME_LEFT_MONTHS, 0),
            weeks = prefs.getLong(KEY_TIME_LEFT_WEEKS, 0),
            days = prefs.getLong(KEY_TIME_LEFT_DAYS, 0),
            hours = prefs.getLong(KEY_TIME_LEFT_HOURS, 0),
            minutes = prefs.getLong(KEY_TIME_LEFT_MINUTES, 0),
            seconds = prefs.getLong(KEY_TIME_LEFT_SECONDS, 0)
        )
        
        return DeathTimerData(
            timeLeft = timeLeft,
            diasRestantes = prefs.getLong(KEY_DIAS_RESTANTES, 0),
            edad = prefs.getInt(KEY_EDAD, 0),
            etapaDeVida = prefs.getString(KEY_ETAPA_VIDA, "") ?: "",
            porcentajeVidaVivida = prefs.getFloat(KEY_PORCENTAJE_VIVIDA, 0f),
            porcentajeVidaRestante = prefs.getFloat(KEY_PORCENTAJE_RESTANTE, 0f),
            screenTimeMillis = prefs.getLong(KEY_SCREEN_TIME_MILLIS, 0),
            screenTimeToday = prefs.getLong(KEY_SCREEN_TIME_TODAY, 0),
            ocioMillis = prefs.getLong(KEY_OCIO_MILLIS, 0),
            productividadMillis = prefs.getLong(KEY_PRODUCTIVIDAD_MILLIS, 0),
            ocioPorcentaje = prefs.getFloat(KEY_OCIO_PORCENTAJE, 0f),
            productividadPorcentaje = prefs.getFloat(KEY_PRODUCTIVIDAD_PORCENTAJE, 0f),
            horasSueno = prefs.getInt(KEY_HORAS_SUENO, 8),
            horasProductivas = prefs.getInt(KEY_HORAS_PRODUCTIVAS, 0),
            heartDrawableId = prefs.getInt(KEY_HEART_DRAWABLE_ID, 0),
            lastUpdated = lastUpdated
        )
    }
    
    /**
     * Obtener solo el TimeLeft
     */
    fun getTimeLeft(): TimeLeft? {
        val lastUpdated = prefs.getLong(KEY_LAST_UPDATED, 0L)
        if (lastUpdated == 0L) return null
        
        return TimeLeft(
            years = prefs.getLong(KEY_TIME_LEFT_YEARS, 0),
            months = prefs.getLong(KEY_TIME_LEFT_MONTHS, 0),
            weeks = prefs.getLong(KEY_TIME_LEFT_WEEKS, 0),
            days = prefs.getLong(KEY_TIME_LEFT_DAYS, 0),
            hours = prefs.getLong(KEY_TIME_LEFT_HOURS, 0),
            minutes = prefs.getLong(KEY_TIME_LEFT_MINUTES, 0),
            seconds = prefs.getLong(KEY_TIME_LEFT_SECONDS, 0)
        )
    }
    
    /**
     * Obtener días restantes
     */
    fun getDiasRestantes(): Long {
        return prefs.getLong(KEY_DIAS_RESTANTES, 0)
    }
    
    /**
     * Obtener edad
     */
    fun getEdad(): Int {
        return prefs.getInt(KEY_EDAD, 0)
    }
    
    /**
     * Obtener etapa de vida
     */
    fun getEtapaDeVida(): String {
        return prefs.getString(KEY_ETAPA_VIDA, "Desconocida") ?: "Desconocida"
    }
    
    /**
     * Obtener tiempo de pantalla
     */
    fun getScreenTimeMillis(): Long {
        return prefs.getLong(KEY_SCREEN_TIME_MILLIS, 0)
    }
    
    /**
     * Obtener uso por categoría
     */
    fun getUsageByCategory(): Pair<Long, Long> {
        return Pair(
            prefs.getLong(KEY_OCIO_MILLIS, 0),
            prefs.getLong(KEY_PRODUCTIVIDAD_MILLIS, 0)
        )
    }
    
    /**
     * Limpiar todos los datos
     */
    fun clearData() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Verificar si hay datos guardados
     */
    fun hasData(): Boolean {
        return prefs.getLong(KEY_LAST_UPDATED, 0L) > 0L
    }
    
    /**
     * Obtener timestamp de última actualización
     */
    fun getLastUpdated(): Long {
        return prefs.getLong(KEY_LAST_UPDATED, 0L)
    }
}
