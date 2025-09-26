package com.example.nir.data.local

import com.russhwolf.settings.Settings

actual object SettingsFactory {
    actual fun create(): Settings {
        return object : Settings {
            private val map = mutableMapOf<String, Any>()
            
            override fun getBoolean(key: String, defaultValue: Boolean): Boolean = 
                map[key] as? Boolean ?: defaultValue
                
            override fun getBooleanOrNull(key: String): Boolean? = 
                map[key] as? Boolean
                
            override fun getDouble(key: String, defaultValue: Double): Double = 
                map[key] as? Double ?: defaultValue
                
            override fun getDoubleOrNull(key: String): Double? = 
                map[key] as? Double
                
            override fun getFloat(key: String, defaultValue: Float): Float = 
                map[key] as? Float ?: defaultValue
                
            override fun getFloatOrNull(key: String): Float? = 
                map[key] as? Float
                
            override fun getInt(key: String, defaultValue: Int): Int = 
                map[key] as? Int ?: defaultValue
                
            override fun getIntOrNull(key: String): Int? = 
                map[key] as? Int
                
            override fun getLong(key: String, defaultValue: Long): Long = 
                map[key] as? Long ?: defaultValue
                
            override fun getLongOrNull(key: String): Long? = 
                map[key] as? Long
                
            override fun getString(key: String, defaultValue: String): String = 
                map[key] as? String ?: defaultValue
                
            override fun getStringOrNull(key: String): String? = 
                map[key] as? String
                
            override fun hasKey(key: String): Boolean = map.containsKey(key)
            
            override fun putBoolean(key: String, value: Boolean) { map[key] = value }
            override fun putDouble(key: String, value: Double) { map[key] = value }
            override fun putFloat(key: String, value: Float) { map[key] = value }
            override fun putInt(key: String, value: Int) { map[key] = value }
            override fun putLong(key: String, value: Long) { map[key] = value }
            override fun putString(key: String, value: String) { map[key] = value }
            override fun remove(key: String) { map.remove(key) }
            override fun clear() { map.clear() }
            override val keys: Set<String> get() = map.keys.toSet()
            override val size: Int get() = map.size
        }
    }
}