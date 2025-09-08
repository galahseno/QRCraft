package id.dev.home.presentation.model

enum class QrTypeIdentifier {
    TEXT, LINK, CONTACT, PHONE, GEO, WIFI;

    companion object {
        fun fromString(type: String): QrTypeIdentifier? {
            return QrTypeIdentifier.entries.find { it.name.equals(type, ignoreCase = true) }
        }
    }
}