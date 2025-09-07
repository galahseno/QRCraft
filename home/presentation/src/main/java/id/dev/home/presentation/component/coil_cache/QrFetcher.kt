package id.dev.home.presentation.component.coil_cache

import android.content.res.Resources
import androidx.core.graphics.drawable.toDrawable
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.request.Options
import id.dev.home.presentation.utils.generateQrBitmap

class QrFetcher(
    private val data: QrImageModel,
    private val resources: Resources
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val bmp = generateQrBitmap(
            text = data.text
        )
        val drawable = bmp.toDrawable(resources)
        return DrawableResult(
            drawable = drawable,
            isSampled = false,
            dataSource = DataSource.MEMORY
        )
    }

    class Factory : Fetcher.Factory<QrImageModel> {
        override fun create(
            data: QrImageModel,
            options: Options,
            imageLoader: ImageLoader
        ): Fetcher = QrFetcher(data, resources = options.context.resources)
    }
}
