package id.dev.home.presentation.di

import id.dev.home.presentation.camera.CameraScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homePresentationModule = module {
    viewModelOf(::CameraScreenViewModel)


}