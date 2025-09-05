package id.dev.core.presentation.utils

import id.dev.core.domain.model.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        else -> UiText.DynamicString("Unknown error")
    }
}