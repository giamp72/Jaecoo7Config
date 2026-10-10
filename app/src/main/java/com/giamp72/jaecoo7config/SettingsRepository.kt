package com.giamp72.jaecoo7config

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore

val VOLUME_KEY = intPreferencesKey("volume")
val BRIGHTNESS_KEY = intPreferencesKey("brightness")
val CLIMATE_TEMP_KEY = floatPreferencesKey("climate_temp")
val DRIVING_MODE_KEY = stringPreferencesKey("driving_mode")

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    val settings: Flow<CarSettings> = dataStore.data.map { prefs ->
        CarSettings(
            volume = prefs[VOLUME_KEY] ?: 35,
            brightness = prefs[BRIGHTNESS_KEY] ?: 70,
            climateTemp = prefs[CLIMATE_TEMP_KEY] ?: 22f,
            drivingMode = prefs[DRIVING_MODE_KEY]?.let { value ->
                DrivingMode.valueOf(value)
            } ?: DrivingMode.NORMAL
        )
    }

    suspend fun saveSettings(settings: CarSettings) {
        dataStore.edit { prefs ->
            prefs[VOLUME_KEY] = settings.volume
            prefs[BRIGHTNESS_KEY] = settings.brightness
            prefs[CLIMATE_TEMP_KEY] = settings.climateTemp
            prefs[DRIVING_MODE_KEY] = settings.drivingMode.name
        }
    }

    suspend fun resetToDefault() {
        saveSettings(CarSettings())
    }
}
