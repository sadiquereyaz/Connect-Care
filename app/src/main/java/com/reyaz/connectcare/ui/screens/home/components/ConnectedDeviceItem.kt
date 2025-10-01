package com.reyaz.connectcare.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.connectcare.R

@Composable
fun ConnectedDeviceItem(
    modifier: Modifier = Modifier,
    name: String,
    isConnected: Boolean,
    fetchedStatus: String
) {
        Row (
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ){
            Column {
                Text(text = name, fontSize = 24.sp)
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
                onClick = {}
            ) {
                val btnText = if (isConnected) "Sync" else "Connect"
                Text(btnText)
            }
        }
}

@Preview(showBackground = true)
@Composable
private fun ConnectedDeviceItemPreview() {
    ConnectedDeviceItem(
        modifier = Modifier,
        name = "Galaxy Watch 4",
        isConnected = true,
        fetchedStatus = "Last Synced: 10:00 AM"
    )
}
