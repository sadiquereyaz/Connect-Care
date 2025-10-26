package com.reyaz.connectcare.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButton
import com.reyaz.connectcare.domain.model.Service

@Composable
fun ParameterCardItem(
    modifier: Modifier = Modifier,
    service: Service,
    value: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.height(150.dp),
        colors = CardDefaults.cardColors(containerColor = service.color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(32.dp),
        onClick = onClick
    ) {
        Box(Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(service.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = service.icon),
                            contentDescription = service.displayName,
//                        tint = service.color,
                            tint = Color(0xFFFFFFFF),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = service.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                ) {
                    Text(
                        text = value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 42.sp
                    )
                    Text(
                        text = service.unit,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            IconButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) { Icon(Icons.Default.Refresh, "refresh") }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ParameterCardItemPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        items(
            count = 4
        ) {
            ParameterCardItem(
                value = "72", service = Service.BLOOD_PRESSURE
            )
        }

    }

}

