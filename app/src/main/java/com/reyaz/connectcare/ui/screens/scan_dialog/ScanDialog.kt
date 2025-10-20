package com.reyaz.connectcare.ui.screens.scan_dialog

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import java.nio.file.WatchEvent

@SuppressLint("MissingPermission")
@Composable
fun ScanDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    refresh: () -> Unit,
    onConnect: (BluetoothDevice) -> Unit,
    scanUiState: ScanUiState
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .heightIn(max = 600.dp, min = 100.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(16.dp)
                ),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (scanUiState.isScanning) {
                        Text("Scanning...")
                        Spacer(Modifier.weight(1f))
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("Scan Result")
                        Spacer(Modifier.weight(1f))
                        Text("Refresh", Modifier.clickable(enabled = true, onClick = refresh))

                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                if (scanUiState.errorMessage != null) {
                    Text(
                        text = scanUiState.errorMessage ?: "Error",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        if (!(scanUiState.isScanning) && scanUiState.deviceList.isEmpty()) {
                            Text(
                                text = "No Device Found",
                                fontWeight = FontWeight.Bold, modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                    items(scanUiState.deviceList) { device ->
                        Column (
                            modifier = Modifier
                                .clickable(enabled = true, onClick = { onConnect(device) })
                        ) {
                            Text(
                                text = "Name: ${device.name}\nAddress: ${device.address}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                            if (device != scanUiState.deviceList.last()) {
                                Spacer(Modifier.height(8.dp))
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}