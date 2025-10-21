package com.reyaz.connectcare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.reyaz.connectcare.R

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFFFF0000)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(R.drawable.ic_logo),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppLogoPreview() {
    Scaffold(
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
//                    AsyncImage(
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .clip(shape = CircleShape)
//                            .clickable(enabled = true, onClick = {  }),
//                        model = R.drawable.ic_launcher_background,
//                        contentDescription = null,
//                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Text("+")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

        }
    }
}