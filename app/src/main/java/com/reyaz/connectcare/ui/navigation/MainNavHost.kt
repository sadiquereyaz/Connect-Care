package com.reyaz.connectcare.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.reyaz.connectcare.ui.screens.home.HomeScreen
import com.reyaz.connectcare.ui.screens.video_call.AgoraVideoScreen

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
//        startDestination = NavigationRoute.VideoCall.route,
        startDestination = NavigationRoute.Home.route,
//        startDestination =  NavigationRoute.Authentication.route,
        modifier = modifier,
    ) {

        dialog(
            route = NavigationRoute.Authentication.route
        ) {
            Dialog(
                onDismissRequest = { navController.popBackStack() },
                properties = DialogProperties(
                    usePlatformDefaultWidth = true
                )
            ) {
                Surface(
                    modifier = Modifier
                    ,shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Authentication Dialog")
                    }
                }
            }
        }

        composable(
            route = NavigationRoute.Home.route
        ) {
            HomeScreen(
                onAuthClick = { navController.navigate(NavigationRoute.Authentication.route) },
                onStartCalling = { navController.navigate(NavigationRoute.VideoCall.route) }
            )
        }

        composable(
            route = NavigationRoute.VideoCall.route
        ) {
            AgoraVideoScreen()
        }

    }

}