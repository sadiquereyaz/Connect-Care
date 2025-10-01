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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
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
fun ParameterCardItem(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    unit: String,
    icon: Int,
    color: Color = Color(0xFF58B05C)
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(20))
                        .background(color = color.copy(alpha = 0.2f))
                        .padding(2.dp)
                )
                Text(text = name, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(start = 36.dp)
            ) {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,

                    )
                Text(
                    text = unit,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 6.dp, bottom = 2.dp),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ParameterCardItemPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.systemBarsPadding().padding(16.dp)
    ) {
        items(
            count = 4
        ) {
            ParameterCardItem(
                name = "Heart Rate",
                value = "72",
                unit = "bpm",
                icon = 0
            )
        }

    }

}

