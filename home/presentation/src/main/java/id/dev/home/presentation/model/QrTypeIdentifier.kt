package id.dev.home.presentation.model

import kotlinx.serialization.Serializable

@Serializable
enum class QrTypeIdentifier { TEXT, LINK, CONTACT, PHONE, GEO, WIFI }