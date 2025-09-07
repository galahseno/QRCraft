package id.dev.qrcraft

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.util.DebugLogger
import id.dev.core.data.di.coreDataModule
import id.dev.home.data.homeDataModule
import id.dev.home.presentation.component.coil_cache.QrFetcher
import id.dev.home.presentation.component.coil_cache.QrKeyer
import id.dev.home.presentation.di.homePresentationModule
import id.dev.qrcraft.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class QRCraftApp : Application(), ImageLoaderFactory {

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
                coreDataModule,
                homePresentationModule,
                homeDataModule
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.15)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .components {
                add(QrFetcher.Factory())
                add(QrKeyer())
            }
            .build()
    }
}