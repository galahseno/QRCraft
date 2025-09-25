package id.dev.home.presentation.di

import id.dev.home.presentation.camera.CameraScreenViewModel
import id.dev.home.presentation.create_qr.CreateQRViewModel
import id.dev.home.presentation.create_qr_generator.GenerateQrScreenViewModel
import id.dev.home.presentation.history.HistoryViewModel
import id.dev.home.presentation.scan_result.ScanResultScreenViewModel
import id.dev.home.presentation.utils.MediaStoreImageSaver
import id.dev.home.presentation.utils.QrCodeAnalyzer
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homePresentationModule = module {
    viewModelOf(::CameraScreenViewModel)
    viewModelOf(::ScanResultScreenViewModel)
    viewModelOf(::CreateQRViewModel)
    viewModelOf(::GenerateQrScreenViewModel)
    viewModelOf(::HistoryViewModel)

    single {
        QrCodeAnalyzer(androidContext())
    }

    single {
        MediaStoreImageSaver(androidContext())
    }
}