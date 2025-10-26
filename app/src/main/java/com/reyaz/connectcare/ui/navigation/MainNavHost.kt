package com.reyaz.connectcare.ui.navigation

import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.reyaz.connectcare.ui.screens.home.HomeScreen
import com.reyaz.connectcare.ui.screens.home.HomeViewModel
import com.reyaz.connectcare.domain.model.IotDevice
import com.reyaz.connectcare.ui.screens.scan_dialog.ScanDialog
import com.reyaz.connectcare.ui.screens.scan_dialog.ScanViewModel
import com.reyaz.connectcare.ui.screens.video_call.AgoraVideoScreen
import com.reyaz.connectcare.utils.Constants
import org.koin.androidx.compose.koinViewModel

@RequiresPermission("android.permission.BLUETOOTH_CONNECT")
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
                    modifier = Modifier, shape = RoundedCornerShape(16.dp)
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

        dialog(
            route = NavigationRoute.ScanDialog.route
        ) {
            val viewModel: ScanViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ScanDialog(
                scanUiState = uiState,
                onDismiss = { navController.popBackStack() },
                refresh = { viewModel.startScanning() },
                onConnect = { device ->
                    val homeEntry = navController.getBackStackEntry(NavigationRoute.Home.route)
                    homeEntry.savedStateHandle[Constants.SCAN_RESULT_KEY] =
                        IotDevice(device.name, device.address)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavigationRoute.Home.route
        ) { backStackEntry ->

            val viewModel: HomeViewModel = koinViewModel()

            val selectedDevice by backStackEntry
                .savedStateHandle
                .getStateFlow<IotDevice?>(Constants.SCAN_RESULT_KEY, null)
                .collectAsStateWithLifecycle()

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(selectedDevice) {
                selectedDevice?.let {
                    viewModel.setConnectedDevice(it)
                    backStackEntry.savedStateHandle.remove<String>(Constants.SCAN_RESULT_KEY)
                }
            }
            HomeScreen(
                uiState = uiState,
                onAuthClick = { navController.navigate(NavigationRoute.Authentication.route) },
                onStartCalling = { navController.navigate(NavigationRoute.VideoCall.route) },
                onStartScanClick = {
                    navController.navigate(NavigationRoute.ScanDialog.route)
                },
                observeParameter = { service -> viewModel.onGetParameterClick(service) },
                onDisconnect = { viewModel.disconnectDevice() },
                onErrorDismiss = { viewModel.onErrorDismiss() },
            )
        }

        composable(
            route = NavigationRoute.VideoCall.route
        ) {
            AgoraVideoScreen()
        }

    }

}