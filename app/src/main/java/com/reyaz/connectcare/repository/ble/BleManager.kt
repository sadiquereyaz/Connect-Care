package com.reyaz.connectcare.repository.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.UUID

@SuppressLint("MissingPermission")
class BleManager(private val context: Context) {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager.adapter
    private var gatt: BluetoothGatt? = null
    private val handler = Handler(Looper.getMainLooper())

    /**
     * A UUID (Universally Unique Identifier) in BLE identifies a specific service or characteristic provided by a BLE device.
     *
     * Think of it like an address or ID that tells your Android app what kind of data or capability the ESP32 is offering.
     */
    private val heartRateServiceUuid = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
    private val heartRateCharUuid = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")  // Client Characteristics Configured UUID

    private var isScanning = false
    private var currentScanCallback: ScanCallback? = null

    /**
     * Scan for nearby BLE devices and emit results continuously via [onResult].
     * [isScanning] indicates whether scanning is currently active.
     */
    fun scanNearbyBleDevices(
        scanDuration: Long = 10_000L,
        onResult: (foundDevices: Result<List<BluetoothDevice>>, isScanning: Boolean) -> Unit
    ) {
        if (isScanning) return
        val scanner = bluetoothAdapter.bluetoothLeScanner ?: return

        val foundDevices = mutableMapOf<String, BluetoothDevice>()
        isScanning = true

        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                result.device?.let { device ->
                    val address = device.address ?: return
                    if (!foundDevices.containsKey(address) && device.name != null) {
                        foundDevices[address] = device
                        onResult(Result.success(foundDevices.values.toList()), true)
                    }
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                var updated = false
                results.forEach { result ->
                    result.device?.let { device ->
                        val address = device.address ?: return
                        if (!foundDevices.containsKey(address) && device.name != null) {
                            foundDevices[address] = device
                            updated = true
                        }
                    }
                }
                if (updated) onResult(Result.success(foundDevices.values.toList()), true)
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e("BLE_SCAN", "Scan failed: $errorCode")
                onResult(Result.failure(Exception("Scan failed. code: $errorCode")), false)
            }
        }

        currentScanCallback = callback

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner.startScan(null, settings, callback)

        handler.postDelayed({
            stopScan()
            onResult(Result.success(foundDevices.values.toList()), false)
        }, scanDuration)
    }

    /** Stop current BLE scan */
    fun stopScan() {
        val scanner = bluetoothAdapter.bluetoothLeScanner ?: return
        currentScanCallback?.let {
            scanner.stopScan(it)
            currentScanCallback = null
            isScanning = false
        }
    }

    /**
     * Scan for a device by name and connect automatically when found.
     */
    fun scanAndConnect(deviceName: String, onData: (Int) -> Unit) {
        if (isScanning) return
        val scanner = bluetoothAdapter.bluetoothLeScanner ?: return
        isScanning = true

        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val name = result.device.name ?: return
                Log.d("BleManager", "Found device: $name")
                if (name.contains(deviceName, true)) {
                    stopScan()
                    gatt = result.device.connectGatt(context, false, gattCallback(onData))
                }
            }

            override fun onScanFailed(errorCode: Int) {
                stopScan()
                Log.e("BleManager", "Scan failed: $errorCode")
            }
        }

        currentScanCallback = callback
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        scanner.startScan(null, settings, callback)
    }

    /**
     * Connect directly to a device by MAC address (no scan required).
     */
    fun connectByAddress(macAddress: String, onData: (Int) -> Unit) {
        val device = bluetoothAdapter.getRemoteDevice(macAddress)
        gatt = device.connectGatt(context, false, gattCallback(onData))
    }

    /** Common GATT callback for heart rate notifications */
    private fun gattCallback(onData: (Int) -> Unit) = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) gatt.discoverServices()
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val characteristic = gatt.getService(heartRateServiceUuid)
                ?.getCharacteristic(heartRateCharUuid)
            characteristic?.let {
                gatt.setCharacteristicNotification(it, true)
                val descriptor = it.getDescriptor(cccdUuid)
                descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                descriptor?.let { gatt.writeDescriptor(it) }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == heartRateCharUuid) {
                val flag = characteristic.value[0].toInt()
                val format = if (flag and 0x01 != 0)
                    BluetoothGattCharacteristic.FORMAT_UINT16
                else
                    BluetoothGattCharacteristic.FORMAT_UINT8
                val heartRate = characteristic.getIntValue(format, 1)
                onData(heartRate)
            }
        }
    }

    /** Disconnect current GATT connection */
    fun disconnect() {
        gatt?.disconnect()
        gatt?.close()
        gatt = null
    }
}
