package com.example.nir.data.local

import com.russhwolf.settings.Settings

expect object SettingsFactory {
    fun create(): Settings
}