package id.dev.qrcraft

import android.app.Application
import id.dev.home.presentation.di.homePresentationModule
import id.dev.qrcraft.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QRCraftApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@QRCraftApp)
            androidLogger()
            modules(
                appModule,
                homePresentationModule
            )
        }
    }
}