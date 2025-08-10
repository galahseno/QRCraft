package id.dev.qrcraft

import android.app.Application
import id.dev.home.data.di.homeDataModule
import id.dev.home.presentation.di.homePresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QRCraftApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@QRCraftApp)
            androidLogger()
            modules(
                homeDataModule,
                homePresentationModule
            )
        }
    }
}