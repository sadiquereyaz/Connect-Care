package com.reyaz.connectcare.di

import com.reyaz.connectcare.repository.ble.BleManager
import com.reyaz.connectcare.ui.screens.home.HomeViewModel
import com.reyaz.connectcare.ui.screens.scan_dialog.ScanViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {        // small `m`

    single<BleManager> { BleManager(get()) }

    viewModel { ScanViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }

    // A new instance of MyPresenter is created every time 'get()' is called
//    factory { MyPresenter(get()) }
}