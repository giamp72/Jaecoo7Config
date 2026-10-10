package com.giamp72.jaecoo7config

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "jaecoo_config")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val repo = remember { SettingsRepository(context.dataStore) }
            val settings by repo.settings.collectAsState(initial = CarSettings())

            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme,
                typography = MaterialTheme.typography
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CarSettingsScreen(
                        settings = settings,
                        onVolumeChange = {
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.saveSettings(settings.copy(volume = it))
                            }
                        },
                        onBrightnessChange = {
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.saveSettings(settings.copy(brightness = it))
                            }
                        },
                        onClimateTempChange = {
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.saveSettings(settings.copy(climateTemp = it))
                            }
                        },
                        onDrivingModeChange = {
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.saveSettings(settings.copy(drivingMode = it))
                            }
                        },
                        onReset = {
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.resetToDefault()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CarSettingsScreen(
    settings: CarSettings,
    onVolumeChange: (Int) -> Unit,
    onBrightnessChange: (Int) -> Unit,
    onClimateTempChange: (Float) -> Unit,
    onDrivingModeChange: (DrivingMode) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Jaecoo7Config",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Saved profile",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(shape = RoundedCornerShape(18.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingSlider(
                    label = "Volume",
                    value = settings.volume.toFloat(),
                    valueRange = 0f..100f,
                    onValueChange = { onVolumeChange(it.toInt()) }
                )

                SettingSlider(
                    label = "Brightness",
                    value = settings.brightness.toFloat(),
                    valueRange = 0f..100f,
                    onValueChange = { onBrightnessChange(it.toInt()) }
                )

                SettingSlider(
                    label = "Climate temp",
                    value = settings.climateTemp,
                    valueRange = 15f..30f,
                    onValueChange = { onClimateTempChange(it) }
                )

                Text(text = "Driving mode")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DrivingMode.values().forEach { mode ->
                        Button(
                            onClick = { onDrivingModeChange(mode) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(mode.name)
                        }
                    }
                }
            }
        }

        Card(shape = RoundedCornerShape(18.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Current profile")
                Text("Volume: ${settings.volume}%")
                Text("Brightness: ${settings.brightness}%")
                Text("Climate: ${settings.climateTemp}°C")
                Text("Mode: ${settings.drivingMode.name}")
            }
        }

        Button(
            onClick = onReset,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Reset defaults")
        }
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "$label: ${value.toInt()}${if (label.contains("temp")) "°C" else "%"}")
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
