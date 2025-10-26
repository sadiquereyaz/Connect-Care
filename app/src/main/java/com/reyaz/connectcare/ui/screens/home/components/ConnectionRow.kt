package com.reyaz.connectcare.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConnectionRow(
    modifier: Modifier = Modifier,
    name: String,
    isConnected: Boolean,
    fetchedStatus: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Column(Modifier.fillMaxWidth(0.6f)) {
            Text(text = name, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row {
                if (isConnected) {
                    Text(text = "Connected", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = fetchedStatus, fontSize = 12.sp)
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Button(
            onClick = onClick
        ) {
            val btnText = if (isConnected) "Disconnect" else "Connect"
            Text(btnText)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectedDeviceItemPreview() {
    ConnectionRow(
        modifier = Modifier,
        name = "Galaxy Watch 4",
        isConnected = true,
        fetchedStatus = "Last Synced: 10:00 AM",
    ) {}
}
