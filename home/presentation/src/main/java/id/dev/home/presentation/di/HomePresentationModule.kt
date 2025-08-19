package id.dev.home.presentation.di

import id.dev.home.presentation.camera.CameraScreenViewModel
import id.dev.home.presentation.create_qr.CreateQRViewModel
import id.dev.home.presentation.create_qr_generator.GenerateQrScreenViewModel
import id.dev.home.presentation.scanResult.ScanResultScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homePresentationModule = module {
    viewModelOf(::CameraScreenViewModel)
    viewModelOf(::ScanResultScreenViewModel)
    viewModelOf(::CreateQRViewModel)
    viewModelOf(::GenerateQrScreenViewModel)
}