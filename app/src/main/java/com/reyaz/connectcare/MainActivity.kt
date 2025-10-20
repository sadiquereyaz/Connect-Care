package com.reyaz.connectcare

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.reyaz.connectcare.ui.theme.ConnectCareTheme
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyaz.connectcare.repository.ble.BleManager
import com.reyaz.connectcare.ui.navigation.MainNavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
@Composable
fun HeartRateScreen(viewModel: HeartRateViewModel = viewModel()) {
    val heartRate by viewModel.heartRate.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Heart Rate: $heartRate bpm", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { viewModel.startScanning("realme C30s") }) {
            Text("Connect to LightBlue")
        }
    }
}

class HeartRateViewModel(application: Application) : AndroidViewModel(application) {
    private val _heartRate = MutableStateFlow(0)
    val heartRate = _heartRate.asStateFlow()
    private val bleManager = BleManager(application)

    fun startScanning(deviceName: String) {
        bleManager.scanAndConnect(deviceName) { bpm ->
            _heartRate.value = bpm
        }
    }
}

class MainActivity : ComponentActivity() {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        // Handle permissions result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestBluetoothPermissions()
        requestCameraPermissions()
        setContent {
            ConnectCareTheme {
                MainNavHost()
//                HeartRateScreen()
            }
        }
    }

    private fun requestBluetoothPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        requestPermissions.launch(permissions)
    }
    private fun requestCameraPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        requestPermissions.launch(permissions)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun HeartRateMonitorScreen(
    bluetoothAdapter: BluetoothAdapter?,
    context: Context
) {
    var heartRate by remember { mutableStateOf(0) }
    var isConnected by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Ready to scan") }
    var bluetoothGatt by remember { mutableStateOf<BluetoothGatt?>(null) }

    val HR_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
    val HR_CHARACTERISTIC_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
    val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    val gattCallback = remember {
        object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        isConnected = true
                        statusMessage = "Connected! Discovering services..."
                        gatt.discoverServices()
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        isConnected = false
                        statusMessage = "Disconnected"
                        heartRate = 0
                    }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val service = gatt.getService(HR_SERVICE_UUID)
                    val characteristic = service?.getCharacteristic(HR_CHARACTERISTIC_UUID)

                    characteristic?.let {
                        gatt.setCharacteristicNotification(it, true)
                        val descriptor = it.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)
                        descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)
                        statusMessage = "Receiving heart rate data!"
                    } ?: run {
                        statusMessage = "Heart rate service not found"
                    }
                } else {
                    statusMessage = "Service discovery failed"
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                if (characteristic.uuid == HR_CHARACTERISTIC_UUID) {
                    val data = characteristic.value
                    if (data != null && data.size >= 2) {
                        heartRate = data[1].toInt() and 0xFF
                    }
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS && characteristic.uuid == HR_CHARACTERISTIC_UUID) {
                    val data = characteristic.value
                    if (data != null && data.size >= 2) {
                        heartRate = data[1].toInt() and 0xFF
                    }
                }
            }
        }
    }

    val scanCallback = remember {
        object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                if (device.name == "ESP32_HeartRate") {
                    bluetoothAdapter?.bluetoothLeScanner?.stopScan(this)
                    isScanning = false
                    statusMessage = "Found ESP32_HeartRate! Connecting..."
                    bluetoothGatt = device.connectGatt(context, false, gattCallback)
                }
            }

            override fun onScanFailed(errorCode: Int) {
                isScanning = false
                statusMessage = "Scan failed: $errorCode"
            }
        }
    }

    fun startScanning() {
        if (bluetoothAdapter == null) {
            statusMessage = "Bluetooth not available"
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            statusMessage = "Please enable Bluetooth"
            return
        }

        // Check permissions
        val hasPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (!hasPermissions) {
            statusMessage = "Please grant Bluetooth permissions"
            return
        }

        isScanning = true
        statusMessage = "Scanning for ESP32_HeartRate..."
        bluetoothAdapter.bluetoothLeScanner?.startScan(scanCallback)

        // Stop scanning after 10 seconds
        CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
            delay(10000)
            if (isScanning) {
                bluetoothAdapter.bluetoothLeScanner?.stopScan(scanCallback)
                isScanning = false
                if (!isConnected) {
                    statusMessage = "Device not found. Is nRF Connect advertising?"
                }
            }
        }
    }

    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        isConnected = false
        heartRate = 0
        statusMessage = "Disconnected"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Heart Rate Display
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    color = if (isConnected) Color(0xFFE91E63) else Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Heart",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (heartRate > 0) "$heartRate" else "--",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "BPM",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Status Message
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = statusMessage,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Control Buttons
        if (!isConnected) {
            Button(
                onClick = { startScanning() },
                enabled = !isScanning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (isScanning) "Scanning..." else "Connect to ESP32_HeartRate",
                    fontSize = 16.sp
                )
            }
        } else {
            Button(
                onClick = { disconnect() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Disconnect",
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Setup Instructions:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Open nRF Connect on another device\n" +
                            "2. Go to Advertiser tab\n" +
                            "3. Create 'ESP32_HeartRate' with Heart Rate service\n" +
                            "4. Start advertising\n" +
                            "5. Tap 'Connect' above",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}