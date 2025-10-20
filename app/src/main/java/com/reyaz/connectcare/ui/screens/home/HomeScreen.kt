package com.reyaz.connectcare.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.reyaz.connectcare.R
import com.reyaz.connectcare.ui.components.AppLogo
import com.reyaz.connectcare.ui.screens.home.components.ConnectedDeviceItem
import com.reyaz.connectcare.ui.screens.home.components.ParameterCardItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onAuthClick: () -> Unit,
    onStartCalling: () -> Unit,
    onStartScanClick: () -> Unit,
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
            FloatingActionButton(
                onClick = onStartCalling
            ) {
                Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = Icon(Icons.Default.Call, null),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .size(24.dp)
                    )
                }
            }
        }
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            item {
                OutlinedButton(
                    onClick = onStartScanClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Start Scan")
                }
            }
            item {
                Row(
                ) {
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        name = "Heart Rate",
                        value = uiState.heartRate?.toString() ?: "--",
                        unit = "bpm",
                        icon = 0
                    )
                    Spacer(Modifier.width(16.dp))
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        name = "Body Temperature",
                        value = uiState.heartRate?.toString() ?: "--",
                        unit = "bpm",
                        icon = 0
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row() {
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        name = "SpO2",
                        value = uiState.heartRate?.toString() ?: "--",
                        unit = "bpm",
                        icon = 0
                    )
                    Spacer(Modifier.width(16.dp))
                    ParameterCardItem(
                        modifier = Modifier.weight(1f),
                        name = "ECG",
                        value = uiState.heartRate?.toString() ?: "--",
                        unit = "bpm",
                        icon = 0
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(text = "Supported Devices")

                        Spacer(modifier = Modifier.height(2.dp))

                        ConnectedDeviceItem(
                            modifier = Modifier,
                            name = "Galaxy Watch 4",
                            isConnected = true,
                            fetchedStatus = "Last Synced: 10:00 AM"
                        )
                        ConnectedDeviceItem(
                            modifier = Modifier,
                            name = "Galaxy Watch 4",
                            isConnected = true,
                            fetchedStatus = "Last Synced: 10:00 AM"
                        )
                        ConnectedDeviceItem(
                            modifier = Modifier,
                            name = "Galaxy Watch 4",
                            isConnected = true,
                            fetchedStatus = "Last Synced: 10:00 AM"
                        )
                        ConnectedDeviceItem(
                            modifier = Modifier,
                            name = "Galaxy Watch 4",
                            isConnected = true,
                            fetchedStatus = "Last Synced: 10:00 AM"
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(100.dp)) }

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
        uiState = HomeUiState()
    )
}
