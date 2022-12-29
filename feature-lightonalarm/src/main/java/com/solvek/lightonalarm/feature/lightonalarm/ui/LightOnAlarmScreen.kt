package com.solvek.lightonalarm.feature.lightonalarm.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.solvek.lightonalarm.core.ui.MyApplicationTheme
import com.solvek.lightonalarm.feature.lightonalarm.R
import com.solvek.lightonalarm.feature.lightonalarm.ui.LightOnAlarmUiState.Success

@Composable
fun LightOnAlarmScreen(modifier: Modifier = Modifier, viewModel: LightOnAlarmViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState<LightOnAlarmUiState>(
        initialValue = LightOnAlarmUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }
    if (state is Success) {
        val successState = state as Success
        LightOnAlarmScreen(
            successState.isAlarmEnabled,
            successState.isPlaying,
            onChangeAlarmSetting = { viewModel.changeAlarmSetting(it) },
            onStopPlaying = { viewModel.stopPlaying() },
            modifier = modifier
        )
    }
}

@Composable
internal fun LightOnAlarmScreen(
    isEnabled: Boolean,
    isPlaying: Boolean,
    onChangeAlarmSetting: (enabled: Boolean) -> Unit,
    onStopPlaying: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row (verticalAlignment = Alignment.CenterVertically){
            Checkbox(checked = isEnabled, onCheckedChange = onChangeAlarmSetting)
            Spacer(modifier = Modifier.size(6.dp))
            Text(stringResource(R.string.alarm_active))
        }
        if (isPlaying){
            Spacer(Modifier.size(20.dp))
            Button(onClick = onStopPlaying) {
                Text(stringResource(R.string.stop))
            }
        }
        Spacer(Modifier.size(40.dp))
        Text(stringResource(R.string.help))
        Spacer(Modifier.size(5.dp))
        Text(stringResource(R.string.stop_russians))
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        LightOnAlarmScreen(
            isEnabled = true,
            isPlaying = false,
            onChangeAlarmSetting = {},
            onStopPlaying = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        LightOnAlarmScreen(isEnabled = true,
            isPlaying = true,
            onChangeAlarmSetting = {},
            onStopPlaying = {})
    }
}
