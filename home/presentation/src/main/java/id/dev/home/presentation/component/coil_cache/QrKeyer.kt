package id.dev.home.presentation.component.coil_cache

import coil.key.Keyer
import coil.request.Options

class QrKeyer : Keyer<QrImageModel> {
    override fun key(data: QrImageModel, options: Options): String =
        "qr:${data.text}"
}
