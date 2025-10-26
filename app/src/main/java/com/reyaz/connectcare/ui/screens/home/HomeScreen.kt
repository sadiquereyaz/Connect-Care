package com.reyaz.connectcare.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.reyaz.connectcare.R
import com.reyaz.connectcare.domain.model.IotDevice
import com.reyaz.connectcare.domain.model.Service
import com.reyaz.connectcare.ui.components.AppLogo
import com.reyaz.connectcare.ui.screens.home.components.ConnectionRow
import com.reyaz.connectcare.ui.screens.home.components.ParameterCardItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAuthClick: () -> Unit,
    onStartCalling: () -> Unit,
    onStartScanClick: () -> Unit,
    onDisconnect: () -> Unit,
    onErrorDismiss: () -> Unit,
    observeParameter: (Service) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    AppLogo(
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                title = {
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(stringResource(R.string.app_name), lineHeight = 16.sp)
                        Text(text = "Welcome to ConnectCare!", fontSize = 12.sp, lineHeight = 8.sp)
                    }
                },
                actions = {
                    AsyncImage(
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(shape = CircleShape)
                            .clickable(enabled = true, onClick = onAuthClick),
                        model = "https://avatars.githubusercontent.com/u/118601913?v=4",
                        contentDescription = null,
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onStartCalling) {
                Icon(Icons.Default.Call, contentDescription = null)
            }
        },
        snackbarHost = {
            uiState.errorMessage?.let {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {},
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    dismissAction = {
                        IconButton(
                            onErrorDismiss
                        ){
                            Icon(
                                Icons.Default.Close,
                                "cross",
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier
//                                    .padding(end = 8.dp)
                            )
                        }
                    },
                ) {
                    Text(text = it)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // First Row
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        service = Service.HEART_RATE,
                        value = uiState.heartRate?.toString() ?: "--",
                        onClick = { observeParameter(Service.HEART_RATE) }
                    )
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        service = Service.THERMOMETER,
                        value = uiState.bodyTemperature?.toString() ?: "--",
                        onClick = { observeParameter(Service.THERMOMETER) }
                    )
                }
            }

            // Second Row
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        service = Service.SPO2,
                        value = uiState.spo2?.toString() ?: "--"
                    )
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        service = Service.BLOOD_PRESSURE,
                        value = uiState.bloodPressure?.let { "${it.first}/${it.second}" } ?: "--"
                    )
                }
            }

            // Connected Device
            item {
                ConnectionRow(
                    name = uiState.connectedDevice?.name ?: "Not Connected",
                    isConnected = uiState.connectedDevice != null,
                    fetchedStatus = "Last Synced: 10:00 AM",
                    onClick = {
                        // todo: check if bluetooth is on
                        if (uiState.connectedDevice == null) {
                            onStartScanClick()
                        } else {
                            onDisconnect()
                        }
                    }
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onAuthClick = {},
        onStartCalling = {},
        onStartScanClick = {},
        uiState = HomeUiState(connectedDevice = IotDevice("Dummy Name", "ff:ad:32:2d:43")),
        onDisconnect = { },
        observeParameter = {},
        onErrorDismiss = {},
    )
}
